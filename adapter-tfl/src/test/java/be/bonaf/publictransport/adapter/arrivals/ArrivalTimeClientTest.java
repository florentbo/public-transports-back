package be.bonaf.publictransport.adapter.arrivals;

import be.bonaf.publictransport.adapter.tfl.*;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.Trip;
import org.junit.jupiter.api.Test;

import java.util.List;

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
  }
}
