import Test.HUnit
import P50

expectEqual :: (Eq a, Show a) => a -> a -> Test
expectEqual expected actual = TestCase (assertEqual "" expected actual)

testList :: String -> [Test] -> Test
testList description tests = TestLabel description (TestList tests)


xNode = node 'x'
xLeaf = leafNode 'x'


p55 = testList "P55" [
    expectEqual 3 (sizeOf $ xNode xLeaf xLeaf),
    expectEqual True (isBalanced $ xNode xLeaf xLeaf),
    expectEqual False (isBalanced $ xNode (xNode xLeaf End) End),
    expectEqual [
            xNode
                (xNode xLeaf End)
                xLeaf,
            xNode
                (xNode End xLeaf)
                xLeaf,
            xNode
                xLeaf
                (xNode xLeaf End),
            xNode
                xLeaf
                (xNode End xLeaf)
        ]
        (cBalanced 4 'x'),
    expectEqual True ((\it -> isBalanced it) `all` (cBalanced 5 'x'))
 ]

p56 = testList "P56" [
    expectEqual True (isSymmetric End),
    expectEqual True (isSymmetric xLeaf),
    expectEqual True (isSymmetric (xNode (xNode xLeaf End) (xNode End xLeaf))),
    expectEqual False (isSymmetric (xNode (xNode xLeaf End) (xNode xLeaf End)))
 ]

p57 = testList "P57" [
    expectEqual (Node 'x' (Node 'a' End End) End) (addValue xLeaf 'a'),
    expectEqual
        (Node 3 (Node 2 (Node 1 End End) End) (Node 5 End (Node 7 End End)))
        (fromList [3, 2, 5, 7, 1]),
    expectEqual True (isSymmetric $ fromList [5, 3, 18, 1, 4, 12, 21]),
    expectEqual False (isSymmetric $ fromList [3, 2, 5, 7, 4])
 ]


main :: IO Counts
main = runTestTT $ TestList [ p55, p56, p57 ]


