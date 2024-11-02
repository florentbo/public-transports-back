package be.bonaf.publictransport.domain.journey;

import java.time.Instant;

public record ArrivalTime(
    String platformName,
    String destinationName,
    Instant estimatedTimeOfArrival,
    Instant scheduledTimeOfArrival,
    int minutesUntilArrival,
    ArrivalStatus status) {
  public enum ArrivalStatus {
    ON_TIME,
    DELAYED,
    CANCELLED
  }
}
