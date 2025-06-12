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
public class BindSubcommands implements IDefaultValue<BindSubcommands>, IParsable {
    static final BindSubcommands DEFAULT = new BindSubcommands(List.of());

    @Key("subcommands")
    List<String> subcommands;

    public static String[] fieldNames() {
        return IParsable.fieldNames(BindSubcommands.class);
    }

    @Override
    public BindSubcommands defaultValue() {
        return DEFAULT;
    }
}
