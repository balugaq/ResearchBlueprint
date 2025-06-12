package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.implementation.cfgparse.annotations.DefaultValue;
import com.balugaq.rb.implementation.cfgparse.annotations.Key;
import com.balugaq.rb.implementation.cfgparse.annotations.Parsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class BindSubcommands implements DefaultValue<BindSubcommands>, Parsable {
    static final BindSubcommands DEFAULT = new BindSubcommands(List.of());

    @Key("subcommands")
    List<String> subcommands;

    public static String[] fieldNames() {
        return Parsable.fieldNames(BindSubcommands.class);
    }

    @Override
    public BindSubcommands defaultValue() {
        return DEFAULT;
    }
}
