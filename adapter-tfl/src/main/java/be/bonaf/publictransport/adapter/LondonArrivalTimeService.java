package be.bonaf.publictransport.adapter;

import java.lang.annotation.*;
import org.springframework.beans.factory.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface LondonArrivalTimeService {}
