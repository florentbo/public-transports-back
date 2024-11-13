package be.bonaf.publictransport.adapter;

import be.bonaf.publictransport.adapter.arrivals.ArrivalTimeClient;
import be.bonaf.publictransport.adapter.tfl.TflConnector;
import be.bonaf.publictransport.domain.journey.ArrivalTimeService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class TflConfiguration {

  @Bean
  public ArrivalTimeService arrivalTimeService() {
    return new ArrivalTimeClient(new TflConnector("https://api.tfl.gov.uk"));
  }
}
