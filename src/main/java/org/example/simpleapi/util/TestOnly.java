package org.example.simpleapi.util;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({METHOD,CONSTRUCTOR})
public @interface TestOnly {
}
