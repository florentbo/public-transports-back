package be.bonaf.publictransport.adapter.stib;

import be.bonaf.publictransport.adapter.stib.client.WaitingTimes;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
public class Mapper {
  private final Clock clock;

  public Mapper(Clock clock) {
    this.clock = clock;
  }

  List<ArrivalTime> from(WaitingTimes waitingTimes) {
    return waitingTimes.results().stream().flatMap(this::from2).toList();
  }

  private Stream<ArrivalTime> from2(WaitingTimes.Result result) {
    return result.passingTimes().stream().map(this::from);
  }

  private ArrivalTime from(WaitingTimes.Result.PassingTime passingTime) {
    OffsetDateTime now = OffsetDateTime.now(clock);
    log.debug("Current time: {}", now);
    var expectedArrivalTime = passingTime.expectedArrivalTime();
    log.debug("Expected arrival time: {}", expectedArrivalTime);
    int minutesBetween = (int) MINUTES.between(now, expectedArrivalTime);
    return ArrivalTime.builder()
        .destinationName(passingTime.destination().fr())
        .minutesUntilArrival(minutesBetween + 1)
        .build();
  }
}
