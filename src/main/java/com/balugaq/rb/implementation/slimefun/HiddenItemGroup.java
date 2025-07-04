package com.balugaq.rb.implementation.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class HiddenItemGroup extends ItemGroup {
    public HiddenItemGroup(NamespacedKey key, ItemStack item) {
        super(key, item);
        this.setTier(Integer.MIN_VALUE + 1);
    }

    public boolean isVisible(@Nonnull Player p) {
        return true;
    }
}
