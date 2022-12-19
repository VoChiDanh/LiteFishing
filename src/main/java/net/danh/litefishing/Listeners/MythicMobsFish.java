package net.danh.litefishing.Listeners;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.danh.litefishing.Fish.FishingData;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static net.danh.litefishing.Fish.FishingData.*;

public class MythicMobsFish implements Listener {

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
                if (ftype[0].equalsIgnoreCase("MYTHICMOB")) {
                    Optional<MythicMob> mob = MythicBukkit.inst().getMobManager().getMythicMob(ftype[1]);
                    if (mob.isPresent()) {
                        mob.get().spawn(new AbstractLocation(e.getHook().getLocation().getWorld().getName(), e.getHook().getLocation().getX(), e.getHook().getLocation().getY(), e.getHook().getLocation().getZ()), 1);
                        Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.MOB"), "CAUGHT.MOB is null").replace("<name>", mob.get().getDisplayName().toString()).replace("<chance>", String.valueOf(chance)));
                    }
                    if (File.getSetting().getBoolean("FISHING_MODE.MYTHICMOB.DISABLE_VANILLA_FISH")) {
                        FishingData.deleteVanillaFish(e, true);
                    }
                }
            }
        }
    }
}
