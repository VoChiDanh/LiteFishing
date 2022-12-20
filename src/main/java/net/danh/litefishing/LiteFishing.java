package net.danh.litefishing;

import net.danh.litefishing.CMD.LFishing;
import net.danh.litefishing.Fish.FishingData;
import net.danh.litefishing.Listeners.CommandFish;
import net.danh.litefishing.Listeners.CustomFish;
import net.danh.litefishing.Listeners.MMOItemsFish;
import net.danh.litefishing.Listeners.MythicMobsFish;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import net.xconfig.bukkit.XConfigBukkit;
import net.xconfig.bukkit.config.BukkitConfigurationModel;
import org.bukkit.Bukkit;
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
        if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            Bukkit.getPluginManager().registerEvents(new MythicMobsFish(), liteFishing);
            Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered MythicMobs Fishing Event");
        }
        new LFishing();
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered Commands");
        configurationManager.build("", "config.yml");
        configurationManager.build("", "messsage.yml");
        configurationManager.build("", "settings.yml");
        configurationManager.build("", "custom_fish.yml");
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aLoaded Files");
        FishingData.loadFishingData(Bukkit.getConsoleSender());
        if (File.getSetting().getBoolean("FISHING_MODE.CUSTOM_FISH.ENABLE")) {
            FishingData.loadCustomFish(Bukkit.getConsoleSender());
            Bukkit.getPluginManager().registerEvents(new CustomFish(), liteFishing);
            Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aRegistered Custom Fish");

        }
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
