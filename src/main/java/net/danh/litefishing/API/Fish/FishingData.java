package net.danh.litefishing.API.Fish;

import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.litefishing.API.Utils.Chat;
import net.danh.litefishing.API.Utils.File;
import net.danh.litefishing.API.Utils.Number;
import net.danh.litefishing.LiteFishing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FishingData {

    public static RandomFish<String> randomFishing = new RandomFish<>();
    public static RandomCustomFish<String> randomCustomFish = new RandomCustomFish<>();
    public static HashMap<String, ItemStack> customFishList = new HashMap<>();
    public static HashMap<Player, String> pFish = new HashMap<>();
    public static HashMap<String, Integer> cFish = new HashMap<>();
    public static HashMap<String, String> dFish = new HashMap<>();


    public static void loadFishingData(CommandSender c) {
        Chat.sendCommandSenderMessage(c, "&cRemoved " + (cFish.size() - dFish.size()) + " Custom MMO/Mythic Rewards");
        cFish.clear();
        Chat.sendCommandSenderMessage(c, "&cRemoved " + dFish.size() + " Custom CMD Rewards");
        dFish.clear();
        Chat.sendCommandSenderMessage(c, "&cRemoved " + randomFishing.total() + " Custom Rewards");
        randomFishing.clear();
        for (String rewardID : Objects.requireNonNull(File.getConfig().getConfigurationSection("SETTINGS")).getKeys(false)) {
            if (!Objects.requireNonNull(File.getConfig().getString("SETTINGS." + rewardID + ".TYPE")).equalsIgnoreCase("COMMAND")) {
                randomFishing.add(File.getConfig().getInt("SETTINGS." + rewardID + ".CHANCE"), File.getConfig().getString("SETTINGS." + rewardID + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + rewardID + ".ID"));
                cFish.put(File.getConfig().getString("SETTINGS." + rewardID + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + rewardID + ".ID"), File.getConfig().getInt("SETTINGS." + rewardID + ".CHANCE"));
                Chat.sendCommandSenderMessage(c, "&aAdded Custom MMO/Mythic Reward ID" + rewardID + " with chance " + File.getConfig().getInt("SETTINGS." + rewardID + ".CHANCE") + "% and type " + File.getConfig().getString("SETTINGS." + rewardID + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + rewardID + ".ID"));
            } else {
                dFish.put(File.getConfig().getString("SETTINGS." + rewardID + ".ID"), File.getConfig().getString("SETTINGS." + rewardID + ".DISPLAY"));
                cFish.put(File.getConfig().getString("SETTINGS." + rewardID + ".ID"), File.getConfig().getInt("SETTINGS." + rewardID + ".CHANCE"));
                randomFishing.add(File.getConfig().getInt("SETTINGS." + rewardID + ".CHANCE"), File.getConfig().getString("SETTINGS." + rewardID + ".ID"));
                Chat.sendCommandSenderMessage(c, "&aAdded Custom CMD Reward ID " + rewardID + " with chance " + File.getConfig().getString("SETTINGS." + rewardID + ".CHANCE") + "%, display is " + File.getConfig().getString("SETTINGS." + rewardID + ".DISPLAY") + "&a and id is " + File.getConfig().getString("SETTINGS." + rewardID + ".ID"));
            }
        }
        Chat.sendCommandSenderMessage(c, "&aLoaded " + (cFish.size() - dFish.size()) + " Custom MMO/Mythic Rewards");
        Chat.sendCommandSenderMessage(c, "&aLoaded " + dFish.size() + " Custom CMD Rewards");
        Chat.sendCommandSenderMessage(c, "&aLoaded " + randomFishing.total() + " Custom Rewards");
    }

    public static void deleteVanillaFish(PlayerFishEvent e, boolean removePlayerData) {
        if (removePlayerData) {
            pFish.remove(e.getPlayer());
        }
        Entity entity = e.getCaught();
        if (entity != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(LiteFishing.getLiteFishing(), () -> {
                if (entity instanceof Item && !entity.isOnGround()) {
                    entity.remove();
                }
            });
        }
    }

    public static boolean antiBug(PlayerFishEvent e) {
        Entity entity = e.getCaught();
        return entity instanceof Item && !entity.isOnGround();
    }

    public static void loadCustomFish(CommandSender c) {
        if (File.getSetting().getBoolean("FISHING_MODE.CUSTOM_FISH.ENABLE")) {
            Chat.sendCommandSenderMessage(c, "&cRemoved " + customFishList.size() + " Custom Fishes");
            customFishList.clear();
            randomCustomFish.clear();
            for (String fishID : Objects.requireNonNull(File.getCustomFish().getConfigurationSection("CUSTOM_FISH")).getKeys(false)) {
                Material material = Material.getMaterial(Objects.requireNonNull(File.getCustomFish().getString("CUSTOM_FISH." + fishID + ".TYPE"), "CUSTOM_FISH." + fishID + ".TYPE is null"));
                if (material != null) {
                    String name = Chat.colorize(File.getCustomFish().getString("CUSTOM_FISH." + fishID + ".NAME"));
                    int weight = File.getCustomFish().getInt("CUSTOM_FISH." + fishID + ".WEIGHT");
                    int cost = File.getCustomFish().getInt("CUSTOM_FISH." + fishID + ".COST");
                    int custom_model_data = File.getCustomFish().getInt("CUSTOM_FISH." + fishID + ".CUSTOM_MODEL_DATA");
                    List<String> lore = Chat.colorize(File.getCustomFish().getStringList("CUSTOM_FISH." + fishID + ".LORE").stream().map(s -> s.replace("<cost>", String.valueOf(cost))).collect(Collectors.toList()));
                    boolean unbreakable = File.getCustomFish().getBoolean("CUSTOM_FISH." + fishID + ".UNBREAKABLE");
                    List<String> flags = File.getCustomFish().getStringList("CUSTOM_FISH." + fishID + ".FLAGS");
                    List<String> enchants = File.getCustomFish().getStringList("CUSTOM_FISH." + fishID + ".ENCHANTMENTS");
                    ItemStack builder = new ItemStack(material);
                    ItemMeta meta = builder.getItemMeta();
                    for (String flag : flags) {
                        meta.addItemFlags(ItemFlag.valueOf(flag));
                    }
                    for (String enchant : enchants) {
                        String enchantment = enchant.split(":")[0].toLowerCase();
                        String level = enchant.split(":")[1];
                        if (Number.getInteger(level) > 0) {
                            Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(enchantment));
                            if (e != null) {
                                int lvl = Number.getInteger(level);
                                meta.addEnchant(e, lvl, true);
                                if (lvl == 0) {
                                    meta.removeEnchant(e);
                                }
                            }
                        }
                    }
                    if (custom_model_data > 0) {
                        meta.setCustomModelData(custom_model_data);
                    }
                    meta.setUnbreakable(unbreakable);
                    meta.setLore(lore);
                    meta.setDisplayName(name);
                    meta.getPersistentDataContainer().set(new NamespacedKey(LiteFishing.getLiteFishing(), "lf_fish"), PersistentDataType.STRING, fishID);
                    meta.getPersistentDataContainer().set(new NamespacedKey(LiteFishing.getLiteFishing(), "lf_cost"), PersistentDataType.INTEGER, cost);
                    builder.setItemMeta(meta);
                    customFishList.put(fishID, builder);
                    randomCustomFish.add(weight, fishID);
                    Chat.sendCommandSenderMessage(c, "&aAdded Custom Fish ID " + fishID + " with weight " + weight);
                }
            }
            Chat.sendCommandSenderMessage(c, "&aLoaded " + customFishList.size() + " Custom Fishes");
        }
    }

    public static void sellCustomFish(Player p, ItemStack fish) {
        if (fish != null) {
            ItemMeta meta = fish.getItemMeta();
            if (meta != null) {
                String fishID = meta.getPersistentDataContainer().get(new NamespacedKey(LiteFishing.getLiteFishing(), "lf_fish"), PersistentDataType.STRING);
                int cost = meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(LiteFishing.getLiteFishing(), "lf_cost"), PersistentDataType.INTEGER, File.getCustomFish().getInt("CUSTOM_FISH." + fishID + ".COST", 0));
                if (fishID != null) {
                    if (cost > 0) {
                        int count = getPlayerAmount(p, fish);
                        int price = cost * count;
                        List<String> commands = File.getSetting().getStringList("SELL.COMMAND").stream().map(s -> PlaceholderAPI.setPlaceholders(p, s.replace("<cost>", String.valueOf(price)))).toList();
                        for (String cmd : commands) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(LiteFishing.getLiteFishing(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
                        }
                        removeItems(p, fish, count);
                        Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.SELL.EARN"), "COMMAND.SELL.EARN is null").replace("<price>", String.valueOf(price)).replace("<fish>", fish.getItemMeta().getDisplayName()));
                    }
                }
            }
        }
    }

    public static int getPlayerAmount(HumanEntity player, ItemStack item) {
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] items = inv.getContents();
        int c = 0;
        for (final ItemStack is : items) {
            if (is != null) {
                if (is.isSimilar(item)) {
                    c += is.getAmount();
                }
            }
        }
        return c;
    }

    public static void removeItems(Player player, ItemStack item, long amount) {
        item = item.clone();
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] items = inv.getContents();
        int c = 0;
        for (int i = 0; i < items.length; ++i) {
            final ItemStack is = items[i];
            if (is != null) {
                if (is.isSimilar(item)) {
                    if (c + is.getAmount() > amount) {
                        final long canDelete = amount - c;
                        is.setAmount((int) (is.getAmount() - canDelete));
                        items[i] = is;
                        break;
                    }
                    c += is.getAmount();
                    items[i] = null;
                }
            }
        }
        inv.setContents(items);
        player.updateInventory();
    }

}