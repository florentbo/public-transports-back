// NativeConfiguration.java
package be.bonaf.publictransport.adapter;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.openapitools.client.model.*;
import org.springframework.aot.hint.MemberCategory;

public class NativeConfiguration implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        register(hints, TflApiPresentationEntitiesArrivalDeparture.class);
        register(hints, TflApiPresentationEntitiesPredictionTiming.class);
    }

    private void register(RuntimeHints hints, Class<?> type) {
        hints.reflection().registerType(
                type,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
                MemberCategory.DECLARED_FIELDS
        );
    }
}
