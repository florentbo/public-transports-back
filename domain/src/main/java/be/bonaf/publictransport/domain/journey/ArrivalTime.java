package be.bonaf.publictransport.domain.journey;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ArrivalTime(
    String platformName,
    String destinationName,
    int minutesUntilArrival,
    ArrivalStatus status) {
  public enum ArrivalStatus {
    ON_TIME,
    DELAYED,
    CANCELLED
  }
}
