package com.minehut.gameplate.module.modules.kit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/21/2016.
 */
public abstract class Kit {

    private String name;
    private int price;
    private String description;
    private Material iconMaterial;

    private List<KitItem> items;

    public Kit(String name, int price, String description, Material iconMaterial, List<KitItem> items) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.iconMaterial = iconMaterial;
        this.items = items;
    }

    public void apply(Player player) {
        items.forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));
    }

    public static Kit fromJson(JsonObject object) {
        String name = object.get("name").getAsString();
        int price = 0;
        if (object.has("price")) {
            price = object.get("price").getAsInt();
        }
        String description = "";
        if (object.has("description")) {
            object.get("description").getAsString();
        }
        Material iconMaterial;
        if (object.has("material")) {
            iconMaterial = Material.getMaterial(object.get("material").getAsString());
        } else if (GamePlate.getInstance().getGameHandler().getMatch().getJson().has("defaultKitMaterial")) {
            iconMaterial = Material.getMaterial(GamePlate.getInstance().getGameHandler().getMatch().getJson().get("defaultKitMaterial").getAsString());
        }
        iconMaterial = Material.CLAY;
        List<KitItem> kitItems = new ArrayList<>();
        JsonArray array = object.get("items").getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();
            String type = obj.get("type").getAsString();
            String mat = obj.get("material").getAsString();
            mat = mat.replace(" ", "_").toUpperCase();
            Material material = Material.getMaterial(mat);
            KitItem kitItem;
            switch (type) {
                case "item":

                    break;
                case "armor":

                    break;
                case "offHand": // Might not be needed

                    break;
            }

        }
        return null;
    }

}
