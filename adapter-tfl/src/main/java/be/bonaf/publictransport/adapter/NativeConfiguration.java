package be.bonaf.publictransport.adapter;


import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;


public class NativeConfiguration implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        ReflectionConfiguration reflectionConfiguration = new ReflectionConfiguration();
        reflectionConfiguration.registerPackageClasses(hints, "org.openapitools.client.model");
    }
}
