package com.minehut.gameplate.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GamePlate;
import org.bukkit.ChatColor;

public enum ChatConstant {

    GENERIC_WELCOME("generic.welcome"),

    GAME_BUILD_HEIGHT("game.buildheight"),

    ERROR_JSON("error.json");

    private final String path;

    ChatConstant(String path) {
        this.path = path;
    }

    public static ChatConstant fromPath(String path) {
        if (path != null) {
            for (ChatConstant chatConstant : ChatConstant.values()) {
                if (path.equalsIgnoreCase(chatConstant.path)) {
                    return chatConstant;
                }
            }
        }
        return null;
    }

    public String getMessage(String locale) {
        JsonObject localized = GamePlate.getInstance().getLocaleHandler().getLocaleDocument(locale.split("_")[0]);

        String split[] = this.path.split("\\.");
        String type = split[0];
        String id = split[1];

        for (JsonElement jsonElement : localized.getAsJsonArray("data")) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.get("type").getAsString().equalsIgnoreCase(type)) {
                for (JsonElement e : jsonObject.getAsJsonArray("messages")) {
                    JsonObject o = e.getAsJsonObject();
                    if (o.get("id").getAsString().equalsIgnoreCase(id)) {
                        return o.get("message").getAsString();
                    }
                }
            }
        }
        return getMessage("en_US");
    }

    public ChatMessage asMessage(ChatMessage... messages) {
        return new LocalizedChatMessage(this, messages);
    }

    public ChatMessage asMessage(ChatColor color, ChatMessage... messages) {
        return new UnlocalizedChatMessage(color + "{0}", asMessage(messages));
    }

}
