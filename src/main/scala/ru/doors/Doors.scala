package ru.doors

import org.junit.Test
import org.scalatest.matchers.ShouldMatchers
import ru.util.Arcade

/**
 * User: dima
 * Date: 25/06/2012
 */

@Arcade
class Doors extends ShouldMatchers {
	@Test def aaa() {
		val doors = Array.fill(10){ false }
		1.to(doors.size).foreach { stepSize =>
			(stepSize - 1).until(doors.size, stepSize).foreach { i => doors(i) = !doors(i)}
		}
		doors should equal(Array(true, false, false, true, false, false, false, false, true, false))
	}

	@Test def bbb() {
		val size = 10
		1.to(size).foldLeft(Seq.fill(size){false}) { (acc, stepSize) =>
			acc.zipWithIndex.map{ v => if ((v._2 + 1) % stepSize == 0) v._1 else !v._1 }
		} should equal(Seq(true, false, false, true, false, false, false, false, true, false))
	}

}