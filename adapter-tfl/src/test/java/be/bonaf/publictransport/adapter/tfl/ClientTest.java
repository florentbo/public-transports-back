package be.bonaf.publictransport.adapter.tfl;

import static be.bonaf.publictransport.adapter.tfl.DepartureTestBuilder.aDefaultToLiverpoolStation;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

import be.bonaf.publictransport.domain.schedule.Departure;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

@WireMockTest
class ClientTest {

  @Test
  void stopPointArrivalDeparturesElements(WireMockRuntimeInfo wm) {
    stubFor(
        get(urlPathEqualTo("/StopPoint/910GCAMHTH/ArrivalDepartures"))
            .withQueryParam("lineIds", equalTo("london-overground"))
            .willReturn(aResponse().withStatus(200).withBodyFile("ArrivalDepartures.json")));

    TflClient tflClient = new TflConnector(wm.getHttpBaseUrl());

    List<Departure> departures =
        tflClient.departures("910GCAMHTH", "910GLIVST", "london-overground");

    assertThat(departures).hasSize(18).contains(aDefaultToLiverpoolStation().build());
  }

  @Test
  void arrivals(WireMockRuntimeInfo wm) {
    stubFor(
        get(urlPathEqualTo("/StopPoint/490001044N/Arrivals"))
            .willReturn(aResponse().withStatus(200).withBodyFile("Arrivals.json")));

    TflClient tflClient = new TflConnector(wm.getHttpBaseUrl());
    Map<String, List<Arrival>> arrivals = tflClient.arrivals("490001044N");
    Arrival arrival55_01 = Arrival.of("outbound", 1309);
    Arrival arrival55_02 = Arrival.of("outbound", 1815);
    Arrival arrival55_03 = Arrival.of("outbound", 220);

    Arrival arrival26_01 = Arrival.of("outbound", 1013);
    Arrival arrival26_02 = Arrival.of("outbound", 632);
    Arrival arrival26_03 = Arrival.of("outbound", 1463);
    Arrival arrival26_04 = Arrival.of("outbound", 475);

    assertThat(arrivals).contains(

            entry("55", List.of(arrival55_01, arrival55_02, arrival55_03)),
            entry("26", List.of(arrival26_01, arrival26_02, arrival26_03, arrival26_04))

    );
  }
}
