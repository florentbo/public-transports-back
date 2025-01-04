package be.bonaf.publictransport.domain;


import be.bonaf.publictransport.domain.journey.*;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class DomainNativeConfiguration implements RuntimeHintsRegistrar {
  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    register(hints, Journey.class);
    register(hints, Journey.JourneyId.class);
    register(hints, Journey.TransportType.class);
    register(hints, Journey.City.class);
    register(hints, Location.class);
    register(hints, LocationId.class);
    register(hints, Journey.Trip.class);
    register(hints, Journey.Trip.TransportStation.class);
  }

  private void register(RuntimeHints hints, Class<?> type) {
    hints
        .reflection()
        .registerType(
            type,
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_METHODS,
            MemberCategory.DECLARED_FIELDS);
  }
}
