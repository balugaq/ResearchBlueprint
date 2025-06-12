package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import com.balugaq.rb.api.cfgparse.annotations.Required;
import com.balugaq.rb.implementation.slimefun.ResearchBlueprint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.PackagePrivate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@AllArgsConstructor
@ToString
@Data
public class ResearchConfiguration implements IParsable {
    public ResearchConfiguration(String identifier, boolean enabled, ResearchType researchType, Blueprint blueprint, BypassPlayers bypassPlayers, Permissions permissions, BindSubcommands bindSubcommands, BindItems bindItems) {
        this.identifier = identifier;
        this.enabled = enabled;
        this.researchType = researchType;
        this.blueprint = blueprint;
        this.bypassPlayers = bypassPlayers;
        this.permissions = permissions;
        this.bindSubcommands = bindSubcommands;
        this.bindItems = bindItems;
    }

    @PackagePrivate
    ItemStack item = null;

    @PackagePrivate
    @Getter
    @Setter
    ResearchBlueprint instance = null;

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
        return IParsable.fieldNames(ResearchConfiguration.class);
    }

    public ItemStack icon() {
        if (this.item != null) {
            return this.item;
        }

        ItemStack icon = blueprint.getItemStack();
        var m = icon.getItemMeta();
        m.getPersistentDataContainer().set(ResearchBlueprint.IDENTIFIER_KEY, PersistentDataType.STRING, identifier);
        icon.setItemMeta(m);
        this.item = icon;
        return icon;
    }

}
