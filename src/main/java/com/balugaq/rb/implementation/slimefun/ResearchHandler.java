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

        if(!hasPermission(player, configuration)) {
            return;
        }

        if (research(player, configuration)) {
            consumeBlueprint(player, event.getItem());
        }
    }

    public static boolean hasPermission(Player player, ResearchConfiguration configuration) {
        var permissions = configuration.getPermissions();
        var type = permissions.getType();
        var nodes = permissions.getNodes();
        if (nodes.isEmpty()) {
            return true;
        }
        if ("AVeryVeryLongLongLongStringToAvoidAnyPlayerNameMatchIt".equals(nodes.get(0))) {
            return true;
        }

        switch (type) {
            case EXCLUDE_ALL -> {
                for (var node : nodes) {
                    if (node.isBlank()) {
                        continue;
                    }
                    if (player.hasPermission(node)) {
                        return false;
                    }
                }
                return true;
            }
            case EXCLUDE_ANY -> {
                for (var node : nodes) {
                    if (node.isBlank()) {
                        continue;
                    }
                    if (!player.hasPermission(node)) {
                        return true;
                    }
                }

                return false;
            }
            case INCLUDE_ALL -> {
                for (var node : nodes) {
                    if (node.isBlank()) {
                        continue;
                    }
                    if (!player.hasPermission(node)) {
                        return false;
                    }
                }
                return true;
            }
            case INCLUDE_ANY -> {
                for (var node : nodes) {
                    if (node.isBlank()) {
                        continue;
                    }
                    if (player.hasPermission(node)) {
                        return true;
                    }
                }
                return false;
            }
        }

        return false;
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
