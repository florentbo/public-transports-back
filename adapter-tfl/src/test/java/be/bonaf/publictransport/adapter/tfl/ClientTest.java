package be.bonaf.publictransport.adapter.tfl;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import java.util.List;
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

    assertThat(departures).hasSize(18).contains(DepartureTestBuilder.aDefaultToLiverpoolStation());
  }
}
