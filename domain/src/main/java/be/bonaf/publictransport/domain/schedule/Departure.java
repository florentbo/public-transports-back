package be.bonaf.publictransport.domain.schedule;

import lombok.Builder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.format.ResolverStyle;

@Builder
public record Departure(
    String platformName,
    String stationName,
    String stationId,
    String destinationName,
    String destinationId,
    LocalTime minutesAndSecondsToArrival) {

  public static LocalTime minutesAndSecondsToDeparture(String minutesAndSeconds) {
    DateTimeFormatter formatter =
        new DateTimeFormatterBuilder()
            .appendPattern("m:s")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    return getParse(minutesAndSeconds, formatter);
  }

  private static LocalTime getParse(String minutesAndSeconds, DateTimeFormatter formatter) {
    try {
      return LocalTime.parse(minutesAndSeconds, formatter);
    } catch (Exception e) {
      return LocalTime.of(0, 59, 59);
    }
  }
}
