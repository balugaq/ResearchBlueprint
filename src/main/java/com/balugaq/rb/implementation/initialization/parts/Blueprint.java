package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.IDefaultValue;
import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
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
public class Blueprint implements IDefaultValue<Blueprint>, IParsable {
    static final Blueprint DEFAULT = new Blueprint(Material.KNOWLEDGE_BOOK, "Research Blueprint", List.of(), 0);

    public Blueprint(Material material, String name, List<String> lore, int customModelData) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
    }

    ItemStack item = null;

    @Key("material")
    Material material;

    @Key("name")
    String name;

    @Key("lore")
    List<String> lore;

    @Key("custom-model-data")
    int customModelData;

    public static String[] fieldNames() {
        return IParsable.fieldNames(Blueprint.class);
    }

    public static Blueprint defaultValue0() {
        return DEFAULT;
    }

    @Override
    public Blueprint defaultValue() {
        return defaultValue0();
    }

    public ItemStack getItemStack() {
        if (item != null) {
            return item;
        }

        item = new CustomItemStack(material, name, lore).setCustomModel(customModelData);
        return item;
    }
}
