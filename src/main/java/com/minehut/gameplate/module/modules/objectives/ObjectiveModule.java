package com.minehut.gameplate.module.modules.objectives;

import com.minehut.gameplate.event.objective.ObjectiveCompleteEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/20/16.
 */
public class ObjectiveModule extends Module {
    private String id;
    private String name;
    private Boolean showOnScoreboard;
    private List<TeamModule> completedBy = new ArrayList<>();

    public ObjectiveModule(String id, String name, Boolean showOnScoreboard) {
        this.id = id;
        this.name = name;
        this.showOnScoreboard = showOnScoreboard;
    }

    /*
     * Any objective that is displayed
     * on the scoreboard should override this.
     */
    public String getScoreboardString() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getShowOnScoreboard() {
        return showOnScoreboard;
    }

    public List<TeamModule> getCompletedBy() {
        return completedBy;
    }

    public void addCompletedBy(TeamModule teamModule) {
        this.completedBy.add(teamModule);

        ObjectiveCompleteEvent objectiveCompleteEvent = new ObjectiveCompleteEvent(this, teamModule);
        Bukkit.getPluginManager().callEvent(objectiveCompleteEvent);
    }

    public boolean isCompletedBy(TeamModule teamModule) {
        return this.completedBy.contains(teamModule);
    }
}
