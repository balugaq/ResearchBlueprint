package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.DefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.Parsable;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class Blueprint implements DefaultValue<Blueprint>, Parsable {
    static final Blueprint DEFAULT = new Blueprint(Material.KNOWLEDGE_BOOK, "Research Blueprint", List.of());

    public Blueprint(Material material, String name, List<String> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    ItemStack item = null;

    @Key("material")
    Material material;

    @Key("name")
    String name;

    @Key("lore")
    List<String> lore;

    public static String[] fieldNames() {
        return Parsable.fieldNames(Blueprint.class);
    }

    @Override
    public Blueprint defaultValue() {
        return DEFAULT;
    }

    public ItemStack getItemStack() {
        if (item != null) {
            return item;
        }

        item = new CustomItemStack(material, name, lore);
        return item;
    }
}
