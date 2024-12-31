package be.bonaf.publictransport.domain.journey;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Journey(JourneyId id, City city, Location origin, Location destination, List<Trip> trips) {
  public enum TransportType {
    METRO,
    TRAM,
    BUS,
    TRAIN
  }

  public enum City {
    LONDON,
    BRUSSELS
  }

  public record JourneyId(UUID value) {
    public static JourneyId from(String value) {
      return new JourneyId(UUID.fromString(value));
    }
  }

  @Builder
  public record TransportOption(
      TransportType type,
      String line,
      String startStation,
      String direction,
      List<Arrival> arrivals) {

    @Builder
    public record Arrival(int minutesUntilArrival, String platform) {}
  }

  @Builder
  public record Trip(TransportStation startingPoint, String line, String direction) {

    @Builder(builderMethodName = "aTransportStation")
    public record TransportStation(String stationId, String name) {}
  }
}
