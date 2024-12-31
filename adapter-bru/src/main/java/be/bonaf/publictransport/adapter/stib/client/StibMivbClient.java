package be.bonaf.publictransport.adapter.stib.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class StibMivbClient {

  private static final String WAITING_TIME_PATH =
      "/catalog/datasets/waiting-time-rt-production/records?";
  private final String baseUrl;
  private final String apiKey;

  public static void main(String[] args) {
    String apiKey = "aa811295bf39fa65cabc9db92a726bcaf373eb062201ec5b2d4abec6";
    String baseUrl = "https://stibmivb.opendatasoft.com/api/explore/v2.1";
    var client = new StibMivbClient(baseUrl, apiKey);
    WaitingTimes waitingTimes = client.waitingTimes(List.of("8784", "5008"));
    System.out.println(waitingTimes);
  }

  public WaitingTimes waitingTimes(List<String> pointIds) {
    String responseBody = send(pointIds);
    return map(responseBody);
  }

  private String filteringPath(List<String> pointIds) {
    log.info("Sending request for waiting times for points: {}", pointIds);
    return "where="
        + pointIds.stream().map(id -> "pointid=" + id).collect(Collectors.joining(" or "));
  }

  private String send(List<String> pointIds) {
    try {
      String waitingPoints =
          baseUrl + WAITING_TIME_PATH + filteringPath(pointIds) + "&apikey=" + apiKey;
      log.debug("Sending request to: {}", waitingPoints);
      URL url = new URL(waitingPoints);
      URI uri =
          new URI(
              url.getProtocol(),
              url.getUserInfo(),
              url.getHost(),
              url.getPort(),
              url.getPath(),
              url.getQuery(),
              url.getRef());

      HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

      return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
    } catch (IOException | InterruptedException e) {
      log.error("Http error while sending request", e);
      throw new RuntimeException(e);
    } catch (Exception e) {
      log.error("Unexpected error while sending request", e);
      throw new RuntimeException(e);
    }
  }

  private WaitingTimes map(String body) {
    try {
      return new ObjectMapper().readValue(body, WaitingTimes.class);
    } catch (JsonProcessingException e) {
      log.error("Error while mapping response body: {}", body, e);
      throw new RuntimeException(e);
    }
  }
}
