package com.balugaq.rb.implementation;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(ResearchBlueprintPlugin.getInstance(), key);
    }
}
