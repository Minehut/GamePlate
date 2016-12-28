package com.minehut.gameplate.module.modules.filter.filterComparator.comparators;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.entity.Player;

import static com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse.*;

/**
 * Created by luke on 12/27/16.
 */
public class TeamComparator extends FilterComparator {
    TeamModule teamModule;

    public TeamComparator(TeamModule teamModule) {
        this.teamModule = teamModule;
    }

    @Override
    public FilterResponse evaluate(Object... objects) {

        for (Object object : objects) {
            if (object instanceof Player) {
                TeamModule playerTeam = TeamManager.getTeamByPlayer((Player) object);
                if (playerTeam == teamModule) {
                    return ALLOW;
                } else {
                    return DENY;
                }
            } else if (object instanceof TeamModule) {
                if (object == teamModule) {
                    return ALLOW;
                } else {
                    return DENY;
                }
            }
        }

        return ABSTAIN;
    }
}
