package be.bonaf.publictransport.domain.journey;

import be.bonaf.publictransport.domain.user.UserId;

import java.time.Instant;
import java.util.List;

public record Journey(
    JourneyId id,
    UserId userId,
    Location origin,
    Location destination,
    List<Trip> trips,
    Instant createdAt) {
  public record JourneyId(String value) {}

  public record Trip(
      TripId id,
      TransportStation startingPoint,
      String direction,
      TripStatus status,
      Instant createdAt) {

    public record TripId(String value) {}

    public record TransportStation(
        String stationId, // This could be the NaptanId
        String name,
        List<String> lines) {}

    public enum TripStatus {
      ACTIVE,
      INACTIVE
    }
  }
}
