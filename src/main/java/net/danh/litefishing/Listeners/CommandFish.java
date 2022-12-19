package net.danh.litefishing.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.litefishing.Fish.FishingData;
import net.danh.litefishing.LiteFishing;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static net.danh.litefishing.Fish.FishingData.*;

public class CommandFish implements Listener {
    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        pFish.put(e.getPlayer(), randomFishing.next());
        String fish = pFish.get(e.getPlayer());
        if (fish == null) return;
        int chance = cFish.get(fish);
        int lChance = ThreadLocalRandom.current().nextInt(100);
        if (chance <= lChance) return;
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) || e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            if (!fish.contains(":")) {
                if (!FishingData.antiBug(e)) return;
                Bukkit.getScheduler().scheduleSyncDelayedTask(LiteFishing.getLiteFishing(), () -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(e.getPlayer(), fish));
                    Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.COMMAND"), "CAUGHT.COMMAND is null").replace("<name>", dFish.get(fish)).replace("<chance>", String.valueOf(chance)));
                    if (File.getSetting().getBoolean("FISHING_MODE.COMMAND.DISABLE_VANILLA_FISH")) {
                        FishingData.deleteVanillaFish(e, true);
                    }
                });
            }
        }
    }
}
