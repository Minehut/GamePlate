package com.minehut.gameplate.module;

import com.minehut.gameplate.module.modules.team.TeamModule;

/**
 * Created by luke on 12/20/16.
 */
public class GameObjectiveModule extends Module {
    private String id;
    private String name;
    private Boolean showOnScoreboard;
    private TeamModule completedBy = null;

    public GameObjectiveModule(String id, String name, Boolean showOnScoreboard) {
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

    public boolean isCompleted() {
        return this.completedBy == null;
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

    public TeamModule getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(TeamModule completedBy) {
        this.completedBy = completedBy;
    }
}
