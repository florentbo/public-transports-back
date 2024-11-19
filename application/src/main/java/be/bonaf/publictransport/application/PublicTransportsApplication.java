package be.bonaf.publictransport.application;

import be.bonaf.publictransport.adapter.rest.config.RestAdapterConfig;
import be.bonaf.publictransport.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
@Import({
  RestAdapterConfig.class,
  ServiceConfiguration.class,
})
public class PublicTransportsApplication {
  public static void main(String[] args) {
    SpringApplication.run(PublicTransportsApplication.class, args);
  }
}
