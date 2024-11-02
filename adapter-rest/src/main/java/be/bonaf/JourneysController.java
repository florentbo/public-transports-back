package be.bonaf;

import be.bonaf.publictransport.adapter.rest.api.JourneysApi;
import be.bonaf.publictransport.adapter.rest.model.ArrivalTimeDTO;
import be.bonaf.publictransport.adapter.rest.model.JourneyDTO;
import be.bonaf.publictransport.adapter.rest.model.LocationDTO;
import be.bonaf.publictransport.domain.journey.ArrivalTime;
import be.bonaf.publictransport.domain.journey.Journey;
import be.bonaf.publictransport.domain.service.JourneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class JourneysController implements JourneysApi {

  private final JourneyService journeyService;

  public JourneysController(JourneyService journeyService) {
    this.journeyService = journeyService;
  }

  @Override
  public ResponseEntity<List<JourneyDTO>> getCurrentUserJourneys() {
    List<Journey> journeys = journeyService.currentUserJourneys();
    List<JourneyDTO> journeyDtos = journeys.stream().map(this::from).toList();

    return ResponseEntity.ok(journeyDtos);
  }

  JourneyDTO from(Journey journey) {
    JourneyDTO journeyDTO = new JourneyDTO();
    journeyDTO.setId(journey.id().value());
    journeyDTO.setOrigin(new LocationDTO().name(journey.origin().name()));
    journeyDTO.setDestination(new LocationDTO().name(journey.destination().name()));
    return journeyDTO;
  }

  @Override
  public ResponseEntity<List<ArrivalTimeDTO>> getJourneyArrivalTimes(UUID journeyId) {
    List<ArrivalTime> arrivalTimes = journeyService.arrivalTimes(new Journey.JourneyId(journeyId));
    List<ArrivalTimeDTO> arrivalTimeDtos = arrivalTimes.stream().map(this::from).toList();

    return ResponseEntity.ok(arrivalTimeDtos);
  }

  ArrivalTimeDTO from(ArrivalTime arrivalTime) {
    ArrivalTimeDTO arrivalTimeDTO = new ArrivalTimeDTO();
    arrivalTimeDTO.setDestinationName(arrivalTime.destinationName());
    arrivalTimeDTO.setMinutesUntilArrival(arrivalTime.minutesUntilArrival());
    return arrivalTimeDTO;
  }
}
