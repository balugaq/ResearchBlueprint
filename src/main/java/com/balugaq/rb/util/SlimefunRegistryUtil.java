package com.balugaq.rb.util;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class SlimefunRegistryUtil {
    public static @NotNull SlimefunItem registerItem(@NotNull SlimefunItem item, @NotNull SlimefunAddon addon) {
        item.register(addon);
        return item;
    }

    public static void unregisterItems(@NotNull SlimefunAddon addon) {
        List<SlimefunItem> copy = new ArrayList<>(Slimefun.getRegistry().getAllSlimefunItems());
        for (SlimefunItem item : copy) {
            if (item.getAddon().equals(addon)) {
                unregisterItem(item);
            }
        }
    }

    public static void unregisterItem(@NotNull SlimefunItem item) {
        if (item == null) {
            return;
        }

        if (item instanceof Radioactive) {
            synchronized (Slimefun.getRegistry().getRadioactiveItems()) {
                Slimefun.getRegistry().getRadioactiveItems().remove(item);
            }
        }

        if (item instanceof GEOResource geor) {
            synchronized (Slimefun.getRegistry().getGEOResources()) {
                Slimefun.getRegistry().getGEOResources().remove(geor.getKey());
            }
        }

        synchronized (Slimefun.getRegistry().getTickerBlocks()) {
            Slimefun.getRegistry().getTickerBlocks().remove(item.getId());
        }
        synchronized (Slimefun.getRegistry().getEnabledSlimefunItems()) {
            Slimefun.getRegistry().getEnabledSlimefunItems().remove(item);
        }

        synchronized (Slimefun.getRegistry().getSlimefunItemIds()) {
            Slimefun.getRegistry().getSlimefunItemIds().remove(item.getId());
        }
        synchronized (Slimefun.getRegistry().getAllSlimefunItems()) {
            Slimefun.getRegistry().getAllSlimefunItems().remove(item);
        }
        synchronized (Slimefun.getRegistry().getMenuPresets()) {
            Slimefun.getRegistry().getMenuPresets().remove(item.getId());
        }
        synchronized (Slimefun.getRegistry().getBarteringDrops()) {
            Slimefun.getRegistry().getBarteringDrops().remove(item.getItem());
        }
    }

    public static void unregisterItemGroups(@NotNull SlimefunAddon addon) {
        List<ItemGroup> copy;
        synchronized (Slimefun.getRegistry().getAllItemGroups()) {
            copy = new ArrayList<>(Slimefun.getRegistry().getAllItemGroups());
        }
        for (ItemGroup itemGroup : copy) {
            if (Objects.equals(itemGroup.getAddon(), addon)) {
                unregisterItemGroup(itemGroup);
            }
        }
    }

    public static void unregisterItemGroup(@NotNull ItemGroup itemGroup) {
        if (itemGroup == null) {
            return;
        }

        synchronized (Slimefun.getRegistry().getAllItemGroups()) {
            Slimefun.getRegistry().getAllItemGroups().remove(itemGroup);
        }
    }
}
