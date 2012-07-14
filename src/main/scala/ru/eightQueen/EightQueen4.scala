package ru.eightQueen

import org.scalatest.matchers.ShouldMatchers
import org.junit.Test

/**
 * User: dima
 * Date: 14/07/2012
 */

class EightQueen4 extends ShouldMatchers {
	type Queen = (Int, Int)
	type Solution = Seq[Queen]

	@Test def shouldFindSolutionForBoardOfSize_4() {
		val solution = solveForBoardOfSize(4)
		asPrintableSolution(solution, 4) should equal(
			"""Vector(X, Q, X, X)
Vector(X, X, X, Q)
Vector(Q, X, X, X)
Vector(X, X, Q, X)

Vector(X, X, Q, X)
Vector(Q, X, X, X)
Vector(X, X, X, Q)
Vector(X, Q, X, X)"""
		)
	}

	def solveForBoardOfSize(boardSize: Int): Seq[Solution] = {
		def solve(from: Queen, solution: Solution): Seq[Solution] = {
			if (solution.size == boardSize) return Seq(solution)

			var result = Seq[Solution]()
			Range(0, boardSize).foreach { row =>
				Range(0, boardSize).foreach { col =>
					if (row > from._1 || (row == from._1 && col >= from._2)) {
						val queen = (row, col)
						if (isCorrectMove(solution, queen))
							result = result ++ solve(queen, solution :+ queen)
					}
				}
			}
			result
		}
		solve((0, 0), Seq())
	}

	@Test def shouldDetermineIsQueensAreOnTheSameDiagonal() {
		val row = 7
		val col = 5
		isCorrectMove(Seq((row, col)), (row + 2, col - 2)) should be(false) // top-right
		isCorrectMove(Seq((row, col)), (row - 2, col - 2)) should be(false) // top-left
		isCorrectMove(Seq((row, col)), (row - 2, col + 2)) should be(false) // bottom-left
		isCorrectMove(Seq((row, col)), (row + 2, col + 2)) should be(false) // bottom-right
	}

	def isCorrectMove(solution: Solution, newQueen: Queen): Boolean = {
		def notOnTheSameRowOrColumn = solution.forall { queen => queen._1 != newQueen._1 && queen._2 != newQueen._2 }
		def notOnTheSameDiagonal = solution.forall{ queen => (queen._1 - newQueen._1).abs != (queen._2 - newQueen._2).abs }
		notOnTheSameRowOrColumn && notOnTheSameDiagonal
	}

	def asPrintableSolution(solutions: Seq[Solution], boardSize: Int): String = {
		solutions.foldLeft("") { (result, solution) => result + asPrintableBoard(solution, boardSize) + "\n\n" }
	}

	def asPrintableBoard(solution: Seq[Queen], boardSize: Int): String = {
		Range(0, boardSize).map { row =>
			Range(0, boardSize).map { col =>
				if (solution.contains((row, col))) "Q" else "X"
			}
		}.mkString("\n")
	}
}