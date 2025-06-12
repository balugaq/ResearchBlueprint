package com.balugaq.rb.implementation.slimefun;

import com.balugaq.rb.implementation.Keys;
import com.balugaq.rb.implementation.initialization.ResearchConfigurations;
import com.balugaq.rb.implementation.initialization.parts.ResearchConfiguration;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ResearchBlueprint extends SlimefunItem {
    public static final NamespacedKey IDENTIFIER_KEY = Keys.newKey("identifier");
    public ResearchBlueprint(SlimefunItemStack item) {
        super(Groups.hidden, item, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                null, null, null,
                null ,null ,null
        });

        addItemHandler((ItemUseHandler) event -> {
            ResearchHandler.handleResearch(event, getIdentified(event.getItem()));
        });
    }

    public static ResearchConfiguration getIdentified(ItemStack itemStack) {
        return getIdentified(itemStack.getItemMeta().getPersistentDataContainer().get(IDENTIFIER_KEY, PersistentDataType.STRING));
    }

    public static ResearchConfiguration getIdentified(String identifier) {
        return ResearchConfigurations.byIdentifier.get(identifier);
    }
}
