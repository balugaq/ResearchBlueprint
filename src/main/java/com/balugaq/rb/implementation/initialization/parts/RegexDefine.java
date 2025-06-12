package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.implementation.cfgparse.annotations.Key;
import com.balugaq.rb.implementation.cfgparse.annotations.Parsable;
import com.balugaq.rb.implementation.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class RegexDefine implements Parsable {
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
        return Parsable.fieldNames(RegexDefine.class);
    }
}
