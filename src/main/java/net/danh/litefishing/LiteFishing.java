package net.danh.litefishing;

import net.danh.litefishing.CMD.LFishing;
import net.danh.litefishing.Listeners.CommandFish;
import net.danh.litefishing.Listeners.MMOItemsFish;
import net.danh.litefishing.Listeners.MythicMobsFish;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.FishingData;
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
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aLoaded Files");
        FishingData.reloadFishingData(Bukkit.getConsoleSender());
        Chat.sendCommandSenderMessage(Bukkit.getConsoleSender(), "&aLoaded Data");
    }

    @Override
    public void onDisable() {
        configurationManager.save("", "config.yml");
        configurationManager.save("", "messsage.yml");
    }
}
