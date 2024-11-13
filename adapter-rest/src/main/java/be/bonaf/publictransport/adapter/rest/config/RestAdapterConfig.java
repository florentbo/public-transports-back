package be.bonaf.publictransport.adapter.rest.config;

import be.bonaf.JourneysController;
import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.Trip;
import be.bonaf.publictransport.domain.journey.Journey.Trip.TransportStation;
import be.bonaf.publictransport.domain.service.JourneyService;
import be.bonaf.publictransport.domain.user.UserId;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.UUID;

import static be.bonaf.publictransport.domain.journey.Journey.*;

@Configuration
@AllArgsConstructor
public class RestAdapterConfig {

  private final JourneyService journeyService;

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
    return new JourneysController(journeyService);
  }
}
