package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import com.balugaq.rb.api.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class RegexDefine implements IParsable {
    @Required
    @Key("scope-type")
    ScopeType scopeType;

    @Required
    @Key("scope")
    Scope scope;

    @Required
    @Key("value")
    String value;

    public static String[] fieldNames() {
        return IParsable.fieldNames(RegexDefine.class);
    }
}
