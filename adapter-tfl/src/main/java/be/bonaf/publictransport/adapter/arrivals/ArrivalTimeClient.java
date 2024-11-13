package be.bonaf.publictransport.adapter.arrivals;

import be.bonaf.publictransport.adapter.tfl.Departure;
import be.bonaf.publictransport.adapter.tfl.TflClient;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import be.bonaf.publictransport.domain.journey.ArrivalTimeService;
import be.bonaf.publictransport.domain.journey.Journey;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ArrivalTimeClient implements ArrivalTimeService {
  private final TflClient tflClient;

  @Override
  public List<ArrivalTime> arrivalTimes(Journey journey) {
    return journey.trips().stream()
        .flatMap(
            trip ->
                tflClient
                    .departures(
                        trip.startingPoint().stationId(),
                        trip.direction(),
                        trip.startingPoint().line())
                    .stream())
        .map(DepartureToArrivalTimeMapper::from)
        .toList();
  }

  static class DepartureToArrivalTimeMapper {
    public static ArrivalTime from(Departure departure) {
      return ArrivalTime.builder()
          .destinationName(departure.destinationName())
          .minutesUntilArrival(departure.minutesAndSecondsToArrival().getMinute())
          .build();
    }
  }
}
