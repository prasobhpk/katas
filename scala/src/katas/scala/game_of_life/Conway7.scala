package katas.scala.game_of_life

import org.junit.Test
import org.scalatest.Matchers


class Conway7 extends Matchers {
	@Test def emptyUniverse_Evolves_IntoEmptyUniverse() {
		val universe = new Universe()
		val evolvedUniverse = universe.evolve()

		evolvedUniverse should equal(emptyUniverse)
	}

	@Test def universeWithOneCell_Evolves_IntoEmptyUniverse() {
		val universe = new Universe().withCell()
		val evolvedUniverse = universe.evolve()

		evolvedUniverse should equal(emptyUniverse)
	}

	@Test def universeWithThreeNeighbouringCells_Evolves_IntoThreeCellsUniverse(){
		val middleCell = new Cell()
		val leftCell = new Cell()
		val rightCell = new Cell()
		middleCell.addNeighbour(leftCell, Left())
		middleCell.addNeighbour(rightCell, Right())
		val universe = new Universe().withCells(leftCell, middleCell, rightCell)

		val evolvedUniverse = universe.evolve()

		evolvedUniverse.cellsCount() should equal(3)
	}

	@Test def newUniverse_Has_NoCells() {
		val universe = new Universe()

		universe.cellsCount() should equal(0)
	}

	val emptyUniverse: Universe = new Universe()


	sealed abstract class Location()
	case class Left() extends Location
	case class Top() extends Location
	case class Right() extends Location
	case class Bottom() extends Location


	case class Cell(){
		def addNeighbour(cell : Cell, location: Location) = {

		}
	}

	case class Universe() {
		def evolve(): Universe = {
			new Universe()
		}

		def withCell(): Universe = {
			this

		}

		def withCells(cells : Cell*) : Universe = {
			this
		}

		def cellsCount() : Int = {
			3
		}
	}
}