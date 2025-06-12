package com.balugaq.rb.implementation.initialization;

import com.balugaq.rb.implementation.ResearchBlueprintPlugin;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunItemRegistryFinalizedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomResearchBlueprintApplier implements Listener {
    @EventHandler
    public void load(SlimefunItemRegistryFinalizedEvent event) {
        ResearchConfigurations configurations = ResearchBlueprintPlugin.getInstance().getConfigurations();

        // todo
    }
}
