package be.bonaf.publictransport.adapter.stib;

import be.bonaf.publictransport.adapter.stib.client.WaitingTimes;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Map;

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
        ArrivalTime.builder().destinationName("FIRST STATION").minutesUntilArrival(2).build();
    var arrivalTime02 =
        ArrivalTime.builder().destinationName("FIRST STATION").minutesUntilArrival(12).build();
    assertThat(arrivalDeparture).isEqualTo(List.of(arrivalTime01, arrivalTime02));
  }

  private WaitingTimes waitingTimes() {
    var passTime =
        aPassingTime()
            .withDestination(new Destination("FIRST STATION", "FIRST STATION"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:09:00+01:00"))
            .withLineId("6")
            .build();

    var passTime2 =
        aPassingTime()
            .withDestination(new Destination("FIRST STATION", "FIRST STATION"))
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

  @Test
  void fromWithMultipleLines() {
    var fixedClock =
        Clock.fixed(
            LocalDateTime.of(2024, 12, 31, 13, 7, 1)
                .atZone(ZoneId.of("Europe/Brussels"))
                .toInstant(),
            ZoneId.of("Europe/Brussels"));
    var mapper = new Mapper(fixedClock);

    var arrivalTime01 =
        ArrivalTime.builder().destinationName("FIRST STATION").minutesUntilArrival(2).build();
    var arrivalTime02 =
        ArrivalTime.builder().destinationName("FIRST STATION").minutesUntilArrival(12).build();
    var arrivalTime03 =
        ArrivalTime.builder().destinationName("SECOND STATION").minutesUntilArrival(22).build();
    var arrivalTime04 =
        ArrivalTime.builder().destinationName("SECOND STATION").minutesUntilArrival(32).build();
    Map<String, List<ArrivalTime>> expected =
        Map.of(
            "6", List.of(arrivalTime01, arrivalTime02),
            "51", List.of(arrivalTime03, arrivalTime04));

    assertThat(mapper.fromMultipleLines(waitingTimesWithMultipleLines())).isEqualTo(expected);
  }

  private WaitingTimes waitingTimesWithMultipleLines() {

    var passTime =
        aPassingTime()
            .withDestination(new Destination("FIRST STATION", "FIRST STATION"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:09:00+01:00"))
            .withLineId("6")
            .build();

    var passTime2 =
        aPassingTime()
            .withDestination(new Destination("FIRST STATION", "FIRST STATION"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:19:00+01:00"))
            .withLineId("6")
            .build();
    var passTime3 =
        aPassingTime()
            .withDestination(new Destination("SECOND STATION", "SECOND STATION"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:29:00+01:00"))
            .withLineId("51")
            .build();

    var passTime4 =
        aPassingTime()
            .withDestination(new Destination("SECOND STATION", "SECOND STATION"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-31T13:39:00+01:00"))
            .withLineId("51")
            .build();

    var result =
        aResult()
            .withPointId("8784")
            .withLineId("6")
            .withPassingTimes(List.of(passTime, passTime2))
            .build();

    var result2 =
        aResult()
            .withPointId("5077")
            .withLineId("51")
            .withPassingTimes(List.of(passTime3, passTime4))
            .build();
    return new WaitingTimes(2, List.of(result, result2));
  }
}
