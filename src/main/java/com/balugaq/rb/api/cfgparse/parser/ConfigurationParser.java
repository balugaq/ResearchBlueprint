package com.balugaq.rb.api.cfgparse.parser;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@UtilityClass
@ApiStatus.Obsolete
public class ConfigurationParser {
    @ApiStatus.Obsolete
    @ParametersAreNonnullByDefault
    @SneakyThrows
    public static <T> T parse(ConfigurationSection section, Class<T> clazz) {
        Method method;
        try {
            method = clazz.getDeclaredMethod("fieldNames");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No fieldNames method found in " + clazz.getName(), e);
        } catch (SecurityException e) {
            throw new RuntimeException("SecurityException while getting fieldNames method from " + clazz.getName(), e);
        }
        method.setAccessible(true);
        String[] fieldNames;
        try {
            fieldNames = (String[]) method.invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Error while invoking fieldNames method from " + clazz.getName(), e);
        }

        List<String> list = List.of(fieldNames);
        LinkedHashMap<Field, Object> read = new LinkedHashMap<>();
        for (var field : clazz.getDeclaredFields()) {
            if (list.contains(field.getName())) {
                if (field.isAnnotationPresent(Key.class)) {
                    Key define = field.getAnnotation(Key.class);
                    String key = define.value();
                    if (Key.ALL_KEY.equals(key)) {
                        Set<String> subkKys = section.getKeys(false);
                        List<Object> arg = new ArrayList<>();
                        for (var subKey : subkKys) {
                            if (List.class.isAssignableFrom(field.getType()) && field.getType().getTypeParameters().length > 0 && field.getGenericType() instanceof ParameterizedType parameterizedType && parameterizedType.getActualTypeArguments()[0] instanceof Class<?> genericType) {
                                var value = parseValue(genericType, section.get(subKey));
                                arg.add(value);
                            } else {
                                var value = parseValue(field.getType(), section.get(subKey));
                                arg.add(value);
                            }
                        }
                        read.put(field, arg);
                    } else {
                        if (!List.class.isAssignableFrom(field.getType()) && field.getType().getTypeParameters().length > 0 && field.getGenericType() instanceof ParameterizedType parameterizedType && parameterizedType.getActualTypeArguments()[0] instanceof Class<?> genericType) {
                            var value = parseValue(genericType, section.get(key));
                            read.put(field, value);
                        } else {
                            var value = parseValue(field.getType(), section.get(key));
                            read.put(field, value);
                        }
                    }
                }
            }
        }

        return consturctObject(clazz, read);
    }

    @ApiStatus.Obsolete
    @ParametersAreNonnullByDefault
    @SneakyThrows
    public static <T> T consturctObject(Class<T> clazz, LinkedHashMap<Field, Object> read) {
        Class<?>[] types = read.keySet().stream().map(Field::getType).toArray(Class<?>[]::new);
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(types);
            Object[] sortedValues = new Object[types.length];

            for (int i = 0; i < types.length; i++) {
                var value = read.get(read.keySet().toArray()[i]);
                sortedValues[i] = value;
            }

            return constructor.newInstance(sortedValues);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No constructor found in " + clazz.getName(), e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while instantiating " + clazz.getName(), e);
        }
    }

    @ApiStatus.Obsolete
    @SneakyThrows
    public static <T> T parseValue(@NotNull Class<T> clazz, @Nullable Object value) {
        if (value == null) {
            return null;
        }

        if (clazz == int.class) {
            return (T) Integer.valueOf(value.toString());
        }

        if (clazz == long.class) {
            return (T) Long.valueOf(value.toString());
        }

        if (clazz == short.class) {
            return (T) Short.valueOf(value.toString());
        }

        if (clazz == char.class) {
            return (T) Character.valueOf(value.toString().charAt(0));
        }

        if (clazz == byte.class) {
            return (T) Byte.valueOf(value.toString());
        }

        if (clazz == float.class) {
            return (T) Float.valueOf(value.toString());
        }

        if (clazz == double.class) {
            return (T) Double.valueOf(value.toString());
        }

        if (clazz == boolean.class) {
            return (T) Boolean.valueOf(value.toString());
        }

        if (clazz == String.class) {
            return (T) value.toString();
        }

        if (clazz.isEnum()) {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz;
            try{
                return (T) Enum.valueOf(enumClass, value.toString().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Cannot find enum value in " + clazz.getName() + ": " + value, e);
            }
        }

        if (clazz.isArray()) {
            return (T) value;
        }

        if (value instanceof ConfigurationSection section) {
            return parse(section, clazz);
        }

        // Fallback
        return (T) value;
    }
}
