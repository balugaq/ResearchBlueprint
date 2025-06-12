package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import com.balugaq.rb.api.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class ItemsDefine implements IParsable {
    @Required
    @Key("scope")
    Scope scope;

    @Required
    @Key("value")
    List<String> values;

    public static String[] fieldNames() {
        return IParsable.fieldNames(ItemsDefine.class);
    }
}
