package be.bonaf.publictransport.adapter.configuration;

import be.bonaf.publictransport.adapter.stib.client.WaitingTimes;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class NativeConfiguration implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        register(hints, WaitingTimes.class);
        register(hints, WaitingTimes.Result.class);
        register(hints, WaitingTimes.Result.PassingTime.class);
        register(hints, WaitingTimes.Result.PassingTime.Destination.class);
        register(hints, WaitingTimes.Result.PassingTime.Message.class);
        register(hints, WaitingTimes.PassingTimesDeserializer.class);
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
