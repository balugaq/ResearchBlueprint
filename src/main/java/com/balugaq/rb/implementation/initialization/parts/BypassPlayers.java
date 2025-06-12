package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.DefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.Parsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class BypassPlayers implements DefaultValue<BypassPlayers>, Parsable {
    static final BypassPlayers DEFAULT = new BypassPlayers(List.of());

    @Key("names")
    List<String> names;

    public static String[] fieldNames() {
        return Parsable.fieldNames(BypassPlayers.class);
    }

    @Override
    public BypassPlayers defaultValue() {
        return DEFAULT;
    }
}
