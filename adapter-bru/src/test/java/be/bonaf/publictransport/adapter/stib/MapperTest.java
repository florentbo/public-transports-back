package be.bonaf.publictransport.adapter.stib;

import be.bonaf.publictransport.adapter.stib.client.WaitingTimes;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static be.bonaf.publictransport.adapter.stib.client.WaitingTimes.Result.PassingTime.*;
import static be.bonaf.publictransport.adapter.stib.client.WaitingTimes.Result.*;
import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

  @Test
  void from() {
    var fixedClock =
        Clock.fixed(
            LocalDateTime.of(2024, 12, 31, 13, 7, 1)
                .atZone(ZoneId.of("Europe/Brussels"))
                .toInstant(),
            ZoneId.of("Europe/Brussels"));
    var mapper = new Mapper(fixedClock);

    var arrivalDeparture = mapper.from(waitingTimes());

    var arrivalTime01 =
        ArrivalTime.builder().destinationName("ELISABETH").minutesUntilArrival(2).build();
    var arrivalTime02 =
        ArrivalTime.builder().destinationName("ELISABETH").minutesUntilArrival(12).build();
    assertThat(arrivalDeparture).isEqualTo(List.of(arrivalTime01, arrivalTime02));
  }

  private WaitingTimes waitingTimes() {
    var passTime =
        aPassingTime()
            .withDestination(new Destination("ELISABETH", "ELISABETH"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:09:00+01:00"))
            .withLineId("6")
            .build();

    var passTime2 =
        aPassingTime()
            .withDestination(new Destination("ELISABETH", "ELISABETH"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:19:00+01:00"))
            .withLineId("6")
            .build();

    var result =
        aResult()
            .withPointId("8784")
            .withLineId("6")
            .withPassingTimes(List.of(passTime, passTime2))
            .build();

    return new WaitingTimes(1, List.of(result));
  }
}
