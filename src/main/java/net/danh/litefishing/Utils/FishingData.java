package net.danh.litefishing.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class FishingData {

    public static RandomFishing<String> randomFishing = new RandomFishing<>();
    public static HashMap<Player, String> pFish = new HashMap<>();
    public static HashMap<String, Integer> cFish = new HashMap<>();
    public static HashMap<String, String> dFish = new HashMap<>();


    public static void reloadFishingData(CommandSender c) {
        randomFishing.clear();
        cFish.clear();
        dFish.clear();
        for (String s : Objects.requireNonNull(File.getConfig().getConfigurationSection("SETTINGS")).getKeys(false)) {
            if (!Objects.requireNonNull(File.getConfig().getString("SETTINGS." + s + ".TYPE")).equalsIgnoreCase("COMMAND")) {
                randomFishing.add(File.getConfig().getInt("SETTINGS." + s + ".CHANCE"), File.getConfig().getString("SETTINGS." + s + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + s + ".ID"));
                cFish.put(File.getConfig().getString("SETTINGS." + s + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getInt("SETTINGS." + s + ".CHANCE"));
            } else {
                dFish.put(File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getString("SETTINGS." + s + ".DISPLAY"));
                cFish.put(File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getInt("SETTINGS." + s + ".CHANCE"));
                randomFishing.add(File.getConfig().getInt("SETTINGS." + s + ".CHANCE"), File.getConfig().getString("SETTINGS." + s + ".ID"));
            }
        }
        Chat.sendCommandSenderMessage(c, "&bReloaded Data");
        Chat.sendCommandSenderMessage(c, "&cCareful with this command, plugin can get issues if you reload it so much!");
    }
}
