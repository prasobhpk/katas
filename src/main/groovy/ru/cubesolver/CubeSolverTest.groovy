package ru.cubesolver

import org.junit.Test

class CubeSolverTest {
  private static String _ = " "
  private static String x = "x"

  @Test void "can assemble some surfaces"() {
    def allSurfaces = combinationsOf(surface(
            [_, _, _],
            [_, x, _],
            [_, _, _]
    ))
    def cube = assembleAsCube(allSurfaces.take(30))

    assert cube.front == surface(
            [_, x, _],
            [_, x, _],
            [_, _, _]
    )
    assert cube.top == surface(
            [x, x, _],
            [_, x, _],
            [_, _, _]
    )
    assert cube.right == surface(
            [x, x, x],
            [x, x, _],
            [_, _, _]
    )
    assert cube.bottom == surface(
            [x, x, x],
            [x, x, x],
            [_, _, _]
    )
    assert cube.left == surface(
            [_, x, x],
            [x, x, x],
            [_, _, _]
    )
    assert cube.back == surface(
            [_, _, _],
            [_, x, x],
            [x, x, x]
    )
    cube.each { key, value ->
      println("----")
      println(key)
      println(value)
    }
  }

  private static List<Map> matchingRotationsOf(List<Surface> surfaces, Closure accepted) {
    surfaces.collectMany { Surface surface ->
      rotationsOf(surface)
              .findAll { accepted(it) }
              .collect{ [surface: surface, rotatedSurface: it] }
    }
  }

  private static Map assembleAsCube(List<Surface> surfaces) {
    if (surfaces.size() < 6) throw new IllegalAccessException()
    def result = assembleAsCube(surfaces.first(), surfaces.tail())
    result.front = surfaces.first()
    result
  }

  private static Map assembleAsCube(Surface frontSurface, List<Surface> surfaces, Map<String, Surface> cube = [:]) {
    if (surfaces.empty || cube.size() == 5) return cube

    if (cube.top == null) {
      def topSurfaces = matchingRotationsOf(surfaces) {
        areConnectible(frontSurface.topSide(), it.bottomSide())
      }
      if (topSurfaces.empty) return cube
      cube.top = topSurfaces.find {
        Map newResult = cube.clone() as Map
        newResult.top = it.rotatedSurface
        Map subResult = assembleAsCube(frontSurface, surfaces - it.surface, newResult)
        subResult.right != null
      }?.rotatedSurface
      if (cube.top == null) return cube
      surfaces -= cube.top
    }

    if (cube.right == null) {
      def rightSurfaces = matchingRotationsOf(surfaces) {
        areConnectible(frontSurface.rightSide(), it.leftSide()) &&
        areConnectible(cube.top.rightSide(), it.topSide()) &&
        [frontSurface.topSide()[2], cube.top.bottomSide()[2], it.leftSide()[0]].count{it == x} == 1
      }
      if (rightSurfaces.empty) return cube
      cube.right = rightSurfaces.find {
        Map newResult = cube.clone() as Map
        newResult.right = it.rotatedSurface
        Map subResult = assembleAsCube(frontSurface, surfaces - it.surface, newResult)
        subResult.bottom != null
      }?.rotatedSurface
      if (cube.right == null) return cube
      surfaces -= cube.right
    }

    if (cube.bottom == null) {
      def bottomSurfaces = matchingRotationsOf(surfaces) {
        areConnectible(frontSurface.bottomSide(), it.topSide()) &&
        areConnectible(cube.right.bottomSide(), it.rightSide()) &&
        [frontSurface.bottomSide()[2], cube.right.leftSide()[2], it.topSide()[2]].count{it == x} == 1
      }
      if (bottomSurfaces.empty) return cube
      cube.bottom = bottomSurfaces.find {
        Map newResult = cube.clone() as Map
        newResult.bottom = it.rotatedSurface
        Map subResult = assembleAsCube(frontSurface, surfaces - it.surface, newResult)
        subResult.left != null
      }?.rotatedSurface
      if (cube.bottom == null) return cube
      surfaces -= cube.bottom
    }

    if (cube.left == null) {
      def leftSurfaces = matchingRotationsOf(surfaces) {
        areConnectible(frontSurface.leftSide(), it.rightSide()) &&
        areConnectible(cube.bottom.leftSide().reverse(), it.bottomSide()) &&
        areConnectible(cube.top.leftSide(), it.topSide()) &&
        [frontSurface.leftSide()[2], cube.bottom.topSide()[0], it.rightSide()[2]].count{it == x} == 1 &&
        [frontSurface.leftSide()[0], cube.top.bottomSide()[0], it.rightSide()[0]].count{it == x} == 1
      }
      if (leftSurfaces.empty) return cube
      cube.left = leftSurfaces.find {
        Map newResult = cube.clone() as Map
        newResult.left = it.rotatedSurface
        Map subResult = assembleAsCube(frontSurface, surfaces - it.surface, newResult)
        subResult.back != null
      }?.rotatedSurface
      if (cube.left == null) return cube
      surfaces -= cube.left
    }


    def rotations = matchingRotationsOf(surfaces) {
      areConnectible(cube.top.topSide(), it.topSide()) &&
              areConnectible(cube.right.rightSide(), it.rightSide()) &&
              areConnectible(cube.bottom.bottomSide(), it.bottomSide()) &&
              areConnectible(cube.left.leftSide(), it.leftSide()) &&
              [cube.top.leftSide()[0], cube.left.topSide()[0], it.topSide()[0]].count { it == x } == 1 &&
              [cube.top.rightSide()[0], cube.right.topSide()[2], it.topSide()[2]].count { it == x } == 1 &&
              [cube.bottom.rightSide()[2], cube.right.bottomSide()[2], it.bottomSide()[2]].count { it == x } == 1 &&
              [cube.bottom.leftSide()[2], cube.left.bottomSide()[0], it.bottomSide()[0]].count { it == x } == 1
    }
    cube.back = rotations.empty ? null : rotations.first().rotatedSurface


    cube
  }

  @Test void "all possible rotations of a surface"() {
    def rotations = rotationsOf(surface(
            [x, x, _],
            [_, x, _],
            [_, x, _],
    ))
    assert rotations.size() == 8
    assert rotations[4] == surface(
            [_, x, x],
            [_, x, _],
            [_, x, _],
    )
    assert rotations[7] == surface(
            [x, _, _],
            [x, x, x],
            [_, _, _],
    )
  }

  private static List<Surface> rotationsOf(Surface surface) {
    def rotate = { Surface aSurface ->
      def result = [aSurface]
      (0..2).each {
        aSurface = aSurface.rotateRight()
        result << aSurface
      }
      result
    }
    rotate(surface) + rotate(surface.horizontalFlip())
  }

  @Test void "determine if surface is valid"() {
    assert !isValid(surface(
          [_, _, _],
          [_, _, _],
          [_, _, _],
    ))
    assert !isValid(surface(
          [x, _, _],
          [_, x, _],
          [_, _, _],
    ))
    assert isValid(surface(
          [x, x, _],
          [_, x, _],
          [_, _, _],
    ))
  }

  @Test void "surfaces can be rotated"() {
    assert surface(
            [x, x, _],
            [_, x, _],
            [_, x, _],
    ).rotateRight() == surface(
            [_, _, x],
            [x, x, x],
            [_, _, _],
    )
    assert surface(
            [_, _, x],
            [x, x, x],
            [_, _, _],
    ).rotateRight() == surface(
            [_, x, _],
            [_, x, _],
            [_, x, x],
    )
  }

  @Test void "surfaces can be horizontally flipped"() {
    assert surface(
            [x, x, _],
            [_, x, _],
            [_, x, _],
    ).horizontalFlip() == surface(
            [_, x, x],
            [_, x, _],
            [_, x, _],
    )
  }

  @Test void "sides of surface"() {
    def surface = surface(
            [x, x, _],
            [_, x, x],
            [_, x, x]
    )
    assert surface.topSide() == [x, x, _]
    assert surface.rightSide() == [_, x, x]
    assert surface.bottomSide() == [_, x, x]
    assert surface.leftSide() == [x, _, _]
  }

  @Test void "sides can be connectible"() {
    assert !areConnectible([_, _, x], [x, x, x])
    assert !areConnectible([_, _, x], [x, _, _])
    assert areConnectible([_, x, x], [_, _, _])
    assert areConnectible([_, _, x], [x, x, _])
  }

  @Test void "all possible valid combinations of surfaces"() {
    def allSurfaces = combinationsOf(surface(
            [_, _, _],
            [_, x, _],
            [_, _, _]
    ))
    allSurfaces.each {
      println("---")
      println(it)
    }
    assert allSurfaces.size() == 160
  }

  private static boolean areConnectible(List side1, List side2) {
    [side1, side2].transpose().every{ it[0] == _ || it[1] == _ } && (side1[1] == x || side2[1] == x)
  }

  private static List<Surface> combinationsOf(Surface surface) {
    positions().collectMany { row, column ->
      if (surface.get(row, column) != x) {
        def updateSurface = surface.copy().set(row, column, x)
        if (updateSurface.valid) {
          [updateSurface] + combinationsOf(updateSurface)
        } else {
          [null]
        }
      } else {
        [null]
      }
    }.findAll{ it != null }.unique()
  }

  private static positions(int startRow = 0, int startColumn = -1) {
    int row = startRow
    int column = startColumn
    new Iterator() {
      @Override Object next() {
        column++
        if (column >= 3) {
          row++
          column = 0
        }
        [row, column]
      }

      @Override boolean hasNext() {
        row != 2 || column != 2
      }
    }
  }


  private static class Surface {
    final List<List> data

    Surface(List<List> data) {
      this.data = data
    }

    Surface(List... rows) {
      this.data = rows.toList()
    }

    def get(int row, int column) {
      data[row][column]
    }

    def set(int row, int column, value) {
      data[row][column] = value
      this
    }

    List topSide() {
      data[0]
    }

    List bottomSide() {
      data[2]
    }

    List rightSide() {
      (0..2).collect{ data[it][2] }
    }

    List leftSide() {
      (0..2).collect{ data[it][0] }
    }

    Surface copy() {
      data.collect{ it.clone() }.toList()
    }

    boolean isValid() {
      if (data[1][1] != x) false
      else if (data[0][0] == x && data[0][1] != x && data[1][0] != x) false
      else if (data[0][2] == x && data[0][1] != x && data[1][2] != x) false
      else if (data[2][2] == x && data[1][2] != x && data[2][1] != x) false
      else if (data[2][0] == x && data[2][1] != x && data[1][0] != x) false
      else true
    }

    Surface rotateRight() {
      def newData =
        (0..2).collect { column ->
          (2..0).collect { row ->
            data[row][column]
          }
        }
      new Surface(newData)
    }

    Surface horizontalFlip() {
      def newData =
        (0..2).collect { row ->
          (2..0).collect { column ->
            data[row][column]
          }
        }
      new Surface(newData)
    }

    @Override String toString() {
      data.collect{it.join("")}.join("\n")
    }

    @Override boolean equals(o) {
      if (this.is(o)) return true
      if (getClass() != o.class) return false

      Surface surface = (Surface) o

      if (data != surface.data) return false

      return true
    }

    @Override int hashCode() {
      return (data != null ? data.hashCode() : 0)
    }
  }

  private static boolean isValid(Surface surface) {
    surface.valid
  }

  private static surface(List... rows) {
    new Surface(rows)
  }
}
