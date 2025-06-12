package com.balugaq.rb.implementation.slimefun;

import com.balugaq.rb.implementation.Keys;
import com.balugaq.rb.implementation.ResearchBlueprintPlugin;
import com.balugaq.rb.implementation.initialization.ResearchConfigurations;
import com.balugaq.rb.implementation.initialization.parts.ResearchConfiguration;
import com.balugaq.rb.implementation.initialization.parts.ResearchType;
import com.balugaq.rb.implementation.initialization.parts.Scope;
import com.balugaq.rb.implementation.initialization.parts.ScopeType;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunItemRegistryFinalizedEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomResearchBlueprintApplier implements Listener {
    static final int BASE_HASH = 5017;
    static final int UNREACHABLE_LEVELS = 99999999;
    static Map<Scope, Map<ScopeType, RegexDefineApplier>> regexDefineAppliers = new EnumMap<>(Scope.class);
    static Map<Scope, ItemDefineApplier> itemDefineAppliers = new EnumMap<>(Scope.class);
    static Map<String /* Player name */, ResearchConfiguration> bypassPlayers = new HashMap<>();
    static Map<String /* ResearchConfiguration Identifier */, Set<Research>> researches = new HashMap<>();

    //<editor-fold defaultstate="collapsed" desc="Initialization">
    static {
        itemDefineAppliers.put(Scope.ADDON, (allItems, values, applyTo) -> {
            for (var item : allItems) {
                if (containsIgnoreCase(values, item.getAddon().getName())) {
                    applyTo.add(item);
                }
            }
        });
        itemDefineAppliers.put(Scope.NAME, (allItems, values, applyTo) -> {
            for (var item : allItems) {
                if (containsIgnoreCase(values, item.getItemName())) {
                    applyTo.add(item);
                }
            }
        });
        itemDefineAppliers.put(Scope.ID, (allItems, values, applyTo) -> {
            for (var item : allItems) {
                if (containsIgnoreCase(values, item.getId())) {
                    applyTo.add(item);
                }
            }
        });
        itemDefineAppliers.put(Scope.COLOR_STRIPPED_NAME, (allItems, values, applyTo) -> {
            for (var item : allItems) {
                if (containsIgnoreCase(values, stripColor(item.getItemName()))) {
                    applyTo.add(item);
                }
            }
        });

        regexDefineAppliers.put(Scope.ADDON, new EnumMap<>(ScopeType.class) {
            {
                put(ScopeType.INCLUDE, (allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (matchRegex(regex, item.getAddon().getName())) {
                            applyTo.add(item);
                        }
                    }
                });
                put(ScopeType.EXCLUDE, (allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (!matchRegex(regex, item.getAddon().getName())) {
                            applyTo.add(item);
                        }
                    }
                });
            }
        });
        regexDefineAppliers.put(Scope.NAME, new EnumMap<>(ScopeType.class) {
            {
                put(ScopeType.INCLUDE, (allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (matchRegex(regex, item.getItemName())) {
                            applyTo.add(item);
                        }
                    }
                });
                put(ScopeType.EXCLUDE, ((allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (!matchRegex(regex, item.getItemName())) {
                            applyTo.add(item);
                        }
                    }
                }));
            }
        });
        regexDefineAppliers.put(Scope.ID, new EnumMap<>(ScopeType.class) {
            {
                put(ScopeType.INCLUDE, (allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (matchRegex(regex, item.getId())) {
                            applyTo.add(item);
                        }
                    }
                });
                put(ScopeType.EXCLUDE, ((allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (!matchRegex(regex, item.getId())) {
                            applyTo.add(item);
                        }
                    }
                }));
            }
        });
        regexDefineAppliers.put(Scope.COLOR_STRIPPED_NAME, new EnumMap<>(ScopeType.class) {
            {
                put(ScopeType.INCLUDE, (allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (matchRegex(regex, stripColor(item.getItemName()))) {
                            applyTo.add(item);
                        }
                    }
                });
                put(ScopeType.EXCLUDE, (((allItems, regex, scopeType, applyTo) -> {
                    for (var item : allItems) {
                        if (!matchRegex(regex, stripColor(item.getItemName()))) {
                            applyTo.add(item);
                        }
                    }
                })));
            }
        });
    }
    //</editor-fold>

    @EventHandler
    public void researchBypasses(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (bypassPlayers.containsKey(player.getName())) {
            ResearchConfiguration configuration = bypassPlayers.get(player.getName());
            researchAll(player, configuration);
        }
    }

    public static void researchAll(Player player, ResearchConfiguration configuration) {
        for (var research : researches.get(configuration.getIdentifier())) {
            research.unlock(player, true);
        }
    }

    @EventHandler
    public void load(SlimefunItemRegistryFinalizedEvent event) {
        List<SlimefunItem> allItems = new ArrayList<>(Slimefun.getRegistry().getAllSlimefunItems());

        ResearchConfigurations configurations = ResearchBlueprintPlugin.getInstance().getConfigurations();
        for (var configuration : configurations.getConfigurations()) {
            if (!configuration.isEnabled()) {
                continue;
            }

            var bindItems = configuration.getBindItems();
            Set<SlimefunItem> applyTo = new HashSet<>();
            for (var itemDefine : bindItems.getItems().getDefines()) {
                var scope = itemDefine.getScope();
                var values = itemDefine.getValues();
                getItemDefineApplier(scope).apply(allItems, values, applyTo);
            }
            for (var regexDefine : bindItems.getRegex().getDefines()) {
                var regex = regexDefine.getValue();
                var scope = regexDefine.getScope();
                var scopeType = regexDefine.getScopeType();
                getRegexDefineApplier(scope, scopeType).apply(allItems, regex, scopeType, applyTo);
            }

            generateResearches(configuration, applyTo);
        }
    }

    public static void generateResearches(ResearchConfiguration configuration, Set<SlimefunItem> applyTo) {
        var researchType = configuration.getResearchType();
        if (researchType == ResearchType.RESEARCH_ALL) {
            var r = new Research(
                    Keys.newKey(configuration.getIdentifier()),
                    getResearchId(configuration.getIdentifier()),
                    getResearchMessage(configuration),
                    UNREACHABLE_LEVELS
            );

            r.addItems(applyTo.toArray(new SlimefunItem[0]));

            r.register();
        } else if (researchType == ResearchType.RESEARCH_ANY) {
            Set<Research> rs = new HashSet<>();
            for (var item : applyTo) {
                var r = new Research(
                        Keys.newKey(configuration.getIdentifier()),
                        getResearchId(configuration.getIdentifier() + item.getId()),
                        getResearchMessage(configuration),
                        UNREACHABLE_LEVELS
                );

                r.addItems(item);
                try {
                    r.register();
                }

                rs.add(r);
            }
        }
    }

    public static String getResearchMessage(ResearchConfiguration configuration) {
        // no idea yet.
        return "物品";
    }

    public static int getResearchId(String identifier) {
        int stableHash = BASE_HASH;
        for (char c : identifier.toCharArray()) {
            stableHash = (stableHash * 31 + c) % Integer.MAX_VALUE;
        }

        return Math.abs(stableHash);
    }

    public static boolean matchRegex(String regex, String input) {
        return input.matches(regex);
    }

    public static boolean containsIgnoreCase(List<String> list, String element) {
        for (String s : list) {
            if (s.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }

    public static String stripColor(String input) {
        return ChatColor.stripColor(input);
    }

    public static ItemDefineApplier getItemDefineApplier(Scope scope) {
        return itemDefineAppliers.get(scope);
    }

    public static RegexDefineApplier getRegexDefineApplier(Scope scope, ScopeType scopeType) {
        return regexDefineAppliers.get(scope).get(scopeType);
    }

    @FunctionalInterface
    public interface ItemDefineApplier {
        void apply(List<SlimefunItem> allItems, List<String> values, Set<SlimefunItem> applyTo);
    }

    @FunctionalInterface
    public interface RegexDefineApplier {
        void apply(List<SlimefunItem> allItems, String regex, ScopeType scopeType, Set<SlimefunItem> applyTo);
    }
}
