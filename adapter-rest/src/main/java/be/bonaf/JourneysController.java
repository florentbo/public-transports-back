package be.bonaf;

import be.bonaf.publictransport.adapter.rest.api.JourneysApi;
import be.bonaf.publictransport.adapter.rest.model.*;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.TransportOption;
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
  public ResponseEntity<TransportOptionsDTO> getTransportOptions(UUID journeyId) {
    List<TransportOption> transportOptions =
        journeyService.transportOptions(new Journey.JourneyId(journeyId));
    TransportOptionsDTO transportOptionsDTO = new TransportOptionsDTO();
    transportOptionsDTO.setTransportOptions(map(transportOptions));
    return ResponseEntity.ok(transportOptionsDTO);
  }

  private List<TransportOptionDTO> map(List<TransportOption> transportOptions) {
    return transportOptions.stream().map(this::from).toList();
  }

  private TransportOptionDTO from(TransportOption transportOption) {
    return new TransportOptionDTO()
        .type(TransportOptionDTO.TypeEnum.valueOf(transportOption.type().name()))
        .line(transportOption.line())
        .startStation(transportOption.startStation())
        .direction(transportOption.direction())
        .arrivals(transportOption.arrivals().stream().map(this::from).toList());
  }

  private ArrivalDTO from(Journey.Arrival arrival) {
    return new ArrivalDTO().time(arrival.time()).platform(arrival.platform());
  }
}
