package com.minehut.gameplate.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GamePlate;
import org.bukkit.ChatColor;

public enum ChatConstant {

    UI_WELCOME("ui.welcome"),
    UI_TEAM_JOIN("ui.teamJoin"),
    UI_TEAM_CHAT("ui.teamChat"),
    UI_POTION_EFFECTS("ui.potionEffects"),
    UI_NO_POTION_EFFECTS("ui.noPotionEffects"),
    UI_HUNGER_LEVEL("ui.hungerLevel"),
    UI_HEALTH_LEVEL("ui.healthLevel"),
    UI_CYCLING_TIMER("ui.cycleTimer"),
    UI_CYCLED_TO("ui.cycledTo"),
    UI_SECOND("ui.second"),
    UI_SECONDS("ui.seconds"),
    UI_STARTING_TIMER("ui.startingTimer"),
    UI_ROTATION_HEAD("ui.rotationHead"),
    UI_REFRESHED("ui.refreshed"),
    UI_OBJECTIVE_PREFIX("ui.objectivePrefix"),
    UI_TIME_REMAINING("ui.timeRemaining"),
    UI_NEXT_MAP_SET("ui.nextMapSet"),
    UI_CURRENTLY_SPECTATING("ui.currentlySpectating"),
    UI_DEAD("ui.dead"),
    UI_RESPAWN_TIMER("ui.respawnTimer"),
    UI_TEAM_PICKER_TITLE("ui.teamPicker"),
    UI_TEAM_PICKER_AUTOJOIN("ui.teamPickerAutoJoin"),
    UI_TEAM_PICKER_AUTOJOIN_DESC("ui.teamPickerAutoJoinDesc"),
    UI_TEAM_PICKER_AUTOJOIN_LEAVE("ui.teamPickerAutoJoinLeave"),
    UI_TEAM_PICKER_ITEM("ui.teamPickerItem"),

    GAME_NOT_YOUR_OBJECTIVE("game.notYourObjective"),
    GAME_OBJECTIVE_ALREADY_COMPLETE("game.objectiveAlreadyComplete"),
    GAME_CAPTURABLE_TOUCHED("game.capturableTouched"),
    GAME_CAPTURABLE_COMPLETED("game.capturableCompleted"),
    GAME_GOAL_SCORE("game.goalScore"),

    ERROR_JSON("error.json"),
    ERROR_INVENTORY_NOT_VIEWABLE("error.inventoryNotViewable"),
    ERROR_TEAM_FULL("error.teamFull"),
    ERROR_TEAM_OVERFLOWED("error.teamOverflowed"),
    ERROR_JOIN_SAME_TEAM("error.joinSameTeam"),
    ERROR_BUILD_HEIGHT("error.buildHeight"),
    ERROR_BLOCKED_CRAFT("error.blockedCraft"),
    ERROR_BLOCKED_PLACE("error.blockedPlace"),
    ERROR_NO_PERMISSION("error.noPermission"),
    ERROR_NUMBER_STRING("error.numberString"),
    ERROR_UNKNOWN_ERROR("error.unknownError"),
    ERROR_COMMAND_PLAYERS_ONLY("error.commandPlayerOnly"),
    ERROR_NO_TEAM_FOUND("error.noTeamFound"),
    ERROR_UNABLE_TO_CYCLE_MID_MATCH("error.unableToCycleMidMatch"),
    ERROR_MATCH_ALREADY_STARTED("error.matchAlreadyStarted"),
    ERROR_MAP_NOT_FOUND("error.mapNotFound"),
    ERROR_ALREADY_ON_TEAM("error.alreadyOnTeam"),
    ERROR_NO_MESSAGE("error.noMessage");

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
