package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.IDefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import com.balugaq.rb.api.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Permissions implements IDefaultValue<Permissions>, IParsable {
    static final Permissions DEFAULT = new Permissions(Type.EXCLUDE_ANY, List.of("AVeryVeryLongLongLongStringToAvoidAnyPlayerNameMatchIt"));
    @Required
    @Key("type")
    Type type;

    @Key("nodes")
    List<String> nodes;

    public static String[] fieldNames() {
        return IParsable.fieldNames(Permissions.class);
    }

    public static Permissions defaultValue0() {
        return DEFAULT;
    }

    @Override
    public Permissions defaultValue() {
        return defaultValue0();
    }

    public enum Type {
        INCLUDE_ANY,
        INCLUDE_ALL,
        EXCLUDE_ANY,
        EXCLUDE_ALL
    }
}
