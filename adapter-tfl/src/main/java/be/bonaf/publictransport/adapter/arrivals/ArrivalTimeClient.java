package be.bonaf.publictransport.adapter.arrivals;

import static java.util.stream.Collectors.*;

import be.bonaf.publictransport.adapter.tfl.Arrival;
import be.bonaf.publictransport.adapter.tfl.TflClient;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import be.bonaf.publictransport.domain.journey.ArrivalTimeService;
import be.bonaf.publictransport.domain.journey.Journey;
import be.bonaf.publictransport.domain.schedule.Departure;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
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
    List<Departure> departures =
        tflClient.departures(trip.startingPoint().stationId(), trip.direction(), trip.line());
    log.info("Found {} departures for trip {}", departures, trip);
    return departures;
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
          .direction(departure.destinationName())
          .minutesUntilArrival(departure.minutesAndSecondsToArrival().getMinute())
          .build();
    }
  }

  @Override
  public Map<String, List<ArrivalTime>> arrivalTimesPerLine(Journey journey) {
    return journey.trips().stream().collect(toMap(Journey.Trip::line, this::toArrivalTimes));
  }

  private List<ArrivalTime> toArrivalTimes(Journey.Trip trip) {
    log.info("Getting arrival times for trip {}", trip);
    String stationId = trip.startingPoint().stationId();
    Map<String, List<Arrival>> arrivalsPerLine = this.tflClient.arrivals(stationId);
    List<Arrival> arrivals = arrivalsPerLine.get(trip.line());
    return arrivals.stream()
        .map(this::toArrivalTime)
        .filter(arrival -> arrival.direction().equals(trip.direction()))
        .sorted(Comparator.comparing(ArrivalTime::minutesUntilArrival))
        .limit(3)
        .toList();
  }

  private ArrivalTime toArrivalTime(Arrival arrival) {
    return ArrivalTime.builder()
        .direction(arrival.direction())
        .minutesUntilArrival(secondsToMinutes(arrival.secondsToStation()))
        .build();
  }

  static int secondsToMinutes(int seconds) {
    return seconds / 60;
  }
}
