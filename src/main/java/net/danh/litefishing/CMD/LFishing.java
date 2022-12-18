package net.danh.litefishing.CMD;

import net.danh.litefishing.CMD.API.CMDBase;
import net.danh.litefishing.LiteFishing;
import net.danh.litefishing.Utils.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LFishing extends CMDBase {
    public LFishing() {
        super("litefishing");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (c.hasPermission("lf.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    LiteFishing.getConfigurationManager().reload("", "config.yml");
                    LiteFishing.getConfigurationManager().reload("", "messsage.yml");
                    Chat.sendCommandSenderMessage(c, "&aReloaded");
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
