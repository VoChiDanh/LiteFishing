package net.danh.litefishing.Listeners;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.clip.placeholderapi.PlaceholderAPI;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.danh.litefishing.LiteFishing;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import net.danh.litefishing.Utils.RandomFishing;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Fish implements Listener {

    HashMap<Player, String> pFish = new HashMap<>();
    HashMap<String, Integer> cFish = new HashMap<>();
    HashMap<String, String> dFish = new HashMap<>();

    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        RandomFishing<String> randomFishing = new RandomFishing<>();
        for (String s : Objects.requireNonNull(File.getConfig().getConfigurationSection("SETTINGS")).getKeys(false)) {
            if (!Objects.requireNonNull(File.getConfig().getString("SETTINGS." + s + ".TYPE")).equalsIgnoreCase("COMMAND")) {
                randomFishing.add(File.getConfig().getInt("SETTINGS." + s + ".CHANCE"), File.getConfig().getString("SETTINGS." + s + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + s + ".ID"));
                cFish.putIfAbsent(File.getConfig().getString("SETTINGS." + s + ".TYPE") + ":" + File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getInt("SETTINGS." + s + ".CHANCE"));
            } else {
                randomFishing.add(File.getConfig().getInt("SETTINGS." + s + ".CHANCE"), File.getConfig().getString("SETTINGS." + s + ".ID"));
                dFish.putIfAbsent(File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getString("SETTINGS." + s + ".DISPLAY"));
                cFish.putIfAbsent(File.getConfig().getString("SETTINGS." + s + ".ID"), File.getConfig().getInt("SETTINGS." + s + ".CHANCE"));
            }
        }
        pFish.putIfAbsent(e.getPlayer(), randomFishing.next());
        String fish = pFish.get(e.getPlayer());
        if (fish == null) return;
        int chance = cFish.get(fish);
        int lChance = ThreadLocalRandom.current().nextInt(100);
        if (chance <= lChance) return;
        String[] ftype = null;
        if (fish.contains(":")) {
            ftype = fish.split(":");
        }
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) || e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            if (ftype != null) {
                if (ftype[0].equalsIgnoreCase("MYTHICMOB")) {
                    Optional<MythicMob> mob = MythicBukkit.inst().getMobManager().getMythicMob(ftype[1]);
                    if (mob.isPresent()) {
                        mob.get().spawn(new AbstractLocation(e.getHook().getLocation().getWorld().getName(), e.getHook().getLocation().getX(), e.getHook().getLocation().getY(), e.getHook().getLocation().getZ()), 1);
                        Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.MOB"), "CAUGHT.MOB is null").replace("<name>", mob.get().getDisplayName().toString()).replace("<chance>", String.valueOf(chance)));
                    }
                }
                if (ftype[0].equalsIgnoreCase("MMOITEMS")) {
                    String[] mtype = ftype[1].split(";");
                    MMOItem mmoItems = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(mtype[0]), mtype[1]);
                    if (mmoItems != null) {
                        ItemStack item = mmoItems.newBuilder().build();
                        if (item != null) {
                            if (!(e.getPlayer().getInventory().firstEmpty() == -1)) {
                                e.getPlayer().getInventory().addItem(item);
                                Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.ITEM"), "CAUGHT.ITEM is null").replace("<name>", item.getItemMeta().getDisplayName()).replace("<chance>", String.valueOf(chance)));
                            } else {
                                e.getHook().getLocation().getWorld().dropItem(e.getHook().getLocation(), item);
                                Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.ITEM_FULL"), "CAUGHT.ITEM_FULL is null").replace("<name>", item.getItemMeta().getDisplayName()).replace("<chance>", String.valueOf(chance)));
                            }
                        }
                    }
                }
            }
            if (!fish.contains(":")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(LiteFishing.getLiteFishing(), () -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(e.getPlayer(), fish));
                    Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.COMMAND"), "CAUGHT.COMMAND is null").replace("<name>", dFish.get(fish)).replace("<chance>", String.valueOf(chance)));
                });
            }
        }
        pFish.remove(e.getPlayer());
        cFish.clear();
        dFish.clear();
    }
}
