package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.domain.journey.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.*;

import static be.bonaf.publictransport.domain.journey.Journey.*;

import static be.bonaf.publictransport.domain.journey.Journey.Trip.TransportStation.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NativeConfigurationTest {

  private static final JourneyId LONDON_JOURNEY_ID =
      new JourneyId(UUID.fromString("c4a2c3b4-b6d0-4e0f-a7e1-e2f2d1c0b0c1"));
  private static final JourneyId BRUSSELS_JOURNEY_ID =
      JourneyId.from("a3b8d517-8c2a-40d3-9673-736a9398fd6e");

  @Test
  void writeAndRead() {
    String json = writeValue(journeys());
    log.debug("JSON: {}", json);
    assertThat(JourneyReader.readValue(new ByteArrayInputStream(json.getBytes()))).isEqualTo(journeys());
  }

  @Test
  void readFromFile() throws Exception {
    var file = ResourceUtils.getFile("classpath:Journeys.json");
    var journeys = new FileInputStream(file);

    assertThat(JourneyReader.readValue(journeys)).isEqualTo(journeys());
  }

  private String writeValue(Map<JourneyId, Journey> journeys) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addKeySerializer(JourneyId.class, new JourneyIdKeySerializer());
      module.addSerializer(JourneyId.class, new JourneyIdSerializer());
      mapper.registerModule(module);

      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(journeys);
    } catch (JsonProcessingException e) {
      log.error("Error while mapping response body: {}", journeys, e);
      throw new RuntimeException(e);
    }
  }

  private Map<JourneyId, Journey> journeys() {
    var cambridgeHeath =
        Location.aLocation().withName("Cambridge Heath (London) Rail Station").build();

    var londonLiverpool =
        Location.aLocation().withName("London Liverpool Street Rail Station").build();

    var journey =
        builder()
            .id(LONDON_JOURNEY_ID)
            .city(City.LONDON)
            .origin(cambridgeHeath)
            .destination(londonLiverpool)
            .trips(List.of(londonTrip()))
            .build();

    var home = Location.aLocation().withName("Home").build();

    var downtown = Location.aLocation().withName("Downtown").build();

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

  public static class JourneyIdSerializer extends JsonSerializer<JourneyId> {
    @Override
    public void serialize(
        JourneyId journeyId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
      try {
        log.debug("Serializing value: {}", journeyId);
        jsonGenerator.writeString(journeyId.value().toString());
      } catch (IOException e) {
        log.error("Error while serializing value: {}", journeyId, e);
        throw new RuntimeException(e);
      }
    }
  }

  public static class JourneyIdKeySerializer extends JsonSerializer<JourneyId> {
    @Override
    public void serialize(
        JourneyId journeyId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
      try {
        log.debug("Serializing key: {}", journeyId);
        jsonGenerator.writeFieldName(journeyId.value().toString());
      } catch (IOException e) {
        log.error("Error while serializing key: {}", journeyId, e);
        throw new RuntimeException(e);
      }
    }
  }
}
