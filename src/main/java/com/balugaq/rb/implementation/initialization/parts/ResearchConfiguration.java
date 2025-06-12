package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.Parsable;
import com.balugaq.rb.api.cfgparse.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class ResearchConfiguration implements Parsable {
    @Key("identifier")
    String identifier;

    @Key("enabled")
    boolean enabled;

    @Required
    @Key("research-type")
    ResearchType researchType;

    @Key("blueprint")
    Blueprint blueprint;

    @Key("bypass-players")
    BypassPlayers bypassPlayers;

    @Key("permissions")
    Permissions permissions;

    @Key("bind-subcommands")
    BindSubcommands bindSubcommands;

    @Key("bind-items")
    BindItems bindItems;

    public static String[] fieldNames() {
        return Parsable.fieldNames(ResearchConfiguration.class);
    }

}
