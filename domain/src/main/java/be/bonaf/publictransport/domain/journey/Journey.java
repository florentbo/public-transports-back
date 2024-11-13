package be.bonaf.publictransport.domain.journey;

import be.bonaf.publictransport.domain.user.UserId;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Journey(
    JourneyId id, UserId userId, Location origin, Location destination, List<Trip> trips) {
  public record JourneyId(UUID value) {}

  @Builder
  public record Arrival(
          int time,       // Minutes until arrival
          String platform
  ) {}

  public enum TransportType {
    METRO, TRAM, BUS, TRAIN
  }

  @Builder
  public record TransportOption(
          TransportType type,
          String line,
          String startStation,
          String direction,
          List<Arrival> arrivals
  ) {}

  @Builder
  public record Trip(TransportStation startingPoint, String direction) {

    @Builder(builderMethodName = "aTransportStation")
    public record TransportStation(String stationId, String name, String line) {}
  }

}
