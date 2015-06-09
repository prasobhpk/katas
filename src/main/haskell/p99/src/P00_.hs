module P00_ (
    myLast', myLast'', myLast''', myLast'''', myLast''''',
    myButLast, myButLast', myButLast'', myButLast''', myButLast'''', lastbut1, lastbut1safe,
    elementAt, elementAt', elementAt'', elementAt''', elementAt_w'pf,
    myLength, myLength1', myLength2', myLength3', myLength4', myLength5', myLength6', myLength1'', myLength2'', myLength3'',
    reverse', reverse'', reverse''', reverse'''',
    isPalindrome, isPalindrome'1, isPalindrome'2, isPalindrome'3, isPalindrome'4, isPalindrome'5, isPalindrome'6, isPalindrome'7,
    NestedList(..), nestedList, flatten, flatten', flatten'2, flatten'3, flatten'4, flatten'5, flatten'6,
    compress, compress', compress'2, compress'3, compress'4, compress'5, compress'6, compress'7,
    pack, pack', pack'2, pack'3, pack'4, pack'5,
    encode, encode', encode'2, encode'3, encode'4, encode'5, encode'6, encode'7,
    ListItem(..), encodeModified, encodeModified',
    decode, decode', decode'2,
    decodeModified, decodeModified', decodeModified'2, decodeModified'3
) where

import Data.Foldable(Foldable, foldMap)
import Control.Monad(liftM2)
import Control.Applicative((<*>), (<$>))
import Control.Arrow((&&&))
import Data.List(group, findIndex)
import GHC.Exts(build)


-- solutions from https://wiki.haskell.org/99_questions

-- P01
myLast' = foldr1 (const id)
myLast'' = foldr1 (flip const)
myLast''' = head . reverse
myLast'''' = foldl1 (curry snd)
myLast''''' [] = error "No end for empty lists!"
myLast''''' x = x !! (length x - 1)


-- P02
myButLast :: [a] -> a
myButLast = last . init
myButLast' x = reverse x !! 1
myButLast'' [x,_]  = x
myButLast'' (_:xs) = myButLast'' xs
myButLast''' (x:(_:[])) = x
myButLast''' (_:xs) = myButLast''' xs
myButLast'''' = head . tail . reverse
--lastbut1 :: Foldable f => f a -> a
lastbut1 = fst . foldl (\(_,b) x -> (b,x)) (err1,err2)
  where
    err1 = error "lastbut1: Empty list"
    err2 = error "lastbut1: Singleton"
--lastbut1safe :: Foldable f => f a -> Maybe a
lastbut1safe = fst . foldl (\(_,b) x -> (b,Just x)) (Nothing,Nothing)


-- P03
elementAt :: Int -> [a] -> a
elementAt i list = list !! i

elementAt' :: Int -> [a] -> a
elementAt' 0 (x:_)  = x
elementAt' i (_:xs) = elementAt' (i - 1) xs
elementAt' _ _      = error "Index out of bounds"

elementAt'' n xs
        | length xs < n = error "Index out of bounds"
        | otherwise = fst . last $ zip xs [0..n]

elementAt''' n xs = head $ foldr ($) xs $ replicate n tail

elementAt_w'pf = (last .) . take . (+ 1)


-- P04
myLength :: [a] -> Int
myLength list = myLength_acc list 0
	where
		myLength_acc [] n = n
		myLength_acc (_:xs) n = myLength_acc xs (n + 1)

myLength1' = foldl (\n _ -> n + 1) 0    :: [a] -> Int
myLength2' = foldr (\_ n -> n + 1) 0    :: [a] -> Int
myLength3' = foldr (\_ -> (+1)) 0       :: [a] -> Int
myLength4' = foldr ((+) . (const 1)) 0  :: [a] -> Int
myLength5' = foldr (const (+1)) 0       :: [a] -> Int
myLength6' = foldl (const . (+1)) 0     :: [a] -> Int

myLength1'' xs = snd $ last $ zip xs [1..]  -- Just for fun
myLength2'' = snd . last . (flip zip [1..]) :: [a] -> Int -- Because point-free is also fun
myLength3'' = fst . last . zip [1..]        :: [a] -> Int -- same, but easier


-- P05
reverse' :: [a] -> [a]
reverse' = foldl (flip (:)) []

reverse'' :: [a] -> [a]
reverse'' [] = []
reverse'' (x:xs) = reverse'' xs ++ [x]

reverse''' :: [a] -> [a]
reverse''' list = reverse_ list []
  where
    reverse_ [] reversed     = reversed
    reverse_ (x:xs) reversed = reverse_ xs (x:reversed)

reverse'''' :: [a] -> [a]
reverse'''' xs = foldr (\x fId empty -> fId (x : empty)) id xs []


-- P06
isPalindrome :: (Eq a) => [a] -> Bool
isPalindrome xs = xs == (reverse xs)

isPalindrome'1 :: (Eq a) => [a] -> Bool
isPalindrome'1 []  = True
isPalindrome'1 [_] = True
isPalindrome'1 xs  = (head xs) == (last xs) && (isPalindrome'1 $ init $ tail xs)

isPalindrome'2 :: (Eq a) => [a] -> Bool -- this seems to be just more vebose version of isPalindrome
isPalindrome'2 xs = foldl (\acc (a,b) -> if a == b then acc else False) True input
	where input = zip xs (reverse xs)

isPalindrome'3 :: (Eq a) => [a] -> Bool
isPalindrome'3 = Control.Monad.liftM2 (==) id reverse

isPalindrome'4 :: (Eq a) => [a] -> Bool
isPalindrome'4 = (==) Control.Applicative.<*> reverse

isPalindrome'5 :: (Eq a) => [a] -> Bool
isPalindrome'5 xs = p [] xs xs
   where p rev (x:xs) (_:_:ys) = p (x:rev) xs ys
         p rev (_:xs) [_] = rev == xs
         p rev xs [] = rev == xs

isPalindrome'6 :: (Eq a) => [a] -> Bool
isPalindrome'6 xs = and $ zipWith (==) xs (reverse xs)

isPalindrome'7 :: (Eq a) => [a] -> Bool
isPalindrome'7 xs = (uncurry (==) . (id &&& reverse)) xs

-- P07
data NestedList a = Elem a | List[NestedList a] deriving (Show, Eq)
nestedList :: [a] -> NestedList a
nestedList xs = List $ (\it -> Elem it) `map` xs

flatten :: NestedList a -> [a]
flatten (Elem x) = [x]
flatten (List x) = concatMap flatten x

flatten' :: NestedList a -> [a]
flatten' (Elem a)      = [a]
flatten' (List (x:xs)) = flatten' x ++ flatten' (List xs)
flatten' (List [])     = []

flatten'2 :: NestedList a -> [a]
flatten'2 (Elem x) = return x
flatten'2 (List x) = flatten'2 =<< x

flatten'3 :: NestedList a -> [a]
flatten'3 (Elem x) = [x]
flatten'3 (List x) = foldMap flatten'3 x

flatten'4 :: NestedList a -> [a]
flatten'4 a = flt' a []
  where flt' (Elem x)      xs = x:xs
        flt' (List (x:ls)) xs = flt' x (flt' (List ls) xs)
        flt' (List [])     xs = xs

flatten'5 :: NestedList a -> [a] -- same as version with concatMap above
flatten'5 (Elem x ) = [x]
flatten'5 (List xs) =  foldr (++) [] $ map flatten'5 xs

flatten'6 :: NestedList a -> [a]
flatten'6 = reverse . rec []
  where rec acc (List []) = acc
        rec acc (Elem x)  = x:acc
        rec acc (List (x:xs)) = rec (rec acc x) (List xs)


-- P08
compress :: Eq a => [a] -> [a]
compress = map head . group

compress' :: Eq a => [a] -> [a]
compress' (x:ys@(y:_))
    | x == y    = compress' ys
    | otherwise = x : compress' ys
compress' ys = ys
compress'2 xs = (foldr f (const []) xs) Nothing
  where
    f x r a@(Just q) | x == q = r a
    f x r _ = x : r (Just x)
compress'3 :: Eq a => [a] -> [a]
compress'3 = foldr skipDups []
    where skipDups x [] = [x]
          skipDups x acc
                | x == head acc = acc
                | otherwise = x : acc
compress'4 []     = []
compress'4 (x:xs) = x : (compress'4 $ dropWhile (== x) xs)
compress'5 xs = foldr (\a b -> if a == (head b) then b else a:b) [last xs] xs
compress'6 x = foldl (\a b -> if (last a) == b then a else a ++ [b]) [head x] x
compress'7 x = reverse $ foldl (\a b -> if (head a) == b then a else b:a) [head x] x


-- P09
pack :: Eq a => [a] -> [[a]]
pack [] = []
pack (x:xs) = let (first,rest) = span (==x) xs
               in (x:first) : pack rest

pack' :: Eq a => [a] -> [[a]]
pack' [] = []
pack' (x:xs) = (x:first) : pack' rest
         where
           getReps [] = ([], [])
           getReps (y:ys)
                   | y == x = let (f,r) = getReps ys in (y:f, r)
                   | otherwise = ([], (y:ys))
           (first, rest) = getReps xs

pack'2 :: Eq a => [a] -> [[a]]
pack'2 [] = []
pack'2 (x:xs) = (x:reps) : (pack'2 rest)
    where
        (reps, rest) = maybe (xs,[]) (\i -> splitAt i xs)
                             (findIndex (/=x) xs)

pack'3 :: (Eq a) => [a] -> [[a]]
pack'3 [] = []
pack'3 (x:xs) = (x : takeWhile (==x) xs) : pack (dropWhile (==x) xs)

pack'4 :: (Eq a) => [a] -> [[a]]
pack'4 [] = []
pack'4 [x] = [[x]]
pack'4 (x:xs) = if x `elem` (head (pack'4 xs))
                then (x:(head (pack'4 xs))):(tail (pack'4 xs))
                else [x]:(pack'4 xs)

pack'5 :: (Eq a) => [a] -> [[a]]
pack'5 [] = []
pack'5 (y:ys) = reverse $ impl ys [[y]]
	where
		impl [] packed = packed
		impl (x:xs) p@(z:zs)
			| x == (head z) = impl xs ((x:z):zs)
			| otherwise     = impl xs ([x]:p)

-- P10
encode :: Eq a => [a] -> [(Int, a)]
encode xs = map (\x -> (length x, head x)) (group xs)

encode' :: Eq a => [a] -> [(Int, a)]
encode' = map (\x -> (length x, head x)) . group

encode'2 :: Eq a => [a] -> [(Int, a)]
encode'2 xs = map (length &&& head) $ group xs

encode'3 :: Eq a => [a] -> [(Int, a)]
encode'3 = map ((,) <$> length <*> head) . pack

encode'4 :: Eq a => [a] -> [(Int, a)]
encode'4 xs = (enc . pack) xs
	where enc = foldr (\x acc -> (length x, head x) : acc) []

encode'5 :: Eq a => [a] -> [(Int, a)]
encode'5 [] = []
encode'5 (x:xs) = (length $ x : takeWhile (==x) xs, x)
                    : encode'5 (dropWhile (==x) xs)

encode'6 :: Eq a => [a] -> [(Int, a)]
encode'6 []     = []
encode'6 (x:xs) = encode'' 1 x xs
        where encode'' n x [] = [(n, x)]
              encode'' n x (y:ys)
                | x == y    = encode'' (n + 1) x ys
                | otherwise = (n, x) : encode'' 1 y ys

encode'7 :: Eq a => [a] -> [(Int, a)]
encode'7 xs = zip (map length l) h
        where l = group xs
              h = map head l

-- P11
data ListItem a = Single a | Multiple Int a deriving (Show, Eq)

encodeModified :: Eq a => [a] -> [ListItem a]
encodeModified = map encodeHelper . encode
    where
      encodeHelper (1,x) = Single x
      encodeHelper (n,x) = Multiple n x

encodeModified' :: Eq a => [a] -> [ListItem a]
encodeModified' xs = [y | x <- group xs,
    let y = if (length x) == 1 then
                Single (head x) else
                Multiple (length x) (head x)]

-- P12
decodeModified :: [ListItem a] -> [a]
decodeModified = concatMap decodeHelper
    where
      decodeHelper (Single x)     = [x]
      decodeHelper (Multiple n x) = replicate n x

decode :: Eq a => [(Int, a)] -> [a]
decode = concatMap (uncurry replicate)

toTuple :: ListItem a -> (Int, a)
toTuple (Single x)     = (1, x)
toTuple (Multiple n x) = (n, x)

decodeModified' :: [ListItem a] -> [a]
decodeModified' = concatMap (uncurry replicate . toTuple)

decodeModified'2 :: [ListItem a]-> [a]
decodeModified'2 = foldl (\x y -> x ++ decodeHelper y) []
    where decodeHelper :: ListItem a -> [a]
          decodeHelper (Single x)     = [x]
          decodeHelper (Multiple n x) = replicate n x

decodeModified'3 :: [ListItem a] -> [a]
decodeModified'3 = (\acc e ->
        case e of
            Single x -> acc ++ [x]
            Multiple n x -> acc ++ replicate n x
    ) `foldl` []

decode' :: Eq a => [(Int,a)] -> [a]
decode' xs = foldr f [] xs
    where f (1, x) r = x : r
          f (k, x) r = x : f (k-1, x) r

{-# INLINE decode #-}
decode'2 :: Eq a => [(Int,a)] -> [a]
decode'2 xs = build (\c n ->
  let
    f (1, x) r = x `c` r
    f (k, x) r = x `c` f (k-1, x) r
  in
    foldr f n xs)
