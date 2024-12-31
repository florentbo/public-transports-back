package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.domain.journey.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.*;

import java.util.List;

@Configuration
@AllArgsConstructor
public class BruConfiguration {

  @Bean
  @BruArrivalTimeService
  public ArrivalTimeService bruArrivalTimeService() {
    return journey -> List.of();
  }
}
