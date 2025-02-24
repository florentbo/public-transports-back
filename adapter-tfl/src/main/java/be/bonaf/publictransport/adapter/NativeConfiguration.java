package be.bonaf.publictransport.adapter;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.openapitools.client.model.*;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.lang.Nullable;

import java.util.List;

public class NativeConfiguration implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(@Nullable RuntimeHints hints, ClassLoader classLoader) {
        List<Class<?>> classes = List.of(
                TflApiPresentationEntitiesArrivalDeparture.class,
                TflApiPresentationEntitiesPrediction.class,
                TflApiPresentationEntitiesPredictionTiming.class);
        registerHints(hints, classes );
    }

    void registerHints(@Nullable RuntimeHints hints, List<Class<?>> classes) {
        classes.forEach(clazz -> register(hints, clazz));
    }

    private void register(@Nullable RuntimeHints hints, Class<?> type) {
        assert hints != null;
        hints.reflection().registerType(
                type,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
                MemberCategory.DECLARED_FIELDS
        );
    }
}
