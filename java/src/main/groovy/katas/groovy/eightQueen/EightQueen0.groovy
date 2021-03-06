package katas.groovy.eightQueen

import org.junit.Test

/**
 * User: DKandalov
 */
class EightQueen0 {
  @Test
  public void shouldSolve8QueenProblem() {
//    println solve(2)
    asData(solve(2), 2).each{ it.each {println it}; println "" }
    asData(solve(5), 5).each{ it.each {println it}; println "" }
  }

  def solve(int size, int row = 0, def solution = []) {
    if (row >= size && isValid(solution)) {
      return [solution]
    }
    def solutions = []

    // used to have outer loop for rows which cause incorrect results.. recursion is the way to go in this case
    for (int column = 0; column < size; column++) {
      def newSolution = deepCopy(solution + [[row, column]])
      if (isValid(newSolution)) {
        solutions += solve(size, (int) row + 1, newSolution)
      } else {
//        println " - " + newSolution
      }
//      if (row == size - 1)
    }

//    solutions << [[1,1], [3, 4]] << [[0,0], [2,1]]
    solutions
  }

  static def deepCopy(List<List> lists) {
    def result = []
    lists.each { list -> result << new ArrayList(list) }
    result
  }

  static def asData(solutions, size) {
    solutions.collect { solution ->
      def data = []
      (0..size - 1).each { data << (0..size - 1).collect {0} }

      solution.each {
        def row = it[0]
        def column = it[1]
        data[row][column] = 1
      }
      data
    }
  }

  boolean isValid(def solution) {
    boolean sameRowOrCol = solution.any { position ->
      (solution - [position]).any { position[0] == it[0] || position[1] == it[1] } // used "-position" instead of "-[position]"; typo: was position[1] == position[1]
    }
    boolean sameDiagonal = solution.any { position ->
      (solution - [position]).any { Math.abs((int) (position[0] - it[0])) == Math.abs((int) (position[1] - it[1])) }
    }
    !sameRowOrCol && !sameDiagonal
  }

  @Test
  public void shouldDetermineIfSolutionIsValid() {
    assert isValid([])
    assert isValid([[0, 0]])
    assert !isValid([[0, 0], [5, 0]])
    assert !isValid([[0, 0], [1, 1]])
    assert isValid([[0, 0], [1, 2]])
    assert !isValid([[0, 0], [1, 2], [2, 2]])

    // 0 0 0
    // X 0 0
    // 0 X 0
    assert !isValid([[2, 1], [1, 0]])
    // 0 0 0
    // 0 0 X
    // 0 X 0
    assert !isValid([[2, 1], [1, 2]])
    assert !isValid([[2, 1], [0, 3]])
  }

}
