package be.bonaf.publictransport.adapter.arrivals;

import be.bonaf.publictransport.adapter.tfl.Departure;
import be.bonaf.publictransport.adapter.tfl.TflClient;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import be.bonaf.publictransport.domain.journey.ArrivalTimeService;
import be.bonaf.publictransport.domain.journey.Journey;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
public class ArrivalTimeClient implements ArrivalTimeService {
  private final TflClient tflClient;

  @Override
  public List<ArrivalTime> arrivalTimes(Journey journey) {
    return journey.trips().stream()
        .flatMap(trip -> departures(trip).stream().filter(startAndArrivalAreTheSame(trip)))
        .map(DepartureToArrivalTimeMapper::from)
        .sorted(Comparator.comparing(ArrivalTime::minutesUntilArrival))
        .limit(2)
        .toList();
  }

  private List<Departure> departures(Journey.Trip trip) {
    return tflClient.departures(
        trip.startingPoint().stationId(), trip.direction(), trip.startingPoint().line());
  }

  private Predicate<Departure> startAndArrivalAreTheSame(Journey.Trip trip) {
    return departure ->
        departure.stationId().equals(trip.startingPoint().stationId())
            && departure.destinationId().equals(trip.direction());
  }

  static class DepartureToArrivalTimeMapper {
    public static ArrivalTime from(Departure departure) {
      return ArrivalTime.builder()
          .platformName(departure.platformName())
          .destinationName(departure.destinationName())
          .minutesUntilArrival(departure.minutesAndSecondsToArrival().getMinute())
          .build();
    }
  }
}
