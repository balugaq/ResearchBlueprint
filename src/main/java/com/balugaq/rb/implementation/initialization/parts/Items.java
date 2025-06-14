package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.IDefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class Items implements IParsable, IDefaultValue<Items> {
    @Key(Key.ALL_KEY)
    List<ItemsDefine> defines;

    public static String[] fieldNames() {
        return IParsable.fieldNames(Items.class);
    }

    static final Items DEFAULT = new Items(List.of());

    public static Items defaultValue0() {
        return DEFAULT;
    }

    @Override
    public Items defaultValue() {
        return defaultValue0();
    }
}
