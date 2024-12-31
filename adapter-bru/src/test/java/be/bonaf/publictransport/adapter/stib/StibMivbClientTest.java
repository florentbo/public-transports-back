package be.bonaf.publictransport.adapter.stib;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import org.junit.jupiter.api.Test;

@WireMockTest
class StibMivbClientTest {

  private static final String API_KEY = "aKey";

  @Test
  void waitingTimes(WireMockRuntimeInfo wm) {
    stubFor(
        get(urlEqualTo(
                "/catalog/datasets/waiting-time-rt-production/records?where=pointid=8784%20or%20pointid=5008&apikey=aKey"))
            .willReturn(aResponse().withStatus(200).withBodyFile("PassingTimes.json")));

    var client = new StibMivbClient(wm.getHttpBaseUrl(), API_KEY);
    WaitingTimes waitingTimes = client.waitingTimes(List.of("8784", "5008"));
    assertThat(waitingTimes.count()).isEqualTo(2);
  }
}
