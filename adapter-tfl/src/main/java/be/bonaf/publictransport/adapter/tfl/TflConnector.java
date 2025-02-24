package be.bonaf.publictransport.adapter.tfl;

import static java.util.stream.Collectors.*;

import be.bonaf.publictransport.domain.schedule.Departure;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import lombok.extern.slf4j.Slf4j;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.StopPointApi;
import org.openapitools.client.model.TflApiPresentationEntitiesArrivalDeparture;
import org.openapitools.client.model.TflApiPresentationEntitiesPrediction;


@Slf4j
public class TflConnector implements TflClient {
  private final StopPointApi stopPointApi;

  public TflConnector(String url) {
    ApiClient client = new ApiClient();
    client.updateBaseUri(url);

    ObjectMapper mapper = client.getObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addDeserializer(OffsetDateTime.class, new SafeInstantDeserializer());
    mapper.registerModule(module);

    client.setObjectMapper(mapper);

    this.stopPointApi = new StopPointApi(client);
  }

  static class SafeInstantDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context) {
      try {
        return OffsetDateTime.parse(
            parser.getText().trim(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
      } catch (Exception e) {
        return null;
      }
    }
  }

  @Override
  public List<Departure> departures(
      String startingNaptanId, String destinationNaptanId, String lineId) {
    try {
      List<TflApiPresentationEntitiesArrivalDeparture> tflApiPresentationEntitiesArrivalDepartures =
          stopPointApi.stopPointArrivalDepartures(startingNaptanId, List.of(lineId));
      return tflApiPresentationEntitiesArrivalDepartures.stream().map(Mapper::from).toList();
    } catch (ApiException e) {
      log.error("Error while calling TfL API", e);
      throw new TflClientException(e);
    }
  }

  @Override
  public Map<String, List<Arrival>> arrivals(String stopId) {
    try {
      var stopPointArrivals = stopPointApi.stopPointArrivals(stopId);
      return stopPointArrivals.stream()
          .map(StopPointArrival::from)
          .collect(groupingBy(StopPointArrival::stopId, mapping(toArrival(), toList())));
    } catch (ApiException e) {
      log.error("Error while calling TfL API", e);
      throw new TflClientException(e);
    }
  }

  private Function<StopPointArrival, Arrival> toArrival() {
    return stopPointArrival ->
        Arrival.of(stopPointArrival.direction(), stopPointArrival.secondsToStation());
  }

  record StopPointArrival(String stopId, String direction, Integer secondsToStation) {
    static StopPointArrival from(TflApiPresentationEntitiesPrediction prediction) {
      return of(prediction.getLineId(), prediction.getDirection(), prediction.getTimeToStation());
    }

    static StopPointArrival of(String stopId, String towards, Integer secondsToStation) {
      return new StopPointArrival(stopId, towards, secondsToStation);
    }
  }

  static class TflClientException extends RuntimeException {
    public TflClientException(ApiException e) {
      super(e);
    }
  }
}
