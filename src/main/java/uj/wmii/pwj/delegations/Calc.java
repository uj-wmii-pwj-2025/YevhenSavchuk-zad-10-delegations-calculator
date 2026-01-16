package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) throws IllegalArgumentException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");

        ZonedDateTime startTime = ZonedDateTime.parse(start, formatter);
        ZonedDateTime endTime = ZonedDateTime.parse(end, formatter);

        if (!startTime.isBefore(endTime))
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        Duration duration = Duration.between(startTime, endTime);
        long totalMinutes = duration.toMinutes();
        long fullDays = totalMinutes / 1440;
        long extraMinutes = totalMinutes % 1440;

        BigDecimal result = dailyRate.multiply(BigDecimal.valueOf(fullDays));

        if (extraMinutes > 12*60)
            result = result.add(dailyRate);
        else if (extraMinutes > 8*60)
            result = result.add(dailyRate.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP));
        else if (extraMinutes > 0)
            result = result.add(dailyRate.divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP));

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
