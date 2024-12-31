package be.bonaf.publictransport.service;

import be.bonaf.publictransport.adapter.LondonArrivalTimeService;
import be.bonaf.publictransport.adapter.TflConfiguration;
import be.bonaf.publictransport.domain.journey.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

import java.util.*;

import static be.bonaf.publictransport.domain.journey.Journey.*;
import static be.bonaf.publictransport.domain.journey.Journey.Trip.TransportStation.*;

@Configuration
@Import({
  TflConfiguration.class,
})
@Slf4j
public class ServiceConfiguration {

  private static final JourneyId LONDON_JOURNEY_ID =
      new JourneyId(UUID.fromString("c4a2c3b4-b6d0-4e0f-a7e1-e2f2d1c0b0c1"));
  private static final JourneyId BRUSSELS_JOURNEY_ID =
      JourneyId.from("a3b8d517-8c2a-40d3-9673-736a9398fd6e");

  private final ArrivalTimeService arrivalTimeService;

  public ServiceConfiguration(@LondonArrivalTimeService ArrivalTimeService arrivalTimeService) {
    this.arrivalTimeService = arrivalTimeService;
  }

  @Bean
  public JourneyService journeyService() {
    return aJourneyService();
  }

  private JourneyService aJourneyService() {
    return new JourneyService() {
      @Override
      public List<Journey> currentUserJourneys() {
        Collection<Journey> values = journeys().values();
        log.info("Found {} journeys", values.size());
        return values.stream().toList();
      }

      private Map<JourneyId, Journey> journeys() {
        var cambridgeHeath =
            Location.aLocation()
                .withLocationId(LocationId.create())
                .withName("Cambridge Heath (London) Rail Station")
                .build();
        var londonLiverpool =
            Location.aLocation()
                .withLocationId(LocationId.create())
                .withName("London Liverpool Street Rail Station")
                .build();
        var journey =
            builder()
                .id(LONDON_JOURNEY_ID)
                .city(City.LONDON)
                .origin(cambridgeHeath)
                .destination(londonLiverpool)
                .trips(List.of(londonTrip()))
                .build();

        var home =
            Location.aLocation().withLocationId(LocationId.create()).withName("Home").build();
        var downtown =
            Location.aLocation().withLocationId(LocationId.create()).withName("Downtown").build();

        var homeToDowntown =
            builder()
                .id(BRUSSELS_JOURNEY_ID)
                .city(City.BRUSSELS)
                .origin(home)
                .destination(downtown)
                .trips(List.of(soutStationTrip(), elizabethTrip()))
                .build();
        return Map.of(LONDON_JOURNEY_ID, journey, BRUSSELS_JOURNEY_ID, homeToDowntown);
      }

      private Trip soutStationTrip() {
        return Trip.builder()
            .startingPoint(aTransportStation().stationId("5008").name("Woest").build())
            .line("51")
            .direction("GARE DU MIDI")
            .build();
      }

      private Trip elizabethTrip() {
        return Trip.builder()
            .startingPoint(aTransportStation().stationId("8784").name("Pannenhuis").build())
            .line("6")
            .direction("ELISABETH")
            .build();
      }

      private Trip londonTrip() {
        return Trip.builder()
            .startingPoint(aTransportStation().stationId("910GCAMHTH").build())
            .direction("910GLIVST")
            .line("london-overground")
            .build();
      }

      @Override
      public List<TransportOption> transportOptions(JourneyId journeyId) {
        var journey = journeys().get(journeyId);

        var arrivalTimes = arrivalTimeService.arrivalTimes(journeys().get(journeyId));
        var arrivals = arrivalTimes.stream().map(ArrivalMapper::from).toList();

        return journey.trips().stream()
            .map(
                trip ->
                    TransportOption.builder()
                        .type(TransportType.TRAIN)
                        .line(trip.line())
                        .startStation(trip.startingPoint().name())
                        .direction(trip.direction())
                        .arrivals(arrivals)
                        .build())
            .toList();
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
