package com.github.arthurschiavom.adventofcode2023.day6;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * Solution for the advent of code 2023 day 6.
 * Explanation: The challenge is finding, in an efficient manner, the interval of milliseconds for which we can hold down the button to win.
 * To do this, I realized that:
 * 1. To have the boat go the farthest, hold the button down for half the time.
 * 2. There as many numbers that would not win that are **bigger** than the range of winning values,
 * as there are numbers that would not win that are **smaller** than the range of winning values
 * For example, for a race of duration of 7ms and win of 9mm, we can hold the button for 0, 1, 2, 3, 4, 5, 6, 7 milliseconds.
 * The first number that can win the race is 2, which means that there are two possibilities before 2 that cannot win the race: 0 and 1
 * Since the number of numbers that can't win the race before and after the acceptable range is the same,
 * we can conclude that the number of values that can't win the race is 2 (before winning range) + 2 (after winning range) = 4
 * 3. Knowing this, what we can do is determine the first winning number then calculate the total quantity of winning numbers as
 * note: the "+ 1" is used to include the first number, 0
 * (race duration + 1) - (first winning number * 2)
 * 4. To complement to this, in order to find the first winnable number it is used binary search.
 */
public class Main {
    public static void main(String[] args) {
//        Time:        44     70     70     80
//        Distance:   283   1134   1134   1491
        final List<RaceResult> raceResults = List.of(
                new RaceResult(44, 283),
                new RaceResult(70, 1134),
                new RaceResult(70, 1134),
                new RaceResult(80, 1491)
        );

        final List<BigDecimal> marginOfError = new ArrayList<>();
        for (final RaceResult raceResult : raceResults) {
            marginOfError.add(calculateMarginOfError(raceResult));
        }

        final BigDecimal totalPossibilities = marginOfError.stream().reduce(ONE, (result, nextMargin) -> result.multiply(nextMargin));
        System.out.println("PART 1 - Total number of possibilities to win: " + totalPossibilities);
        System.out.println("PART 2 - Total number of possibilities to win: " + calculateMarginOfError(new RaceResult(44707080L, 283113411341491L)));
    }

    private static BigDecimal calculateMarginOfError(final RaceResult raceResult) {
        final BigDecimal minimumHoldTime = calculateMinButtonHoldTime(new BigDecimal(raceResult.getDuration()), new BigDecimal(raceResult.getWinDistance()));

        // The valid values are those between
        return new BigDecimal(raceResult.getDuration() + 1).subtract(minimumHoldTime.multiply(TWO));
    }

    private static final BigDecimal TWO = new BigDecimal(2);
    private static BigDecimal calculateMinButtonHoldTime(final BigDecimal raceDuration, final BigDecimal winDistance) {
        BigDecimal currentTestValue = raceDuration.divide(TWO, RoundingMode.CEILING);
        BigDecimal currentMaxValue = raceDuration;
        BigDecimal currentMinValue = ZERO;
        int isMinimumButtonHoldTime = 1;
        while (isMinimumButtonHoldTime != 0) {
            if (isMinimumButtonHoldTime == -1) {
                currentMinValue = currentTestValue;
                currentTestValue = currentTestValue.add(currentMaxValue.subtract(currentTestValue).divide(TWO, RoundingMode.CEILING));
            }
            else if (isMinimumButtonHoldTime == 1) {
                currentMaxValue = currentTestValue;
                currentTestValue = currentTestValue.subtract(currentTestValue.subtract(currentMinValue).divide(TWO, RoundingMode.CEILING));
            }
            else {
                break;
            }
            isMinimumButtonHoldTime = isMinimumButtonHoldTime(raceDuration, currentTestValue, winDistance);
        }
        return currentTestValue;
    }

    private static int isMinimumButtonHoldTime(final BigDecimal raceDuration, final BigDecimal buttonHoldTime, final BigDecimal winDistance) {
        final BigDecimal finalDistance = distanceTraveled(raceDuration, buttonHoldTime);
        if (finalDistance.compareTo(winDistance) <= 0) {
            return -1;
        }

        final BigDecimal previousValueDistance = distanceTraveled(raceDuration, buttonHoldTime.subtract(ONE));
        if (previousValueDistance.compareTo(winDistance) > 0) {
            return 1;
        }
        return 0;
    }

    private static BigDecimal distanceTraveled(BigDecimal raceDuration, BigDecimal buttonHoldTime) {
        return buttonHoldTime.multiply(raceDuration.subtract(buttonHoldTime));
    }
}