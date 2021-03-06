module P9x.P91.KnightsTour(
    findKnightTours
) where

findKnightTours :: Int -> [[(Int, Int)]]
findKnightTours 0 = []
findKnightTours 1 = [[(0,0)]]
findKnightTours boardSize =
    (\it -> findKnightTours' boardSize [it]) `concatMap` allPositions
    where allPositions = [(row, column) | row <- [0..boardSize-1], column <- [0..boardSize-1]]


findKnightTours' :: Int -> [(Int, Int)] -> [[(Int, Int)]]
findKnightTours' boardSize moves =
    if (length moves == boardSize * boardSize) then [moves]
    else (\it -> findKnightTours' boardSize (it : moves)) `concatMap` nextValidMoves
    where nextMoves = knightMoves $ head moves
          nextValidMoves = notVisited `filter` (isOnBoard `filter` nextMoves)
          isOnBoard (row, column) = row >= 0 && row < boardSize && column >= 0 && column < boardSize
          notVisited pos = not (elem pos moves)


knightMoves :: (Int, Int) -> [(Int, Int)]
knightMoves position = [
    (row - 1, column - 2),
    (row + 1, column - 2),
    (row + 2, column - 1),
    (row + 2, column + 1),
    (row + 1, column + 2),
    (row - 1, column + 2),
    (row - 2, column + 1),
    (row - 2, column - 1)
 ]
 where row = fst position
       column = snd position