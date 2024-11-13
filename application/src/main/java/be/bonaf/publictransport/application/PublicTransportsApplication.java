package be.bonaf.publictransport.application;

import be.bonaf.publictransport.adapter.rest.config.RestAdapterConfig;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.user.UserId;
import be.bonaf.publictransport.service.JourneyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

import java.util.List;
import java.util.UUID;

import static be.bonaf.publictransport.domain.journey.Journey.Trip.TransportStation.*;

@SpringBootApplication
@Import({
  RestAdapterConfig.class,
})
@Log4j2
public class PublicTransportsApplication {
  public static void main(String[] args) {
    SpringApplication.run(PublicTransportsApplication.class, args);
  }

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
            Journey.builder()
                .id(new Journey.JourneyId(UUID.fromString("c4a2c3b4-b6d0-4e0f-a7e1-e2f2d1c0b0c1")))
                .userId(new UserId("user-id"))
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

      private Journey.Trip aTrip() {
        return Journey.Trip.builder()
            .startingPoint(
                aTransportStation().stationId("910GCAMHTH").line("london-overground").build())
            .direction("910GLIVST")
            .build();
      }

      @Override
      public List<Journey.TransportOption> transportOptions(Journey.JourneyId journey) {
        Journey.Arrival arrival01 = new Journey.Arrival(45, "metro-platform");
        Journey.Arrival arrival02 = new Journey.Arrival(60, "metro-platform");
        Journey.TransportOption transportOption =
            Journey.TransportOption.builder()
                .type(Journey.TransportType.METRO)
                .line("metro-line")
                .startStation("metro-station")
                .direction("metro-direction")
                .arrivals(List.of(arrival01, arrival02))
                .build();

        return List.of(transportOption);
      }
    };
  }
}
