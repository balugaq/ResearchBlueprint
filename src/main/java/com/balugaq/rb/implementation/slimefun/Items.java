package com.balugaq.rb.implementation.slimefun;

import com.balugaq.rb.implementation.Keys;
import com.balugaq.rb.implementation.ResearchBlueprintPlugin;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;

public class Items {
    public static SlimefunItem unlockTicket = null;

    public static void setup() {
        unlockTicket = new ResearchUnlockTicket(
            new SlimefunItemStack("YUNAMING_RESEARCH_TOURUYA_IS_AI", Material.PAPER, "&d&l研究解锁券", "&7用于自定义解锁自己想要的研究", "&7点击未解锁研究优先消耗")
        );
        unlockTicket.register(ResearchBlueprintPlugin.getInstance());
    }
}
