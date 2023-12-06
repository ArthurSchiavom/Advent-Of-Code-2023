package org.example;

import java.util.ArrayList;
import java.util.List;

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

        final List<Long> marginOfError = new ArrayList<>();
        for (final RaceResult raceResult : raceResults) {
            marginOfError.add(calculateMarginOfError(raceResult));
        }

        final long totalPossibilities = marginOfError.stream().reduce(1L, (result, nextMargin) -> result * nextMargin);
        System.out.println("PART 1 - Total number of possibilities to win: " + totalPossibilities);


        System.out.println("PART 2 - Total number of possibilities to win: " + calculateMarginOfError(new RaceResult(4477080L, 283113411341491L)));
    }

    private static Long calculateMarginOfError(final RaceResult raceResult) {
        final long minimumHoldTime = calculateMinButtonHoldTime(raceResult.getDuration(), raceResult.getWinDistance());

        // The valid values are those between
        return raceResult.getDuration() - ((minimumHoldTime - 1) * 2) - 1;
    }

    private static long calculateMinButtonHoldTime(final long raceDuration, final long winDistance) {
        long currentTestValue = (long)Math.ceil(raceDuration / 2.0D);
        long currentMaxValue = raceDuration;
        long currentMinValue = 0;
        long isMinimumButtonHoldTime = 1;
        while (isMinimumButtonHoldTime != 0) {
            if (isMinimumButtonHoldTime == -1) {
                currentMinValue = currentTestValue;
                currentTestValue = currentTestValue + (int)(Math.ceil((currentMaxValue - currentTestValue) / 2.0D));
            }
            else if (isMinimumButtonHoldTime == 1) {
                currentMaxValue = currentTestValue;
                currentTestValue = currentTestValue - (int)(Math.ceil((currentTestValue - currentMinValue) / 2.0D));
            }
            else {
                break;
            }
            isMinimumButtonHoldTime = isMinimumButtonHoldTime(raceDuration, currentTestValue, winDistance);
        }
        System.out.println("intermediary result " + currentTestValue);
        return currentTestValue;
    }

    private static long isMinimumButtonHoldTime(final long raceDuration, final long buttonHoldTime, final long winDistance) {
        final long finalDistance = distanceTraveled(raceDuration, buttonHoldTime);
        if (finalDistance <= winDistance) {
            return -1;
        }

        final long previousValueDistance = distanceTraveled(raceDuration, buttonHoldTime - 1);
        if (previousValueDistance > winDistance) {
            return 1;
        }
        return 0;
    }

    private static long distanceTraveled(long raceDuration, long buttonHoldTime) {
        return buttonHoldTime * (raceDuration - buttonHoldTime);
    }


}