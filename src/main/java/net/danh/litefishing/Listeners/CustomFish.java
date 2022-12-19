package net.danh.litefishing.Listeners;

import net.danh.litefishing.Fish.FishingData;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CustomFish implements Listener {
    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) || e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            if (FishingData.antiBug(e)) return;
            ItemStack fish = FishingData.customFishList.get(FishingData.randomCustomFish.next());
            if (fish == null) return;
            if (e.getPlayer().getInventory().firstEmpty() == -1) {
                e.getHook().getWorld().dropItem(e.getPlayer().getLocation(), fish);
                Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.CUSTOM_FISH"), "CAUGHT.CUSTOM_FISH is null")
                        .replace("<name>", fish.getItemMeta().getDisplayName()));
            } else {
                e.getPlayer().getInventory().addItem(fish);
                Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.CUSTOM_FISH"), "CAUGHT.CUSTOM_FISH is null")
                        .replace("<name>", fish.getItemMeta().getDisplayName()));
            }
            if (File.getSetting().getBoolean("FISHING_MODE.CUSTOM_FISH.DISABLE_VANILLA_FISH")) {
                FishingData.deleteVanillaFish(e, false);
            }
        }
    }

}
