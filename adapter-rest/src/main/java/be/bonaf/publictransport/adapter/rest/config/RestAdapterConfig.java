package be.bonaf.publictransport.adapter.rest.config;

import be.bonaf.JourneysController;
import be.bonaf.publictransport.service.JourneyService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
