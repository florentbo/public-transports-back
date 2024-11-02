package be.bonaf;

import be.bonaf.publictransport.adapter.rest.api.JourneysApi;
import be.bonaf.publictransport.adapter.rest.model.JourneyDTO;
import be.bonaf.publictransport.adapter.rest.model.LocationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JourneysController implements JourneysApi {
  @Override
  public ResponseEntity<List<JourneyDTO>> getCurrentUserJourneys() {
    JourneyDTO journeyDTO = new JourneyDTO();
    journeyDTO.setId(1L);
    journeyDTO.setOrigin(new LocationDTO().name("Brussels"));
    journeyDTO.setDestination(new LocationDTO().name("Antwerp"));
    List<JourneyDTO> journeys = List.of(journeyDTO);
    return ResponseEntity.ok(journeys);
  }
}
