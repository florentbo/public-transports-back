package be.bonaf.publictransport.adapter;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;

public class ReflectionConfiguration {

    public void registerPackageClasses(RuntimeHints hints, String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(s -> true));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        for (Class<?> clazz : classes) {
            hints.reflection().registerType(
                    clazz,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS
            );
        }
    }
}
