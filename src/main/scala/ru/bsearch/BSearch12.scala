package ru.bsearch

import org.scalatest.matchers.ShouldMatchers
import org.junit.Test

/**
 * User: dima
 * Date: 06/06/2012
 */

class BSearch12 extends ShouldMatchers {
	case class State(value: Int, seq: Seq[Int], shift: Int, pos: Option[Option[Int]]) {
		def withPos(pos: Option[Option[Int]]) = State(value, seq, shift, pos)
		def withSeq(seq: Seq[Int]) = State(value, seq, shift, pos)
		def withShift(shift: Int) = State(value, seq, shift, pos)

		def next(): State = {
			val state = this
			if (state.seq.isEmpty) return state.withPos(Some(None))

			val midPos = state.seq.size / 2
			val midValue = state.seq(midPos)

			if (state.value == midValue) state.withPos(Some(Some(state.shift + midPos)))
			else if (state.value < midValue) state.withSeq(state.seq.slice(0, midPos))
			else state.withSeq(state.seq.slice(midPos + 1, state.seq.size)).withShift(midPos + 1)
		}
	}
	
	@Test def shouldProgressFromOneStateOfBinarySearchToAnother() {
		State(0, Seq(), 0, None).next should equal(State(0, Seq(), 0, Some(None)))
		State(1, Seq(), 0, None).next should equal(State(1, Seq(), 0, Some(None)))

		State(0, Seq(1), 0, None).next should equal(State(0, Seq(), 0, None))
		State(1, Seq(1), 0, None).next should equal(State(1, Seq(1), 0, Some(Some(0))))
		State(2, Seq(1), 0, None).next should equal(State(2, Seq(), 1, None))

		next(State(0, Seq(1, 2), 0, None)) should equal(State(0, Seq(1), 0, None))
		next(State(1, Seq(1, 2), 0, None)) should equal(State(1, Seq(1), 0, None))
		next(State(2, Seq(1, 2), 0, None)) should equal(State(2, Seq(1, 2), 0, Some(Some(1))))
		next(State(3, Seq(1, 2), 0, None)) should equal(State(3, Seq(), 2, None))

		next(State(0, Seq(1, 2, 3), 0, None)) should equal(State(0, Seq(1), 0, None))
		next(State(1, Seq(1, 2, 3), 0, None)) should equal(State(1, Seq(1), 0, None))
		next(State(2, Seq(1, 2, 3), 0, None)) should equal(State(2, Seq(1, 2, 3), 0, Some(Some(1))))
		next(State(3, Seq(1, 2, 3), 0, None)) should equal(State(3, Seq(3), 2, None))

		next(State(3, Seq(3), 2, None)) should equal(State(3, Seq(3), 2, Some(Some(2))))
	}

	@Test def shouldFindIndexOfAnElementInASequence() {
		binarySearch(0, Seq()) should equal(None)

		binarySearch(0, Seq(1)) should equal(None)
		binarySearch(1, Seq(1)) should equal(Some(0))
		binarySearch(2, Seq(1)) should equal(None)

		binarySearch(0, Seq(1, 2)) should equal(None)
		binarySearch(1, Seq(1, 2)) should equal(Some(0))
		binarySearch(2, Seq(1, 2)) should equal(Some(1))
		binarySearch(3, Seq(1, 2)) should equal(None)

		binarySearch(0, Seq(1, 2, 3)) should equal(None)
		binarySearch(1, Seq(1, 2, 3)) should equal(Some(0))
		binarySearch(2, Seq(1, 2, 3)) should equal(Some(1))
		binarySearch(3, Seq(1, 2, 3)) should equal(Some(2))
		binarySearch(4, Seq(1, 2, 3)) should equal(None)
	}

	def binarySearch(value: Int, seq: Seq[Int]): Option[Int] = {
		doFind(State(value, seq, 0, None))
	}

	def doFind(state: State): Option[Int] = {
		if (state.pos.isDefined) state.pos.get
		else doFind(next(state))
	}

	def next(state: State): State = {
		if (state.seq.isEmpty) return state.withPos(Some(None))

		val midPos = state.seq.size / 2
		val midValue = state.seq(midPos)

		if (state.value == midValue) state.withPos(Some(Some(state.shift + midPos)))
		else if (state.value < midValue) state.withSeq(state.seq.slice(0, midPos))
		else state.withSeq(state.seq.slice(midPos + 1, state.seq.size)).withShift(midPos + 1)
	}
}