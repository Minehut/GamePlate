package com.minehut.gameplate.module.modules.friendlyFire;

import com.google.common.base.Optional;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by luke on 12/31/16.
 */
public class FriendlyFireModule extends Module {

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        boolean proceed = false;
        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (effect.getType().equals(PotionEffectType.POISON) || effect.getType().equals(PotionEffectType.BLINDNESS) ||
                    effect.getType().equals(PotionEffectType.CONFUSION) || effect.getType().equals(PotionEffectType.HARM) ||
                    effect.getType().equals(PotionEffectType.HUNGER) || effect.getType().equals(PotionEffectType.SLOW) ||
                    effect.getType().equals(PotionEffectType.SLOW_DIGGING) || effect.getType().equals(PotionEffectType.WITHER) ||
                    effect.getType().equals(PotionEffectType.WEAKNESS)) {
                proceed = true;
            }
        }
        if (proceed && event.getPotion().getShooter() instanceof Player && TeamManager.getTeamByPlayer((Player) event.getPotion().getShooter()) != null) {
            TeamModule team = TeamManager.getTeamByPlayer((Player) event.getPotion().getShooter());
            for (LivingEntity affected : event.getAffectedEntities()) {
                if (affected instanceof Player && TeamManager.getTeamByPlayer((Player) affected) != null && TeamManager.getTeamByPlayer((Player) affected).equals(team) && !affected.equals(event.getPotion().getShooter())) {
                    event.setIntensity(affected, 0);
                }
            }
        }
    }
}
