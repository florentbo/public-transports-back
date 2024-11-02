package be.bonaf.publictransport.adapter.rest.config;

import be.bonaf.JourneysController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class RestAdapterConfig {
    @Bean
    public JourneysController journeysController() {
        return new JourneysController();
    }
}
