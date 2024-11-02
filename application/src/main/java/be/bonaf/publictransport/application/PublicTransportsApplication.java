package be.bonaf.publictransport.application;

import be.bonaf.publictransport.adapter.rest.config.RestAdapterConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
  RestAdapterConfig.class,
  // Add other adapter configs as needed
})
@Log4j2
public class PublicTransportsApplication {
  public static void main(String[] args) {
    SpringApplication.run(PublicTransportsApplication.class, args);
  }
}
