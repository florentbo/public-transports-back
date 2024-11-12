package be.bonaf.publictransport.adapter.tfl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.StopPointApi;

@Slf4j
public class TflConnector implements TflClient {
  private final StopPointApi stopPointApi;

  public TflConnector(String url) {
    ApiClient client = new ApiClient();
    client.updateBaseUri(url);
    this.stopPointApi = new StopPointApi(client);
  }

  @Override
  public List<Departure> departures(
      String startingNaptanId, String destinationNaptanId, String lineId) {
    try {
      return stopPointApi.stopPointArrivalDepartures(startingNaptanId, List.of(lineId)).stream()
          .map(Mapper::from)
          .toList();
    } catch (ApiException e) {
      log.error("Error while calling TfL API", e);
      throw new TflClientException(e);
    }
  }

  static class TflClientException extends RuntimeException {
    public TflClientException(ApiException e) {
      super(e);
    }
  }
}
