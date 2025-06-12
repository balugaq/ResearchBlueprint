package com.balugaq.rb.implementation.cfgparse.annotations;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface Parsable {
    @SneakyThrows
    static String[] fieldNames(Class<? extends Parsable> clazz) {
        try {
            clazz.getDeclaredConstructor(Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Key.class)).map(Field::getType).toArray(Class[]::new));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No valid constructor found in " + clazz.getName(), e);
        }

        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Key.class)).map(Field::getName).toArray(String[]::new);
    }
}
