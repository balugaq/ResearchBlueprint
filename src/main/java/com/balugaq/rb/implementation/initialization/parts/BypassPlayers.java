package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.IDefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class BypassPlayers implements IDefaultValue<BypassPlayers>, IParsable {
    static final BypassPlayers DEFAULT = new BypassPlayers(List.of());

    @Key("names")
    List<String> names;

    public static String[] fieldNames() {
        return IParsable.fieldNames(BypassPlayers.class);
    }

    public static BypassPlayers defaultValue0() {
        return DEFAULT;
    }

    @Override
    public BypassPlayers defaultValue() {
        return defaultValue0();
    }
}
