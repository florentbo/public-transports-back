package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.domain.journey.Journey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class JourneyReader {
    public static Map<Journey.JourneyId, Journey> readValue(InputStream json) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Journey.JourneyId.class, new JourneyIdKeyDeserializer());
        module.addDeserializer(
            Journey.JourneyId.class,
            new JsonDeserializer<>() {
              @Override
              public Journey.JourneyId deserialize(
                      JsonParser jsonParser, DeserializationContext deserializationContext)
                  throws IOException {
                return Journey.JourneyId.from(jsonParser.getValueAsString());
              }
            });
        mapper.registerModule(module);

        return mapper.readValue(json, new TypeReference<>() {});
      } catch (IOException e) {
        log.error("Error while mapping response body: {}", json, e);
        throw new RuntimeException(e);
      }
    }

    public static class JourneyIdKeyDeserializer extends KeyDeserializer {
      @Override
      public Object deserializeKey(String s, DeserializationContext deserializationContext) {
        log.debug("Deserializing key: {}", s);
        return Journey.JourneyId.from(s);
      }
    }
}
