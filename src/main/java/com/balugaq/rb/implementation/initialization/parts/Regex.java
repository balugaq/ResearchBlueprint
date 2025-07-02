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
public class Regex implements IParsable, IDefaultValue<Regex> {
    @Key(Key.ALL_KEY)
    List<RegexDefine> defines;

    public static String[] fieldNames() {
        return IParsable.fieldNames(Regex.class);
    }

    static final Regex DEFAULT = new Regex(List.of());

    public static Regex defaultValue0() {
        return DEFAULT;
    }

    @Override
    public Regex defaultValue() {
        return defaultValue0();
    }
}
