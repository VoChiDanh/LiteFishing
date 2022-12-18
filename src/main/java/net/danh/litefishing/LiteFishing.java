package net.danh.litefishing;

import net.danh.litefishing.CMD.LFishing;
import net.danh.litefishing.Listeners.Fish;
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
        Bukkit.getPluginManager().registerEvents(new Fish(), liteFishing);
        new LFishing();
        configurationManager.build("", "config.yml");
        configurationManager.build("", "messsage.yml");
    }

    @Override
    public void onDisable() {
        configurationManager.save("", "config.yml");
        configurationManager.save("", "messsage.yml");
    }
}
