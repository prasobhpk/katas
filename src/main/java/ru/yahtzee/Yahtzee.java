package ru.yahtzee;

class Yahtzee {
    private static final int MAX_DICE_VALUE = 6;

    private final int[] dices;
    private final int[] count;

    public Yahtzee(int... dices) {
        this.dices = dices;

        count = new int[MAX_DICE_VALUE];
        for (int dice : dices) {
            count[dice - 1] += 1;
        }
    }

    public int sumOf(int n) {
        int sum = 0;
        for (int dice : dices) {
            if (dice == n) sum += dice;
        }
        return sum;
    }

    public int pair() {
        int maxSum = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 2) {
                int sum = sumOf(i + 1);
                if (sum > maxSum) maxSum = sum;
            }
        }
        return maxSum;
    }

    public int twoPairs() {
        int sum = 0;
        int amountOfPairs = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 2) {
                sum += sumOf(i + 1);
                amountOfPairs++;
            }
        }
        return amountOfPairs == 2 ? sum : 0;
    }

    public int threeOfAKind() {
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 3) return sumOf(i + 1);
        }
        return 0;
    }

    public int fourOfAKind() {
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 4) return sumOf(i + 1);
        }
        return 0;
    }

    public int smallStraight() {
        for (int i = 0; i < 5; i++) {
            if (count[i] != 1) return 0;
        }
        return 15;
    }

    public int largeStraight() {
        for (int i = 1; i < 6; i++) {
            if (count[i] != 1) return 0;
        }
        return 20;
    }

    public int fullHouse() {
        int pair = pair();
        int threeOfAKind = threeOfAKind();
        if (pair == 0 || threeOfAKind == 0)
            return 0;
        else
            return pair + threeOfAKind;
    }

    public int yahtzee() {
        for (int i = 1; i < count.length; i++) {
            if (count[i] == 5) return 50;
        }
        return 0;
    }

    public int chance() {
        int sum = 0;
        for (int dice : dices) {
            sum += dice;
        }
        return sum;
    }
}
