package be.bonaf.publictransport.service;

import be.bonaf.publictransport.adapter.TflConfiguration;
import be.bonaf.publictransport.domain.journey.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.*;

import java.util.*;

import static be.bonaf.publictransport.domain.journey.Journey.*;

@Configuration
@AllArgsConstructor
@Import({
  TflConfiguration.class,
})
public class ServiceConfiguration {

  private final ArrivalTimeService arrivalTimeService;

  @Bean
  public JourneyService journeyService() {
    return aJourneyService();
  }

  private JourneyService aJourneyService() {
    return new JourneyService() {
      @Override
      public List<Journey> currentUserJourneys() {
        return journeys();
      }

      private List<Journey> journeys() {
        Journey journey =
            builder()
                .id(new JourneyId(UUID.fromString("c4a2c3b4-b6d0-4e0f-a7e1-e2f2d1c0b0c1")))
                .origin(
                    new Location(
                        new LocationId("origin-id"), "Cambridge Heath (London) Rail Station"))
                .destination(
                    new Location(
                        new LocationId("destination-id"), "London Liverpool Street Rail Station"))
                .trips(List.of(aTrip()))
                .build();
        return List.of(journey);
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

      @Override
      public List<TransportOption> transportOptions(JourneyId journey) {
        var arrivalTimes = arrivalTimeService.arrivalTimes(journeys().get(0));
        var arrivals = arrivalTimes.stream().map(ArrivalMapper::from).toList();
        TransportOption transportOption =
            TransportOption.builder()
                .type(TransportOption.TransportType.TRAIN)
                .line("london-overground")
                .startStation("Cambridge Heath (London) Rail Station")
                .direction("London Liverpool Street Rail Station")
                .arrivals(arrivals)
                .build();

        return List.of(transportOption);
      }
    };
  }

  static class ArrivalMapper {
    public static TransportOption.Arrival from(ArrivalTime arrivalTime) {
      return TransportOption.Arrival.builder()
          .minutesUntilArrival(arrivalTime.minutesUntilArrival())
          .platform(arrivalTime.platformName())
          .build();
    }
  }
}
