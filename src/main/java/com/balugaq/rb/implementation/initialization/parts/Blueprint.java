package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.implementation.cfgparse.annotations.DefaultValue;
import com.balugaq.rb.implementation.cfgparse.annotations.Key;
import com.balugaq.rb.implementation.cfgparse.annotations.Parsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class Blueprint implements DefaultValue<Blueprint>, Parsable {
    static final Blueprint DEFAULT = new Blueprint(Material.KNOWLEDGE_BOOK, "Research Blueprint", List.of());

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
}
