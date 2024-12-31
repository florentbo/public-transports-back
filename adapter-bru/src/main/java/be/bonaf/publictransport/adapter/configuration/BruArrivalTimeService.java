package be.bonaf.publictransport.adapter.configuration;

import org.springframework.beans.factory.annotation.*;

import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface BruArrivalTimeService {}
