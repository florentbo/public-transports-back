package be.bonaf.publictransport.adapter.tfl;

import java.util.List;
import java.util.Map;

import be.bonaf.publictransport.domain.journey.ArrivalTime;
import be.bonaf.publictransport.domain.schedule.Departure;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.StopPointApi;
import org.openapitools.client.model.TflApiPresentationEntitiesArrivalDeparture;

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
      log.info("TflConnector +++++++++++++++++++++++++++++");
      List<TflApiPresentationEntitiesArrivalDeparture> tflApiPresentationEntitiesArrivalDepartures = stopPointApi.stopPointArrivalDepartures(startingNaptanId, List.of(lineId));
      log.info("Found {} departures", tflApiPresentationEntitiesArrivalDepartures);
      log.info("TflConnector -----------------------------");
      return tflApiPresentationEntitiesArrivalDepartures.stream()
          .map(Mapper::from)
          .toList();
    } catch (ApiException e) {
      log.error("Error while calling TfL API", e);
      throw new TflClientException(e);
    }
  }

  @Override
  public Map<String, List<ArrivalTime>> arrivals(String stopId) {
    return Map.of();
  }

  static class TflClientException extends RuntimeException {
    public TflClientException(ApiException e) {
      super(e);
    }
  }
}
