package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.Parsable;
import com.balugaq.rb.api.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class ItemsDefine implements Parsable {
    @Required
    @Key("scope")
    Scope scope;

    @Required
    @Key("value")
    List<String> values;

    public static String[] fieldNames() {
        return Parsable.fieldNames(ItemsDefine.class);
    }
}
