package be.bonaf.publictransport.adapter.stib;

import be.bonaf.publictransport.adapter.stib.client.WaitingTimes;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
public class Mapper {
  private final Clock clock;

  public Mapper(Clock clock) {
    this.clock = clock;
  }

  public List<ArrivalTime> from(WaitingTimes waitingTimes) {
    return waitingTimes.results().stream().flatMap(this::passingTimes).toList();
  }

  public Map<String, List<ArrivalTime>> fromMultipleLines(WaitingTimes waitingTimes) {
    Stream<ArrivalTimes> arrivalTimesStream = waitingTimes.results().stream().map(this::from2);
    return arrivalTimesStream.collect(
        Collectors.toMap(ArrivalTimes::lineId, ArrivalTimes::arrivalTimes));
  }

  private ArrivalTimes from2(WaitingTimes.Result result) {
    String lineId = result.lineId();
    List<ArrivalTime> arrivalTimes = result.passingTimes().stream().map(this::from).toList();
    return new ArrivalTimes(lineId, arrivalTimes);
  }

  record ArrivalTimes(String lineId, List<ArrivalTime> arrivalTimes) {}

  private Stream<ArrivalTime> passingTimes(WaitingTimes.Result result) {
    return result.passingTimes().stream().map(this::from);
  }

  private ArrivalTime from(WaitingTimes.Result.PassingTime passingTime) {
    OffsetDateTime now = OffsetDateTime.now(clock);
    log.debug("Current time: {}", now);
    var expectedArrivalTime = passingTime.expectedArrivalTime();
    log.debug("Expected arrival time: {}", expectedArrivalTime);
    int minutesBetween = (int) MINUTES.between(now, expectedArrivalTime);
    return ArrivalTime.builder()
        .direction(passingTime.destination().fr())
        .minutesUntilArrival(minutesBetween + 1)
        .build();
  }
}
