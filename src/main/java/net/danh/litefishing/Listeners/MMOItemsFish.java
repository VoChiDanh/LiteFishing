package net.danh.litefishing.Listeners;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import net.danh.litefishing.Utils.RandomFishing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static net.danh.litefishing.Utils.FishingData.*;

public class MMOItemsFish implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        pFish.put(e.getPlayer(), randomFishing.next());
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
                    pFish.remove(e.getPlayer());
                }
            }
        }
    }
}
