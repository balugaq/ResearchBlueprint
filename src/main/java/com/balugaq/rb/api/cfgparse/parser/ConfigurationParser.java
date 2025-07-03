package com.balugaq.rb.api.cfgparse.parser;

import com.balugaq.rb.api.cfgparse.annotations.IDefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import io.github.thebusybiscuit.slimefun4.libraries.dough.reflection.ReflectionUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Array;
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

@SuppressWarnings({"unchecked", "unused"})
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
                        Set<String> subKeys = section.getKeys(false);
                        List<Object> arg = new ArrayList<>();
                        for (var subKey : subKeys) {
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
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();

            T object = constructor.newInstance();
            // find setter
            for (var entry : read.entrySet()) {
                // when field.getName() is "query"
                // setterName: setQuery
                // setterName2: query
                Field field = entry.getKey();
                String setterName = "set" + field.getName().substring(0, 1).toUpperCase(Locale.ROOT) + field.getName().substring(1);
                Method setter = ReflectionUtils.getMethod(clazz, setterName, field.getType());
                if (setter != null) {
                    Object arg = entry.getValue();
                    setter.invoke(object, arg);
                } else {
                    String setterName2 = field.getName();
                    setter = ReflectionUtils.getMethod(clazz, setterName2, field.getType());
                    if (setter != null) {
                        Object arg = entry.getValue();
                        setter.invoke(object, arg);
                    } else {
                        throw new RuntimeException("No setter found for " + field.getName() + " in " + clazz.getName());
                    }
                }
            }

            return object;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No constructor found in " + clazz.getName(), e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while instantiating " + clazz.getName(), e);
        }
    }

    @SuppressWarnings("rawtypes")
    @ApiStatus.Obsolete
    @SneakyThrows
    public static <T> T parseValue(@NotNull Class<T> clazz, @Nullable Object value) {
        if (value == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                if (interfaceClass == IDefaultValue.class) {
                    Method defaultValueMethod = clazz.getDeclaredMethod("defaultValue0");
                    return (T) defaultValueMethod.invoke(null);
                }
            }
        }

        if (clazz == int.class) {
            if (value == null) {
                return (T) Integer.valueOf(0);
            }
            return (T) Integer.valueOf(value.toString());
        }

        if (clazz == long.class) {
            if (value == null) {
                return (T) Long.valueOf(0);
            }
            return (T) Long.valueOf(value.toString());
        }

        if (clazz == short.class) {
            if (value == null) {
                return (T) Short.valueOf((short) 0);
            }
            return (T) Short.valueOf(value.toString());
        }

        if (clazz == char.class) {
            if (value == null) {
                return (T) Character.valueOf('\0');
            }
            return (T) Character.valueOf(value.toString().charAt(0));
        }

        if (clazz == byte.class) {
            if (value == null) {
                return (T) Byte.valueOf((byte) 0);
            }
            return (T) Byte.valueOf(value.toString());
        }

        if (clazz == float.class) {
            if (value == null) {
                return (T) Float.valueOf(0);
            }
            return (T) Float.valueOf(value.toString());
        }

        if (clazz == double.class) {
            if (value == null) {
                return (T) Double.valueOf(0);
            }
            return (T) Double.valueOf(value.toString());
        }

        if (clazz == boolean.class) {
            if (value == null) {
                return (T) Boolean.valueOf(false);
            }
            return (T) Boolean.valueOf(value.toString());
        }

        if (clazz == String.class) {
            if (value == null) {
                return (T) "";
            }
            return (T) value.toString();
        }

        if (clazz.isEnum()) {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz;
            try{
                if (value != null) {
                    return (T) Enum.valueOf(enumClass, value.toString().toUpperCase(Locale.ROOT));
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Cannot find enum value in " + clazz.getName() + ": " + value, e);
            }
        }

        if (clazz.isArray()) {
            if (value == null) {
                return (T) Array.newInstance(clazz.getComponentType(), 0);
            }

            List<?> list = (List<?>) value;

            @SuppressWarnings("unchecked")
            T array = (T) list.toArray((Object[]) Array.newInstance(clazz.getComponentType(), 0));
            return array;
        }

        if (value instanceof ConfigurationSection section) {
            return parse(section, clazz);
        }

        // Fallback
        return (T) value;
    }
}