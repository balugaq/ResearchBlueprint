package com.balugaq.rb.implementation.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class HiddenItemGroup extends ItemGroup {
    public HiddenItemGroup(NamespacedKey key, ItemStack item) {
        super(key, item);
    }

    public boolean isVisible(@Nonnull Player p) {
        return p.isOp();
    }
}
