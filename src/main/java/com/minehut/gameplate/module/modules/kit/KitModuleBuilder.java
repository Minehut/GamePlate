package com.minehut.gameplate.module.modules.kit;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.kit.types.KitInventoryItem;
import com.minehut.gameplate.module.modules.kit.types.KitPotionItem;
import com.minehut.gameplate.util.Effects;
import com.minehut.gameplate.util.Items;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/21/2016.
 */
@BuilderData(load = ModuleLoadTime.EARLIER)
public class KitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<KitModule> results = new ModuleCollection();

        for (Element kitsElement : match.getDocument().getRootElement().getChildren("kits")) {
            for (Element kitElement : kitsElement.getChildren()) {
                KitModule kit = parseKit(kitElement);

                if (kitElement.getAttributeValue("parents") != null) {
                    String split[] = kitElement.getAttributeValue("parents").replace(" ", "").split(",");
                    for (String parent : split) {
                        for (KitModule parentKit : results) {
                            if (parentKit.getId().equals(parent)) {
                                kit.addParentKit(parentKit);
                            }
                        }
                    }
                }

                results.add(kit);
            }
        }

        return results;
    }

    public static KitModule parseKit(Element kitElement) {
        String id = kitElement.getAttributeValue("id");
        List<KitItem> kitItems = new ArrayList<>();

        for (Element element : kitElement.getChildren()) {
            if (element.getName().equalsIgnoreCase("item")) {

                ItemStack itemStack = Items.parseItemstack(element);
                int slot = Numbers.parseInt(element.getAttributeValue("slot"));

                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("helmet")) {
                ItemStack itemStack = Items.parseItemstack(element);
                int slot = 103;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("chestplate")) {
                ItemStack itemStack = Items.parseItemstack(element);
                int slot = 102;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("leggings")) {
                ItemStack itemStack = Items.parseItemstack(element);
                int slot = 101;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("boots")) {
                ItemStack itemStack = Items.parseItemstack(element);
                int slot = 100;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("offhand")) {
                ItemStack itemStack = Items.parseItemstack(element);
                int slot = -2;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("potion")) {
                PotionEffect potionEffect = Effects.parsePotionEffect(element);

                int slot = -1;
                if (element.getAttributeValue("slot") != null) {
                    slot = Numbers.parseInt(element.getAttributeValue("slot"));
                }

                int amount = 1;
                if (element.getAttributeValue("amount") != null) {
                    amount = Numbers.parseInt(element.getAttributeValue("amount"));
                }

                kitItems.add(new KitPotionItem(potionEffect, slot, amount));
            }
        }

        return new KitModule(id, kitItems);
    }

}
