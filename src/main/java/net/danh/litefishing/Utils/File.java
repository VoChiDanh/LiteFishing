package net.danh.litefishing.Utils;

import net.danh.litefishing.LiteFishing;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class File {

    public static FileConfiguration getConfig() {
        return LiteFishing.getConfigurationManager().file("", "config.yml");
    }

    public static FileConfiguration getMessage() {
        return LiteFishing.getConfigurationManager().file("", "messsage.yml");
    }

    public static FileConfiguration getSetting() {
        return LiteFishing.getConfigurationManager().file("", "settings.yml");
    }

    public static FileConfiguration getCustomFish() {
        return LiteFishing.getConfigurationManager().file("", "custom_fish.yml");
    }

    public static void checkUpdate(CommandSender c) {
        new UpdateChecker(LiteFishing.getLiteFishing(), 106804).getVersion(version -> {
            if (!LiteFishing.getLiteFishing().getDescription().getVersion().equals(version)) {
                String[] v_s = version.split("-");
                String[] v_p = version.split("-");
                if (Integer.parseInt(v_s[1].replace("b", "")) > Integer.parseInt(v_p[1].replace("b", ""))) {
                    Chat.sendCommandSenderMessage(c, "&aVersion " + version + " &ais available");
                    Chat.sendCommandSenderMessage(c, "&aYour server is using version " + LiteFishing.getLiteFishing().getDescription().getVersion());
                    Chat.sendCommandSenderMessage(c, "&aLet's update it. New update can be fix bug or do sth else important.");
                    Chat.sendCommandSenderMessage(c, "&aPlease check the update log carefully");
                    Chat.sendCommandSenderMessage(c, "&aSpigotMC: https://www.spigotmc.org/resources/106804/");
                    Chat.sendCommandSenderMessage(c, "&aModrinth: https://modrinth.com/plugin/litefishing");
                }
            }
        });
    }
}
