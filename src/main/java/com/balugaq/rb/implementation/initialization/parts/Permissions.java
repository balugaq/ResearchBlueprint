package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.implementation.cfgparse.annotations.DefaultValue;
import com.balugaq.rb.implementation.cfgparse.annotations.Key;
import com.balugaq.rb.implementation.cfgparse.annotations.Parsable;
import com.balugaq.rb.implementation.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class Permissions implements DefaultValue<Permissions>, Parsable {
    static final Permissions DEFAULT = new Permissions(Type.EXCLUDE_ANY, List.of());
    @Required
    @Key("type")
    Type type;

    @Key("nodes")
    List<String> nodes;

    public static String[] fieldNames() {
        return Parsable.fieldNames(Permissions.class);
    }

    @Override
    public Permissions defaultValue() {
        return DEFAULT;
    }

    enum Type {
        INCLUDE_ANY,
        INCLUDE_ALL,
        EXCLUDE_ANY,
        EXCLUDE_ALL
    }
}
