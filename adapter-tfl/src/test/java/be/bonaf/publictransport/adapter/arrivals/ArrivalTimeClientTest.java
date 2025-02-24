package be.bonaf.publictransport.adapter.arrivals;

import be.bonaf.publictransport.adapter.tfl.*;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.Trip;
import be.bonaf.publictransport.domain.schedule.Departure;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static be.bonaf.publictransport.adapter.tfl.DepartureTestBuilder.*;
import static be.bonaf.publictransport.domain.journey.Journey.Trip.TransportStation.*;
import static org.assertj.core.api.Assertions.assertThat;

class ArrivalTimeClientTest {

  @Test
  void arrivalTimes() {
    ArrivalTimeClient arrivalTimeClient = new ArrivalTimeClient(new TflClientStub());

    Journey journey = Journey.builder().trips(List.of(aTrip())).build();

    List<ArrivalTime> arrivalTimes = arrivalTimeClient.arrivalTimes(journey);

    assertThat(arrivalTimes)
        .containsExactly(
            liverpoolArrival().minutesUntilArrival(15).build(),
            liverpoolArrival().minutesUntilArrival(30).build());
  }

  private ArrivalTime.ArrivalTimeBuilder liverpoolArrival() {
    return ArrivalTime.builder()
        .platformName("Platform 1")
        .destinationName("London Liverpool Street Rail Station");
  }

  private Trip aTrip() {
    return Trip.builder()
        .startingPoint(aTransportStation().stationId("910GCAMHTH").build())
        .line("london-overground")
        .direction("910GLIVST")
        .build();
  }

  @Test
  void arrivalTimesPerLine() {
    ArrivalTimeClient arrivalTimeClient = new ArrivalTimeClient(new TflClientStub());

    Journey journey = Journey.builder().trips(List.of(aBusTrip())).build();

    Map<String, List<ArrivalTime>> arrivalTimesPerLine =
        arrivalTimeClient.arrivalTimesPerLine(journey);

    assertThat(arrivalTimesPerLine)
        .contains(
            Map.entry(
                "55",
                List.of(
                    anArrivalTime().minutesUntilArrival(3).build(),
                    anArrivalTime().minutesUntilArrival(21).build(),
                    anArrivalTime().minutesUntilArrival(30).build())));
  }

  private Trip aBusTrip() {
    return Trip.builder()
        .startingPoint(aTransportStation().stationId("490001044N").build())
        .line("55")
        .direction("Liverpool Street Or Old Street")
        .build();
  }

  private ArrivalTime.ArrivalTimeBuilder anArrivalTime() {
    return ArrivalTime.builder().destinationName("Liverpool Street Or Old Street");
  }

  static class TflClientStub implements TflClient {
    @Override
    public List<Departure> departures(
        String startingNaptanId, String destinationNaptanId, String lineId) {
      return List.of(
          aDefaultToLiverpoolStation().withMinutesAndSecondsToArrival(45, 4).build(),
          aDefaultToLiverpoolStation().withMinutesAndSecondsToArrival(30, 4).build(),
          aDefaultToLiverpoolStation().withMinutesAndSecondsToArrival(15, 4).build(),
          aDefaultToCheshuntStation().withMinutesAndSecondsToArrival(7, 4).build());
    }

    @Override
    public Map<String, List<Arrival>> arrivals(String stopId) {
      Arrival arrival55_01 = Arrival.of("Liverpool Street Or Old Street", 1309);
      Arrival arrival55_02 = Arrival.of("Liverpool Street Or Old Street", 1815);
      Arrival arrival55_03 = Arrival.of("Liverpool Street Or Old Street", 220);

      Arrival arrival26_01 = Arrival.of("Liverpool Street Or Old Street", 1013);
      Arrival arrival26_02 = Arrival.of("Liverpool Street Or Old Street", 632);
      Arrival arrival26_03 = Arrival.of("Liverpool Street Or Old Street", 1463);
      Arrival arrival26_04 = Arrival.of("Liverpool Street Or Old Street", 475);

      return Map.of(
          "55", List.of(arrival55_01, arrival55_02, arrival55_03),
          "26", List.of(arrival26_01, arrival26_02, arrival26_03, arrival26_04));
    }
  }
}
