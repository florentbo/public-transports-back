package be.bonaf.publictransport.service;

import be.bonaf.publictransport.adapter.LondonArrivalTimeService;
import be.bonaf.publictransport.adapter.TflConfiguration;
import be.bonaf.publictransport.adapter.configuration.*;
import be.bonaf.publictransport.domain.DomainNativeConfiguration;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

import java.util.*;

import static be.bonaf.publictransport.domain.journey.Journey.*;

@Configuration
@Import({
  TflConfiguration.class,
  BruConfiguration.class,
  InMemoryConfiguration.class,
})
@ImportRuntimeHints(DomainNativeConfiguration.class)
@Slf4j
public class ServiceConfiguration {

  private final ArrivalTimeService arrivalTimeService;
  private final ArrivalTimeService bruArrivalTimeService;
  private final UserRepository userRepository;

  public ServiceConfiguration(
      @LondonArrivalTimeService ArrivalTimeService arrivalTimeService,
      @BruArrivalTimeService ArrivalTimeService bruArrivalTimeService,
      UserRepository userRepository) {
    this.arrivalTimeService = arrivalTimeService;
    this.bruArrivalTimeService = bruArrivalTimeService;
    this.userRepository = userRepository;
  }

  @Bean
  public JourneyService journeyService() {
    return aJourneyService();
  }

  private JourneyService aJourneyService() {
    return new JourneyService() {
      @Override
      public List<Journey> currentUserJourneys() {
        List<Journey> values = userRepository.journeys();
        log.info("Found {} journeys", values.size());
        return values;
      }

      @Override
      public List<TransportOption> transportOptions(JourneyId journeyId) {
        var journey = userRepository.journey(journeyId);
        return switch (journey.city()) {
          case LONDON -> london(journey);
          case BRUSSELS -> bru(journey);
        };
      }

      private List<TransportOption> bru(Journey journey) {
        var arrivalTimes = bruArrivalTimeService.arrivalTimesPerLine(journey);
        return transportOptions(journey, arrivalTimes);
      }

      private List<TransportOption> london(Journey journey) {
        var arrivalTimes = arrivalTimeService.arrivalTimesPerLine(journey);
        return transportOptions(journey, arrivalTimes);
      }

      private List<TransportOption> transportOptions(
          Journey journey, Map<String, List<ArrivalTime>> arrivalTimes) {
        return journey.trips().stream()
            .map(
                trip -> {
                  var arrivals =
                      arrivalTimes.get(trip.line()).stream().map(ArrivalMapper::from).toList();
                  return TransportOption.builder()
                      .type(TransportType.TRAIN)
                      .line(trip.line())
                      .startStation(trip.startingPoint().name())
                      .direction(trip.direction())
                      .arrivals(arrivals)
                      .build();
                })
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
