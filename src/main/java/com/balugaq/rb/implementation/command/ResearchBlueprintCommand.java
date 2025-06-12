package com.balugaq.rb.implementation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchBlueprintCommand implements TabExecutor {
    public static final Map<String, ItemStack> blueprints = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage("You do not have permission to use this command.");
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by a player.");
            return false;
        }

        if (strings.length == 0) {
            commandSender.sendMessage("Usage: /rb <blueprint>");
            return false;
        }

        ItemStack blueprint = blueprints.get(strings[0]);
        player.getInventory().addItem(blueprint);
        player.sendMessage("You have received a blueprint for " + strings[0] + ".");
        player.updateInventory();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return blueprints.keySet().stream().sorted().toList();
    }
}
