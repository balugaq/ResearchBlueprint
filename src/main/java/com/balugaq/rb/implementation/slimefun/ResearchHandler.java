package com.balugaq.rb.implementation.slimefun;

import com.balugaq.rb.implementation.initialization.parts.ResearchConfiguration;
import com.balugaq.rb.implementation.initialization.parts.ResearchType;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

@UtilityClass
public class ResearchHandler {
    public static void handleResearch(PlayerRightClickEvent event, ResearchConfiguration configuration) {
        Player player = event.getPlayer();

        if (research(player, configuration)) {
            consumeBlueprint(player, event.getItem());
        }
    }

    public static boolean research(Player player, ResearchConfiguration configuration) {
        var profile = PlayerProfile.find(player).orElse(null);
        if (profile == null) {
            return false;
        }

        Set<Research> researches = CustomResearchBlueprintApplier.researches.get(configuration.getIdentifier());
        var type = configuration.getResearchType();
        if (type == ResearchType.RESEARCH_ALL) {
            CustomResearchBlueprintApplier.researchAll(player, configuration);
            return true;
        } else if (type == ResearchType.RESEARCH_ANY) {
            for (Research research : researches) {
                if (profile.getResearches().contains(research)) {
                    continue;
                }

                research.unlock(player, false);
                return true;
            }
        }

        return false;
    }

    public static void consumeBlueprint(Player player, ItemStack blueprint) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        blueprint.setAmount(blueprint.getAmount() - 1);
        player.updateInventory();
    }
}
