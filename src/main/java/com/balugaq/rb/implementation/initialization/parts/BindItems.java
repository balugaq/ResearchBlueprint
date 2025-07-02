package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class BindItems implements IParsable {
    @Key("regex")
    Regex regex;

    @Key("items")
    Items items;

    @Key("excludes")
    Excludes excludes;

    public static String[] fieldNames() {
        return IParsable.fieldNames(BindItems.class);
    }
}
