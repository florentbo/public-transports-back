package be.bonaf.publictransport.adapter.configuration;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class NativeConfiguration implements RuntimeHintsRegistrar {
  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    register(hints, JourneyReader.JourneyIdKeyDeserializer.class);
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
