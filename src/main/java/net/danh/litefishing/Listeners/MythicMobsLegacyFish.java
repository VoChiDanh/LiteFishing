package net.danh.litefishing.Listeners;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import net.danh.litefishing.Fish.FishingData;
import net.danh.litefishing.Utils.Chat;
import net.danh.litefishing.Utils.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static net.danh.litefishing.Fish.FishingData.*;

public class MythicMobsLegacyFish implements Listener {

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
            if (!FishingData.antiBug(e)) return;
            if (ftype != null) {
                if (ftype[0].equalsIgnoreCase("MYTHICMOB")) {
                    MythicMob mob = MythicMobs.inst().getMobManager().getMythicMob(ftype[1]);
                    try {
                        MythicMobs.inst().getAPIHelper().spawnMythicMob(mob, e.getHook().getLocation(), 1);
                    } catch (InvalidMobTypeException ex) {
                        throw new RuntimeException(ex);
                    }
                    Chat.sendPlayerMessage(e.getPlayer(), Objects.requireNonNull(File.getMessage().getString("CAUGHT.MOB"), "CAUGHT.MOB is null").replace("<name>", mob.getDisplayName().toString()).replace("<chance>", String.valueOf(chance)));
                    if (File.getSetting().getBoolean("FISHING_MODE.MYTHICMOB.DISABLE_VANILLA_FISH")) {
                        FishingData.deleteVanillaFish(e, true);
                    }
                }
            }
        }
    }
}
