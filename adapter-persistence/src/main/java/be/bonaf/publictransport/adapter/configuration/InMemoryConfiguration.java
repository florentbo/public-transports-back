package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.domain.journey.Journey;
import be.bonaf.publictransport.domain.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.*;
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

      private InputStream getFileContent(String url) {
        try (var client = HttpClient.newBuilder().build()) {
          HttpRequest request =
              HttpRequest.newBuilder()
                  .uri(URI.create(url))
                  .header("User-Agent", "Java 21 Client")
                  .GET()
                  .build();

          return client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body();
        } catch (IOException | InterruptedException e) {
          throw new RuntimeException("Failed to fetch file content from " + url, e);
        }
      }

      @Override
      public Journey journey(Journey.JourneyId journeyId) {
        return getJourneys().get(journeyId);
      }

      private Map<Journey.JourneyId, Journey> getJourneys() {
        String url = "https://api.jsonbin.io/v3/b/6962de5143b1c97be9279557/latest";
        return JourneyReader.readValue(getFileContent(url));
      }
    };
  }
}
