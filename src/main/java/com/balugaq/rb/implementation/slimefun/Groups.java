package com.balugaq.rb.implementation.slimefun;

import com.balugaq.rb.implementation.Keys;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;

public class Groups {
    public static ItemGroup hidden = null;

    public static void setup() {
        hidden = new HiddenItemGroup(
                Keys.newKey("hidden"),
                new CustomItemStack(
                        Material.BLUE_DYE,
                        "&7Hidden"
                )
        );
    }
}
