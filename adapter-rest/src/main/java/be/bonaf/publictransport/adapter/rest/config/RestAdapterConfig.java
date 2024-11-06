package be.bonaf.publictransport.adapter.rest.config;

import be.bonaf.JourneysController;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.Trip;
import be.bonaf.publictransport.domain.journey.Journey.Trip.TransportStation;
import be.bonaf.publictransport.domain.service.JourneyService;
import be.bonaf.publictransport.domain.user.UserId;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.UUID;

import static be.bonaf.publictransport.domain.journey.Journey.*;

@Configuration
public class RestAdapterConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
      }
    };
  }

  @Bean
  public JourneysController journeysController() {
    return new JourneysController(journeyService());
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
            builder()
                .id(new JourneyId(UUID.fromString("c4a2c3b4-b6d0-4e0f-a7e1-e2f2d1c0b0c1")))
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

      private Trip aTrip() {
        return Trip.builder()
            .startingPoint(
                new TransportStation(
                    "910GCAMHTH", "Cambridge Heath (London) Rail Station", "london-overground"))
            .direction("910GLIVST")
            .build();
      }

      @Override
      public List<ArrivalTime> arrivalTimes(JourneyId journey) {
        ArrivalTime arrivalTime01 =
            ArrivalTime.builder()
                .destinationName("London Liverpool Street Rail Station")
                .minutesUntilArrival(45)
                .status(ArrivalTime.ArrivalStatus.ON_TIME)
                .build();

        ArrivalTime arrivalTime02 =
            ArrivalTime.builder()
                .destinationName("London Liverpool Street Rail Station")
                .minutesUntilArrival(15)
                .status(ArrivalTime.ArrivalStatus.ON_TIME)
                .build();

        return List.of(arrivalTime01, arrivalTime02);
      }

      @Override
      public List<TransportOption> transportOptions(JourneyId journey) {
        Arrival arrival01 = new Arrival(45, "metro-platform");
        Arrival arrival02 = new Arrival(60, "metro-platform");
        TransportOption transportOption =
            TransportOption.builder()
                .type(TransportType.METRO)
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
