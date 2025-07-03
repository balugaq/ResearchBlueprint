package com.balugaq.rb.implementation;

import com.balugaq.rb.implementation.command.ResearchBlueprintCommand;
import com.balugaq.rb.implementation.slimefun.CustomResearchBlueprintApplier;
import com.balugaq.rb.implementation.initialization.ResearchConfigurations;
import com.balugaq.rb.api.cfgparse.parser.ConfigurationParser;
import com.balugaq.rb.implementation.initialization.parts.ResearchConfiguration;
import com.balugaq.rb.implementation.slimefun.Groups;
import com.balugaq.rb.implementation.slimefun.Items;
import com.balugaq.rb.util.SlimefunRegistryUtil;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;

@Getter
public class ResearchBlueprintPlugin extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static ResearchBlueprintPlugin instance;
    @Getter
    private final @NotNull String username;
    @Getter
    private final @NotNull String repo;
    @Getter
    private final @NotNull String branch;
    private ConfigManager configManager;
    @Getter
    private ResearchConfigurations configurations;

    public ResearchBlueprintPlugin() {
        this.username = "balugaq";
        this.repo = "ResearchBlueprint";
        this.branch = "master";
    }

    public static ConfigManager getConfigManager() {
        return getInstance().configManager;
    }

    /**
     * Initializes the plugin and sets up all necessary components.
     */
    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("正在加载配置文件...");
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        var researchesConfig = getConfig().getConfigurationSection("researches");
        if (researchesConfig == null) {
            getLogger().warning("未找到 researches 配置项！");
            configurations = new ResearchConfigurations(List.of());
        } else {
            configurations = ConfigurationParser.parse(researchesConfig, ResearchConfigurations.class);
            for (ResearchConfiguration configuration : configurations.getConfigurations()) {
                ResearchConfigurations.byIdentifier.put(configuration.getIdentifier(), configuration);
            }
        }

        // load Slimefun part
        Groups.setup();
        Items.setup();
        Bukkit.getPluginManager().registerEvents(new CustomResearchBlueprintApplier(), this);
        getCommand("rb").setExecutor(new ResearchBlueprintCommand());
        getLogger().info("成功启用 " + getName());
    }

    @Override
    public void onDisable() {
        SlimefunRegistryUtil.unregisterItems(this);
        SlimefunRegistryUtil.unregisterItemGroups(this);
        getCommand("rb").setExecutor(null);
        getCommand("rbcallback").setExecutor(null);
        this.configManager = null;

        getLogger().info("成功禁用 " + getName());

        // Clear instance
        instance = null;
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    /**
     * Returns the bug tracker URL for the plugin.
     *
     * @return the bug tracker URL
     */
    @NotNull
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues/", this.username, this.repo);
    }
}
