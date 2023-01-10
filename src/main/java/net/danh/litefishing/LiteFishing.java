package net.danh.litefishing;

import net.danh.litefishing.API.Fish.FishingData;
import net.danh.litefishing.API.Utils.Chat;
import net.danh.litefishing.API.Utils.File;
import net.danh.litefishing.CMD.LFishing;
import net.danh.litefishing.Listeners.*;
import net.xconfig.bukkit.XConfigBukkit;
import net.xconfig.bukkit.config.BukkitConfigurationModel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LiteFishing extends JavaPlugin {
    private static LiteFishing liteFishing;
    private static BukkitConfigurationModel configurationManager;

    public static LiteFishing getLiteFishing() {
        return liteFishing;
    }

    public static BukkitConfigurationModel getConfigurationManager() {
        return configurationManager;
    }

    @Override
    public void onEnable() {
        liteFishing = this;
        configurationManager = XConfigBukkit.manager(liteFishing);
        Bukkit.getPluginManager().registerEvents(new CommandFish(), liteFishing);
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered Command Fishing Event");
        if (getServer().getPluginManager().getPlugin("MMOItems") != null) {
            Bukkit.getPluginManager().registerEvents(new MMOItemsFish(), liteFishing);
            Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered MMOItems Fishing Event");
        }
        Plugin mythicmob = getServer().getPluginManager().getPlugin("MythicMobs");
        if (mythicmob != null) {
            if (mythicmob.getDescription().getVersion().startsWith("5")) {
                Bukkit.getPluginManager().registerEvents(new MythicMobsFish(), liteFishing);
                Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered MythicMobs v5 Fishing Event");
            }
            if (mythicmob.getDescription().getVersion().startsWith("4")) {
                Bukkit.getPluginManager().registerEvents(new MythicMobsLegacyFish(), liteFishing);
                Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered MythicMobs v4 (4.8.0 -> 4.14.1) Fishing Event");
            }
        }
        new LFishing();
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered Commands");
        configurationManager.build("", "config.yml");
        configurationManager.build("", "messsage.yml");
        configurationManager.build("", "settings.yml");
        configurationManager.build("", "custom_fish.yml");
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aLoaded Files");
        FishingData.loadFishingData(Bukkit.getConsoleSender());
        FishingData.loadCustomFish(Bukkit.getConsoleSender());
        Bukkit.getPluginManager().registerEvents(new CustomFish(), liteFishing);
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered Custom Fish");
        File.checkUpdate(Bukkit.getConsoleSender());
    }

    @Override
    public void onDisable() {
        configurationManager.save("", "config.yml");
        configurationManager.save("", "messsage.yml");
        configurationManager.save("", "settings.yml");
        configurationManager.save("", "custom_fish.yml");
    }
}
