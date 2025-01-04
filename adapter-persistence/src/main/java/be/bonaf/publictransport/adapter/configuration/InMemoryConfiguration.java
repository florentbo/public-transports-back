package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.domain.journey.Journey;
import be.bonaf.publictransport.domain.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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

        HttpClient client =
            HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build()  ;

          HttpRequest request =
              HttpRequest.newBuilder()
                  .uri(URI.create(url))
                  .timeout(Duration.ofSeconds(10))
                  .header("User-Agent", "Java 21 Client")
                  .GET()
                  .build();

          try {
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
          // http client to get file content:

          String url1 = "https://verdant-blini-222724.netlify.app/journeys.json";

          return JourneyReader.readValue(getFileContent(url1));

      }
    };
  }
}
