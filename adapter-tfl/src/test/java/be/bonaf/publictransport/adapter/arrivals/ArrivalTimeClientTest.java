package be.bonaf.publictransport.adapter.arrivals;

import be.bonaf.publictransport.adapter.tfl.*;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.Trip;
import org.junit.jupiter.api.Test;

import java.util.List;

import static be.bonaf.publictransport.adapter.tfl.DepartureTestBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

class ArrivalTimeClientTest {

  @Test
  void arrivalTimes() {
    ArrivalTimeClient arrivalTimeClient = new ArrivalTimeClient(new TflClientStub());
    Journey journey = Journey.builder().trips(List.of(aTrip())).build();
    List<ArrivalTime> arrivalTimes = arrivalTimeClient.arrivalTimes(journey);
    assertThat(arrivalTimes)
        .contains(
            ArrivalTime.builder()
                .destinationName("London Liverpool Street Rail Station")
                .minutesUntilArrival(3)
                .build());
  }

  private Trip aTrip() {
    return Trip.builder()
        .startingPoint(
            Trip.TransportStation.aTransportStation()
                .stationId("910GCAMHTH")
                .line("london-overground")
                .build())
        .direction("910GLIVST")
        .build();
  }

  static class TflClientStub implements TflClient {
    @Override
    public List<Departure> departures(
        String startingNaptanId, String destinationNaptanId, String lineId) {
        return List.of(aDefaultToLiverpoolStation().withMinutesAndSecondsToArrival(3, 4).build());
    }
  }
}
