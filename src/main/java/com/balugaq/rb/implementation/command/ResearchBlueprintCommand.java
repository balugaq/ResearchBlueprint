package com.balugaq.rb.implementation.command;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import org.bukkit.Bukkit;
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
import java.util.stream.Collectors;

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

        if (strings[0].equals("1")) {
            // test
            String s1;
            var var = PlayerProfile.find((Player)commandSender).get();
            s1 = var.getResearches().stream().map(Research::getUnlocalizedName).collect(Collectors.joining(", "));
            Bukkit.getConsoleSender().sendMessage(s1);
            return true;
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
