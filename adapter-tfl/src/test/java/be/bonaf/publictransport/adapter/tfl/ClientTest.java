package be.bonaf.publictransport.adapter.tfl;

import org.junit.jupiter.api.Test;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WireMockTest
public class ClientTest {

  @Test
  public void callFooAndBar(WireMockRuntimeInfo wm) throws Exception {
    String name = "Ada";

    stubFor(
        get(urlPathEqualTo("/foo"))
            .withQueryParam("name", equalTo(name))
            .willReturn(ok().withBody("Hello " + name + " I am Foo!")));

    stubFor(
        get(urlPathMatching("/bar/" + name))
            .willReturn(ok().withBody("Hello " + name + " I am Bar!")));

    App app = new App(name, wm.getHttpBaseUrl(), wm.getHttpBaseUrl());

    String expectedOutput =
        "Hi! I am "
            + name
            + "\n"
            + "I called Foo and its response is Hello "
            + name
            + " I am Foo!\n"
            + "I called Bar and its response is Hello "
            + name
            + " I am Bar!\n"
            + "Bye!";

    assertEquals(expectedOutput, app.execute());
  }

  public static class App {

    private final String name;
    private final String fooBaseUrl;
    private final String barBaseUrl;

    public App(String name, String fooBaseUrl, String barBaseUrl) {
      this.name = name;
      this.fooBaseUrl = fooBaseUrl;
      this.barBaseUrl = barBaseUrl;
    }

    public String execute() throws Exception {
      StringBuilder sb = new StringBuilder();

      sb.append("Hi! I am ").append(name).append("\n");

      String fooResponse = callFoo();
      sb.append("I called Foo and its response is ").append(fooResponse).append("\n");

      String barResponse = callBar();
      sb.append("I called Bar and its response is ").append(barResponse).append("\n");

      sb.append("Bye!");

      return sb.toString();
    }

    private String callFoo() throws Exception {
        String url = fooBaseUrl + "/foo?name=" + name;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    private String callBar() throws Exception {

        String url = barBaseUrl + "/bar/" + name;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }
  }
}
