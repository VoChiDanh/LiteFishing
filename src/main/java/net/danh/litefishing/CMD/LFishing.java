package net.danh.litefishing.CMD;

import net.danh.litefishing.CMD.API.CMDBase;
import net.danh.litefishing.LiteFishing;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static net.danh.litefishing.Listeners.Fish.cFish;
import static net.danh.litefishing.Listeners.Fish.dFish;

public class LFishing extends CMDBase {
    public LFishing() {
        super("litefishing");
    }

    private void reloadFiles(CommandSender c) {
        Chat.sendCommandSenderMessage(c, "&6Reloading Files...");
        LiteFishing.getConfigurationManager().reload("", "config.yml");
        LiteFishing.getConfigurationManager().reload("", "messsage.yml");
        Chat.sendCommandSenderMessage(c, "&aReloaded Files...");
    }

    private void reloadFishingData(CommandSender c) {
        Chat.sendCommandSenderMessage(c, "&eReloading Data...");
        cFish.clear();
        dFish.clear();
        for (String s : Objects.requireNonNull(File.getConfig().getConfigurationSection("SETTINGS")).getKeys(false)) {
            if (!Objects.requireNonNull(File.getConfig().getString("SETTINGS." + s + ".TYPE")).equalsIgnoreCase("COMMAND")) {
                cFish.putIfAbsent(File.getConfig().getString("SETTINGS." + s + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getInt("SETTINGS." + s + ".CHANCE"));
            } else {
                dFish.putIfAbsent(File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getString("SETTINGS." + s + ".DISPLAY"));
                cFish.putIfAbsent(File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getInt("SETTINGS." + s + ".CHANCE"));
            }
        }
        Chat.sendCommandSenderMessage(c, "&bReloaded Data...");
        Chat.sendCommandSenderMessage(c, "&cCareful with this command, plugin can get issues if you reload it so much!");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (c.hasPermission("lf.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadFiles(c);
                    reloadFishingData(c);
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("lf.admin")) {
                commands.add("reload");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
