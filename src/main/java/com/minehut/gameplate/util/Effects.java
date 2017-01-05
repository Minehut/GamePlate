package com.minehut.gameplate.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Element;

/**
 * Originally created by Lucas on 12/21/2016 in KitModuleBuilder.
 * Moved here by MatrixTunnel on 01/04/2017.
 */
public class Effects {

    public static PotionEffect parsePotionEffect(Element element) {
        PotionEffectType effectType = PotionEffectType.getByName(element.getAttributeValue("id").toUpperCase().replace(" ", "_"));
        int level = 0;
        if (element.getAttribute("level") != null) {
            level = Numbers.parseInt(element.getAttributeValue("level"));
        }
        int amplifier = 0;
        if (element.getAttributeValue("amplifier") != null) {
            amplifier = Numbers.parseInt(element.getAttributeValue("amplifier"));
        }
        return new PotionEffect(effectType, level, amplifier);
    }

}
