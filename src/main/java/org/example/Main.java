package org.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * Solution for the advent of code 2023 6
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


        System.out.println("PART 2 - Total number of possibilities to win: " + calculateMarginOfError(new RaceResult(44707080L,
                283113411341491L)));
//                         10022122663200
//                         1252765332900
    }

    private static BigDecimal calculateMarginOfError(final RaceResult raceResult) {
        final BigDecimal minimumHoldTime = calculateMinButtonHoldTime(new BigDecimal(raceResult.getDuration()), new BigDecimal(raceResult.getWinDistance()));

        // The valid values are those between
        return new BigDecimal(raceResult.getDuration()).subtract(minimumHoldTime.subtract(ONE).multiply(TWO)).subtract(ONE);
    }

    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal MINUS_ONE = new BigDecimal(-1);
    private static BigDecimal calculateMinButtonHoldTime(final BigDecimal raceDuration, final BigDecimal winDistance) {
        BigDecimal currentTestValue = raceDuration.divide(TWO, RoundingMode.CEILING);
        BigDecimal currentMaxValue = raceDuration;
        BigDecimal currentMinValue = ZERO;
        int isMinimumButtonHoldTime = 1;
        while (isMinimumButtonHoldTime != 0) {
            System.out.println("Testing " + currentTestValue);
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
//        System.out.println("intermediary result " + currentTestValue);
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