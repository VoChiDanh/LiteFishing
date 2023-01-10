package net.danh.litefishing.CMD;

import net.danh.litefishing.API.CMDBase;
import net.danh.litefishing.API.Fish.FishingData;
import net.danh.litefishing.API.Utils.Chat;
import net.danh.litefishing.API.Utils.File;
import net.danh.litefishing.API.Utils.Number;
import net.danh.litefishing.LiteFishing;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.danh.litefishing.API.Fish.FishingData.loadCustomFish;
import static net.danh.litefishing.API.Fish.FishingData.loadFishingData;

public class LFishing extends CMDBase {
    public LFishing() {
        super("litefishing");
    }

    private void reloadFiles(CommandSender c) {
        LiteFishing.getConfigurationManager().reload("", "config.yml");
        LiteFishing.getConfigurationManager().reload("", "messsage.yml");
        LiteFishing.getConfigurationManager().reload("", "settings.yml");
        LiteFishing.getConfigurationManager().reload("", "custom_fish.yml");
        Chat.sendCommandSenderMessage(c, File.getMessage().getString("COMMAND.RELOAD"));
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                Chat.sendCommandSenderMessage(c, File.getMessage().getStringList("COMMAND.HELP_MEMBER"));
            }
            if (args[0].equalsIgnoreCase("sell")) {
                if (c instanceof Player p) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(LiteFishing.getLiteFishing(), () -> p.getInventory().forEach(itemStack -> FishingData.sellCustomFish(p, itemStack)));
                }
            }
        }
        if (c.hasPermission("lf.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    Chat.sendCommandSenderMessage(c, File.getMessage().getStringList("COMMAND.HELP"));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadFiles(c);
                    loadFishingData(c);
                    loadCustomFish(c);
                    File.checkUpdate(c);
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("fish")) {
                    Player p = Bukkit.getPlayer(args[3]);
                    String fishID = args[1];
                    if (p != null) {
                        int amount = Number.getInteger(args[2]);
                        if (amount > 0) {
                            ItemStack fish = FishingData.customFishList.get(fishID);
                            if (fish != null) {
                                fish.setAmount(amount);
                                p.getInventory().addItem(fish);
                                Chat.sendCommandSenderMessage(c, Objects.requireNonNull(File.getMessage().getString("COMMAND.FISH.GIVE_FISH"), "COMMAND.FISH.GIVE_FISH is null").replace("<name>", fish.getItemMeta().getDisplayName()));
                            } else {
                                Chat.sendCommandSenderMessage(c, Objects.requireNonNull(File.getMessage().getString("COMMAND.FISH.ID_IS_NULL"), "COMMAND.FISH.ID_IS_NULL is null").replace("<id>", fishID));
                            }
                        } else {
                            Chat.sendCommandSenderMessage(c, File.getMessage().getString("COMMAND.FISH.AMOUNT_IS_INCORRECT"));
                        }
                    } else {
                        Chat.sendCommandSenderMessage(c, Objects.requireNonNull(File.getMessage().getString("COMMAND.FISH.PLAYER_IS_NULL"), "COMMAND.FISH.PLAYER_IS_NULL is null").replace("name", args[3]));
                    }
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
                commands.add("fish");
            }
            commands.add("help");
            commands.add("sell");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (sender.hasPermission("lf.admin")) {
                if (args[0].equalsIgnoreCase("fish")) {
                    StringUtil.copyPartialMatches(args[1], FishingData.customFishList.keySet().stream().toList(), completions);
                }
            }
        }
        if (args.length == 3) {
            if (sender.hasPermission("lf.admin")) {
                if (args[0].equalsIgnoreCase("fish")) {
                    if (FishingData.customFishList.keySet().stream().toList().contains(args[1])) {
                        StringUtil.copyPartialMatches(args[2], List.of("<number>"), completions);
                    }
                }
            }
        }
        if (args.length == 4) {
            if (sender.hasPermission("lf.admin")) {
                if (args[0].equalsIgnoreCase("fish")) {
                    if (FishingData.customFishList.keySet().stream().toList().contains(args[1])) {
                        if (Number.getInteger(args[2]) > 0) {
                            StringUtil.copyPartialMatches(args[3], Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()), completions);
                        }
                    }
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
