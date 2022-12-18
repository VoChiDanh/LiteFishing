package net.danh.litefishing.Utils;

import net.danh.litefishing.LiteFishing;
import org.bukkit.configuration.file.FileConfiguration;

public class File {

    public static FileConfiguration getConfig() {
        return LiteFishing.getConfigurationManager().file("", "config.yml");
    }

    public static FileConfiguration getMessage() {
        return LiteFishing.getConfigurationManager().file("", "messsage.yml");
    }
}
