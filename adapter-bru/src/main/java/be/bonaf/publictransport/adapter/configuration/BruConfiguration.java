package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.adapter.stib.Mapper;
import be.bonaf.publictransport.adapter.stib.client.StibMivbClient;
import be.bonaf.publictransport.adapter.stib.client.WaitingTimes;
import be.bonaf.publictransport.domain.journey.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.*;

import java.time.Clock;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class BruConfiguration {

  @Bean
  @BruArrivalTimeService
  public ArrivalTimeService bruArrivalTimeService() {
    return new ArrivalTimeService() {
      @Override
      public List<ArrivalTime> arrivalTimes(Journey journey) {

        String apiKey = "aa811295bf39fa65cabc9db92a726bcaf373eb062201ec5b2d4abec6";
        String baseUrl = "https://stibmivb.opendatasoft.com/api/explore/v2.1";
        var client = new StibMivbClient(baseUrl, apiKey);
        Mapper mapper = new Mapper(Clock.systemDefaultZone());

        List<Journey.Trip> trips = journey.trips();
        WaitingTimes waitingTimes =
            client.waitingTimes(
                trips.stream()
                    .map(Journey.Trip::startingPoint)
                    .map(Journey.Trip.TransportStation::stationId)
                    .toList());
        return mapper.from(waitingTimes);
      }

      @Override
      public Map<String, List<ArrivalTime>> arrivalTimesPerLine(Journey journey) {

        String apiKey = "aa811295bf39fa65cabc9db92a726bcaf373eb062201ec5b2d4abec6";
        String baseUrl = "https://stibmivb.opendatasoft.com/api/explore/v2.1";
        var client = new StibMivbClient(baseUrl, apiKey);
        List<Journey.Trip> trips = journey.trips();
        WaitingTimes waitingTimes =
            client.waitingTimes(
                trips.stream()
                    .map(Journey.Trip::startingPoint)
                    .map(Journey.Trip.TransportStation::stationId)
                    .toList());

        Mapper mapper = new Mapper(Clock.systemDefaultZone());
        return mapper.fromMultipleLines(waitingTimes);
      }
    };
  }
}
