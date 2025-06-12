package com.balugaq.rb.api.cfgparse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
    public static final String ALL_KEY = "ALL_KEY";
    String value() default "";
}
