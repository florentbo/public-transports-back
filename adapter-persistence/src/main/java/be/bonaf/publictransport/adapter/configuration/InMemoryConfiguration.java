package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.domain.journey.Journey;
import be.bonaf.publictransport.domain.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
@ImportRuntimeHints(NativeConfiguration.class)
@Slf4j
public class InMemoryConfiguration {

  @Bean
  UserRepository userRepository() {
    return new UserRepository() {
      @Override
      public User getCurrentUser() {
        return null;
      }

      @Override
      public List<Journey> journeys() {
        return getJourneys().values().stream().toList();
      }

      @Override
      public Journey journey(Journey.JourneyId journeyId) {
        return getJourneys().get(journeyId);
      }

      private Map<Journey.JourneyId, Journey> getJourneys() {
        try {
          URL url = new URL("https://static.staticsave.com/journeys/journeys.json");
          try (var stream = url.openStream()) {
            return JourneyReader.readValue(stream);
          }
        } catch (MalformedURLException e) {
          log.error("Error while reading url: ", e);
          throw new RuntimeException(e);
        } catch (IOException e) {
          log.error("Error while opening stream: ", e);
          throw new RuntimeException(e);
        }
      }
    };
  }
}
