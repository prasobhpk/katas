package ru.network.resend

import groovy.transform.Immutable
import groovyx.gpars.activeobject.ActiveMethod
import groovyx.gpars.activeobject.ActiveObject
import groovyx.gpars.actor.DefaultActor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import org.junit.Test
import ru.network.actors.Bus
import ru.network.actors.PrintingBusListener
import ru.util.Pomodoro
import static groovyx.gpars.actor.Actors.actor
import static java.util.concurrent.TimeUnit.MILLISECONDS
import static java.util.concurrent.TimeUnit.SECONDS

/**
 * User: dima
 * Date: 11/03/2012
 */
@Pomodoro("3")
class ReSend1 {
  @Test void senderAndReceiverEstablishConnection_WhenMessageDeliveryIsReliable() {
    def bus = new Bus()
    PrintingBusListener.listenTo(bus)
    def receiver = new Receiver(bus)
    def sender = new Sender(bus)

    assert receiver.hasReceivedSeqId()
    assert sender.hasEstablishedConnection()
  }

  @Test void senderAndReceiverEstablishConnection_WhenMessageDeliveryIsUnReliable() {
    def bus = new Bus()
    bus.addFilter { new Random().nextBoolean() }
    PrintingBusListener.listenTo(bus)
    def receiver = new Receiver(bus)
    def sender = new Sender(bus)

    assert receiver.hasReceivedSeqId(10, SECONDS)
    assert sender.hasEstablishedConnection(10, SECONDS)
  }

  @Test void senderSendsOneMessage_WhenMessageDeliveryIsReliable() {
    def bus = new Bus()
    PrintingBusListener.listenTo(bus)
    def receiver = new Receiver(bus)
    def sender = new Sender(bus)

    assert receiver.hasReceivedSeqId()
    assert sender.hasEstablishedConnection()

    sender.send("message one")
    assert receiver.takeLastReceived() == "message one"
    assert sender.takeLastConfirmed() == "message one"
  }

  @Test void senderSendsOneMessage_WhenMessageDeliveryIsUnReliable() {
    def bus = new Bus()
    bus.addFilter { new Random().nextBoolean() }
    PrintingBusListener.listenTo(bus)
    def receiver = new Receiver(bus)
    def sender = new Sender(bus)
    sender.send("message one")

    assert receiver.hasReceivedSeqId(10, SECONDS)
    assert sender.hasEstablishedConnection(10, SECONDS)
    assert receiver.takeLastReceived(10, SECONDS) == "message one"
    assert sender.takeLastConfirmed(10, SECONDS) == "message one"
  }

  @Test void senderSendsSeveralMessages_WhenMessageDeliveryIsUnReliable() {
    100.times{
      println "-------------"
      aaa()
    }
  }

  def aaa() {
    List messages = (0..10).collect { it.toString() }

    def bus = new Bus()
    bus.addFilter { new Random().nextBoolean() }
    PrintingBusListener.listenTo(bus)
    def receiver = new Receiver(bus)
    def sender = new Sender(bus)

    messages.each { sender.send(it) }
    assert receiver.hasReceivedSeqId(10, SECONDS)
    assert sender.hasEstablishedConnection(10, SECONDS)
    assert messages.collect { receiver.takeLastReceived(10, SECONDS) }.sort() == messages.sort()
    // TODO confirmed messages can have duplicates
    assert messages.collect { sender.takeLastConfirmed(10, SECONDS) }.sort() == messages.sort()
  }

  @ActiveObject static class Sender {
    Bus bus
    RePublisher rePublisher
    int lastId
    def confirmedMessages = new LinkedBlockingQueue()

    def establishedConnection = new CountDownLatch(1)

    Sender(Bus bus) {
      this.bus = bus
      this.rePublisher = new RePublisher(bus)
      bus.addListener(this)
      send("syncSeqId")
    }

    @ActiveMethod def send(String payload) {
      def message = new Message(payload, nextId())
      bus.publish(message)
      rePublisher.scheduleRepublishingOf(message)
    }

    @ActiveMethod def onMessage(message) {
      if (message instanceof AckMessage) {
        if (message.originalMessage.payload == "syncSeqId") {
          establishedConnection.countDown()
        } else {
          confirmedMessages << message.originalMessage.payload
        }
        rePublisher.unScheduleRepublishingOf(message.originalMessage)
      }
    }

    private int nextId() {
      ++lastId
    }

    boolean hasEstablishedConnection(int timeout = 100, TimeUnit timeUnit = MILLISECONDS) {
      establishedConnection.await(timeout, timeUnit)
    }

    def takeLastConfirmed(int timeout = 100, TimeUnit timeUnit = MILLISECONDS) {
      confirmedMessages.poll(timeout, timeUnit)
    }
  }

  static class RePublisher {
    def actors = new ConcurrentHashMap() // accessed from two actors, but it's thread-safe anyway
    Bus bus

    RePublisher(Bus bus) {
      this.bus = bus
    }

    def scheduleRepublishingOf(Message message) {
      def actor = actor {
        react(25, MILLISECONDS) {
          if (it == DefaultActor.TIMEOUT) {
            bus.publish(message)
            scheduleRepublishingOf(message)
          }
        }
      }
      actors.put(message.id, actor)
    }

    def unScheduleRepublishingOf(Message message) {
      def actor = actors.remove(message.id)
      actor?.send("cancel republishing")
    }
  }

  @ActiveObject static class Receiver {
    Bus bus
    def receivedSeqId = new CountDownLatch(1)
    def receivedMessages = new LinkedBlockingQueue()
    def missingIds = new HashSet()
    int lastId

    Receiver(Bus bus) {
      this.bus = bus
      bus.addListener(this)
    }

    @ActiveMethod def onMessage(message) {
      if (message instanceof Message) {
        if (message.payload == "syncSeqId") {
          if (!hasReceivedSeqId(0)) {
            lastId = message.id
            receivedSeqId.countDown()
          }
          bus.publish(new AckMessage(message))
        } else if (hasReceivedSeqId(0)) {
          if (lastId + 1 == message.id) {
            lastId = message.id
            receivedMessages << message.payload
          } else if (lastId + 1 < message.id) {
            missingIds.addAll((lastId + 1..<message.id).toList())
            lastId = message.id
            receivedMessages << message.payload
          } else if (lastId + 1 > message.id) {
            if (missingIds.remove(message.id)) {
              receivedMessages << message.payload
            }
          }
          bus.publish(new AckMessage(message))
        }
      }
    }

    boolean hasReceivedSeqId(int timeout = 100, TimeUnit timeUnit = MILLISECONDS) {
      receivedSeqId.await(timeout, timeUnit)
    }

    def takeLastReceived(int timeout = 100, TimeUnit timeUnit = MILLISECONDS) {
      receivedMessages.poll(timeout, timeUnit)
    }
  }

  @Immutable static class AckMessage {
    Message originalMessage

    @Override String toString() {
      "AckMessage{" + "originalMessage=" + originalMessage + '}'
    }
  }

  @Immutable static class Message {
    String payload
    int id

    @Override String toString ( ) {
      "Message{" + "payload='" + payload + '\'' + ", id=" + id + '}'
    }
  }
}
