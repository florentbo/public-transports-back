package be.bonaf.publictransport.application;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class PublicTransportsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PublicTransportsApplication.class, args);
  }
}
