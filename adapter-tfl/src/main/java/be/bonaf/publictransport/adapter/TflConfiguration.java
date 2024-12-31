package be.bonaf.publictransport.adapter;

import be.bonaf.publictransport.adapter.arrivals.ArrivalTimeClient;
import be.bonaf.publictransport.adapter.tfl.TflConnector;
import be.bonaf.publictransport.domain.journey.ArrivalTimeService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@AllArgsConstructor
@ImportRuntimeHints(NativeConfiguration.class)
public class TflConfiguration {

  @Bean
  @LondonArrivalTimeService
  public ArrivalTimeService londonArrivalTimeService() {
    return new ArrivalTimeClient(new TflConnector("https://api.tfl.gov.uk"));
  }
}
