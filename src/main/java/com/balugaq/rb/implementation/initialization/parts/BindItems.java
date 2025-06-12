package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.implementation.cfgparse.annotations.Key;
import com.balugaq.rb.implementation.cfgparse.annotations.Parsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class BindItems implements Parsable {
    @Key("regex")
    Regex regex;

    @Key("items")
    Items items;

    public static String[] fieldNames() {
        return Parsable.fieldNames(BindItems.class);
    }
}
