package com.balugaq.rb.implementation.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerPreResearchEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.stream.IntStream;

public class ResearchUnlockTicket extends SlimefunItem implements Listener, CommandExecutor {
    public ResearchUnlockTicket(SlimefunItemStack item) {
        super(Groups.hidden, item, RecipeType.NULL, IntStream.of(9).mapToObj(i->(ItemStack)null).toArray(ItemStack[]::new));
    }

    public Plugin getPlugin(){
        return (Plugin) getAddon();
    }

    @Override
    public void register(@NotNull SlimefunAddon addon) {
        super.register(addon);
        Bukkit.getServer().getPluginManager().registerEvents(this, getPlugin());
        ((JavaPlugin)addon).getCommand("rbcallback").setExecutor(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPreresearch(PlayerPreResearchEvent event){
        boolean creativeResearch = event.getPlayer().getGameMode() == GameMode.CREATIVE && Slimefun.getConfigManager().isFreeCreativeResearchingEnabled();
        if(!creativeResearch && event.getResearch() != null){
            //check if any unlock ticket
            PlayerInventory inventory = event.getPlayer().getInventory();
            int size = inventory.getSize();
            for (int i = 0 ; i < size ; ++i){
                if(SlimefunItem.getByItem(inventory.getItem(i)) == this){
                    event.setCancelled(true);
                    unlockAsync(event.getPlayer(), event);
                }
            }
        }
    }
    private final Map<UUID, PlayerUnlockRequest> playerRequests = new HashMap<>();
    private final long RequestTimeoutMS = 120 * 1000;
    private final Random requestRand = new Random();
    @Data
    @AllArgsConstructor
    private static class PlayerUnlockRequest{
        long requestTime;
        String requestId;
        PlayerPreResearchEvent requestContent;
    }
    public void unlockAsync(Player p, PlayerPreResearchEvent event){
        //block any unlock request first
        Bukkit.getScheduler().runTask(getPlugin(), ()->{
            //最终也没有解锁
            if(event.isCancelled()){
                p.closeInventory();
                long currentTime = System.currentTimeMillis();
                String randId = String.valueOf(requestRand.nextLong());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&7ResearchBluePrint&6] &a您可能在尝试使用[&f" + this.getItemName()+"&a] 对物品[&f"+ event.getSlimefunItem().getItemName()+"&a]的研究进行解锁"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[&7ResearchBluePrint&6] &e请确认你的行为"));
                BaseComponent[] component = new ComponentBuilder("")
                    .append("      ")
                    .append("[ 取消 ]")
                    .color(net.md_5.bungee.api.ChatColor.RED)
                    .bold(Boolean.TRUE)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rbcallback false "+ randId))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("点击取消请求").color(net.md_5.bungee.api.ChatColor.RED).create()))
                    .append("      ")
                    .reset()
                    .append("[ 确认 ]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(Boolean.TRUE)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rbcallback true "+ randId))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("点击确认请求").color(net.md_5.bungee.api.ChatColor.GREEN).create()))
                    .create();
                p.spigot().sendMessage(component);
                playerRequests.put(p.getUniqueId(), new PlayerUnlockRequest(currentTime, randId, event));
            }
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p && strings.length == 2){
            PlayerUnlockRequest request;
            if((request = playerRequests.remove(p.getUniqueId())) != null){
                if(Objects.equals(strings[1], request.requestId) && System.currentTimeMillis() < request.requestTime + RequestTimeoutMS){
                    if(Objects.equals(strings[0], "true")){
                        PlayerPreResearchEvent event = request.requestContent;
                        PlayerInventory inventory = p.getInventory();
                        int size = inventory.getSize();
                        for (var i = 0; i < size; ++i){
                            if(SlimefunItem.getByItem(inventory.getItem(i)) == this){
                                PlayerProfile playerProfile = PlayerProfile.find(p).orElse( null );
                                if(playerProfile != null){
                                    if(playerProfile.hasUnlocked(event.getResearch())){
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[&7ResearchBluePrint&6] &e你所请求解锁的研究已经被解锁了!"));
                                    }else {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[&7ResearchBluePrint&6] &e已成功为你解锁了此项研究!"));
                                        var guide = Slimefun.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE);
                                        boolean skipLearningAnimation = Slimefun.getConfigManager().isLearningAnimationDisabled() || !SlimefunGuideSettings.hasLearningAnimationEnabled(p);
                                        event.getResearch().unlock(p, skipLearningAnimation, (player)->{
                                            guide.openItemGroup(playerProfile, event.getSlimefunItem().getItemGroup(), 1);
                                        });
                                        ItemStack itemStack = inventory.getItem(i);
                                        itemStack.setAmount(itemStack.getAmount() - 1);
                                    }
                                }else {
                                    PlayerProfile.find(p);
                                }

                                return true;
                            }
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[&7ResearchBluePrint&6] &c你的背包里已经没有[&f" + this.getItemName() +"&c]了"));
                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[&7ResearchBluePrint&6] &a已成功取消请求"));
                    }

                }else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[&7ResearchBluePrint&6] &c请求不存在或已过期!"));
                }
            }
        }
        return true;
    }


}
