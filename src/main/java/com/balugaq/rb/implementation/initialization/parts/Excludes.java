package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.IDefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Excludes implements IParsable, IDefaultValue<Excludes> {
    @Key("value")
    List<String> value;

    public static String[] fieldNames() {
        return IParsable.fieldNames(Excludes.class);
    }

    static final Excludes DEFAULT = new Excludes(List.of());

    public static Excludes defaultValue0() {
        return DEFAULT;
    }

    @Override
    public Excludes defaultValue() {
        return defaultValue0();
    }
}
