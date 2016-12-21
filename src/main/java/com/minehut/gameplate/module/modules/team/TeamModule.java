package com.minehut.gameplate.module.modules.team;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.module.GameObjectiveModule;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.util.CachedPlayer;
import com.minehut.gameplate.util.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Lucas on 12/18/2016.
 */
public class TeamModule extends Module {

    private String id;
    private String name;
    private ChatColor color;
    private int maxPlayers;
    private int maxOverflow;
    private JoinAllowance joinAllowance;
    private List<GameObjectiveModule> objectives;
    private boolean observer;

    public enum JoinAllowance {
        ALL,
        PRE_GAME,
        MID_GAME,
        NEVER
    }


    private List<CachedPlayer> members;

    /*
     * Used for TeamType.TRADITIONAL modes.
     */

    public TeamModule(String id, String name, boolean observer, ChatColor color, int maxPlayers, int maxOverflow, JoinAllowance joinAllowance) {
        this.id = id;
        this.name = name;
        this.observer = observer;
        this.color = color;
        this.maxPlayers = maxPlayers;
        this.maxOverflow = maxOverflow;
        this.joinAllowance = joinAllowance;
        this.members = new ArrayList<>();
        this.objectives = new ArrayList<>();
    }

    /*
     * Used for TeamType.SOLO modes.
     */
    public TeamModule(String id, String name, ChatColor color, int maxPlayers, int maxOverflow, JoinAllowance joinAllowance, Player player) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.maxPlayers = maxPlayers;
        this.maxOverflow = maxOverflow;
        this.joinAllowance = joinAllowance;

        this.members = new ArrayList<>();
        this.members.add(new CachedPlayer(player));
        this.objectives = new ArrayList<>();
    }

    public void addPlayer(Player player, boolean message) {
        this.members.add(new CachedPlayer(player));

        if (message) {
            ChatUtil.sendMessage(player, ChatConstant.UI_TEAM_JOIN, this.color + this.name);
        }
    }

    public void removePlayer(Player player) {
        this.members.remove(getCachedPlayer(player));
    }

    public CachedPlayer getCachedPlayer(Player player) {
        for (CachedPlayer cachedPlayer : this.members) {
            if (cachedPlayer.getUuid().equals(player.getUniqueId())) {
                return cachedPlayer;
            }
        }
        return null;
    }

    public boolean containsPlayer(Player player) {
        return this.contains(player.getUniqueId());
    }

    public boolean contains(UUID uuid) {
        for (CachedPlayer cachedPlayer : this.members) {
            if (cachedPlayer.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMaxOverflow() {
        return maxOverflow;
    }

    public JoinAllowance getJoinAllowance() {
        return joinAllowance;
    }

    public List<CachedPlayer> getMembers() {
        return members;
    }

    public List<GameObjectiveModule> getObjectives() {
        return objectives;
    }

    public boolean isObserver() {
        return observer;
    }
}
