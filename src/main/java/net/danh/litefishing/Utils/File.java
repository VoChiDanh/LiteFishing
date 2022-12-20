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
                String[] version_spigot = version.split("-");
                String[] version_plugin = LiteFishing.getLiteFishing().getDescription().getVersion().split("-");
                Double version_release = Number.getDouble(version_spigot[0]);
                Double version_beta = Number.getDouble(version_plugin[0]);
                Integer build_release = Number.getInteger(version_spigot[1].replace("b", ""));
                Integer build_beta = Number.getInteger(version_plugin[1].replace("b", ""));
                if (version_release > version_beta) {
                    Chat.sendCommandSenderMessage(c, "&aVersion " + version + " &ais available");
                    Chat.sendCommandSenderMessage(c, "&aYour server is using version " + LiteFishing.getLiteFishing().getDescription().getVersion());
                    Chat.sendCommandSenderMessage(c, "&aPlease update it. This is major update.");
                    Chat.sendCommandSenderMessage(c, "&aPlease check the update log carefully");
                    Chat.sendCommandSenderMessage(c, "&aSpigotMC: https://www.spigotmc.org/resources/106804/");
                    Chat.sendCommandSenderMessage(c, "&aModrinth: https://modrinth.com/plugin/litefishing");
                    return;
                }
                if (version_release.equals(version_beta)) {
                    if (build_release > build_beta) {
                        Chat.sendCommandSenderMessage(c, "&6Build " + build_release + "&a for version " + version_release + " is available");
                        Chat.sendCommandSenderMessage(c, "&6Your server is using build " + build_beta + "&a of version " + version_beta);
                        Chat.sendCommandSenderMessage(c, "&6Please update it. The new build may be fixed or prepare for a major update in the near future.");
                        Chat.sendCommandSenderMessage(c, "&6You are out of date " + (build_release - build_beta) + " build(s)");
                        Chat.sendCommandSenderMessage(c, "&6SpigotMC: https://www.spigotmc.org/resources/106804/");
                        Chat.sendCommandSenderMessage(c, "&6Modrinth: https://modrinth.com/plugin/litefishing");
                        return;
                    }
                    if (build_release < build_beta) {
                        Chat.sendCommandSenderMessage(c, "&eYou are using beta version, be careful with everything!");
                        Chat.sendCommandSenderMessage(c, "&eIf you find any issue from this version, you should report it to author");
                        Chat.sendCommandSenderMessage(c, "&eDiscord Support: https://discord.gg/r5ejaPSjku");
                        Chat.sendCommandSenderMessage(c, "&eDiscord ID: VoChiDanh#0862");
                        return;
                    }
                    return;
                }
                if (version_release < version_beta) {
                    Chat.sendCommandSenderMessage(c, "&eYou are using beta version, be careful with everything!");
                    Chat.sendCommandSenderMessage(c, "&eIf you find any issue from this version, you should report it to author");
                    Chat.sendCommandSenderMessage(c, "&eDiscord Support: https://discord.gg/r5ejaPSjku");
                    Chat.sendCommandSenderMessage(c, "&eDiscord ID: VoChiDanh#0862");
                }
            }
        });
    }
}
