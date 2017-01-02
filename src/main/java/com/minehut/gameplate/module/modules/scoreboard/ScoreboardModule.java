package com.minehut.gameplate.module.modules.scoreboard;

import com.google.common.base.Strings;
import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.event.TeamCreateEvent;
import com.minehut.gameplate.event.objective.ObjectiveCompleteEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.objectives.score.ScoreObjectiveModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.SimpleScoreboard;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luke on 12/31/16.
 */
public class ScoreboardModule extends Module {
    private TeamModule teamModule;
    private SimpleScoreboard simpleScoreboard;

    private HashMap<Module, Integer> lines = new HashMap<>();
    private List<ObjectiveModule> compact = new ArrayList<>();

    /*
     * Each team initializes its own ScoreboardModule.
     */

    public ScoreboardModule(TeamModule teamModule) {
        this.teamModule = teamModule;

        this.simpleScoreboard = new SimpleScoreboard(ChatUtil.HIGHLIGHT + "Objectives");

        for (TeamModule other : TeamManager.getTeamModules()) {
            setupTeam(other);
        }

        for (Player player : teamModule.getPlayers()) {
            simpleScoreboard.send(player);
        }
    }

    @EventHandler
    public void onCycle(CycleCompleteEvent event) {
        render();
    }

    public void refresh(ObjectiveModule objectiveModule) {
        this.simpleScoreboard.remove(this.lines.get(objectiveModule));
        renderLine(this.lines.get(objectiveModule));
    }

    public void render() {
        for (Element scoreboardElement : GameHandler.getGameHandler().getMatch().getDocument().getRootElement().getChildren("scoreboard")) {

            int line = 1;

            for (Element element : scoreboardElement.getChildren()) {
                if (element.getName().equalsIgnoreCase("team")) {
                    line += 1;
                } else if (element.getName().equalsIgnoreCase("objective")) {
                    ObjectiveModule objectiveModule = ObjectiveModule.getObjective(element.getAttributeValue("id"));
                    if (objectiveModule instanceof ScoreObjectiveModule) {
                        line += TeamManager.getTeamModules().size() - 1;
                    }
                } else if (element.getName().equalsIgnoreCase("space")) {
                    line += 1;
                }
            }

            int spaceCount = 1;

            boolean lastCompact = false;

            for(int i = 0; i < scoreboardElement.getChildren().size(); i++) {
                Element element = scoreboardElement.getChildren().get(i);
                if (element.getName().equalsIgnoreCase("team")) {
                    TeamModule teamModule = TeamManager.getTeamById(element.getAttributeValue("id"));
                    simpleScoreboard.add(teamModule.getColor() + teamModule.getName(), line);
                    line--;
                    lastCompact = false;
                }
                else if (element.getName().equalsIgnoreCase("objective")) {

                    boolean compact = false;
                    if (element.getAttribute("compact") != null) {
                        try {
                            compact = element.getAttribute("compact").getBooleanValue();
                        } catch (DataConversionException e) {
                            e.printStackTrace();
                        }
                    }
                    lastCompact = compact;


                    ObjectiveModule objectiveModule = ObjectiveModule.getObjective(element.getAttributeValue("id"));

                    this.lines.put(objectiveModule, line);
                    if (compact) {
                        this.compact.add(objectiveModule);
                    }

                    line -= renderObjective(objectiveModule);
                }
                else if (element.getName().equalsIgnoreCase("space")) {

                    if (lastCompact) {
                        line--;
                    }
                    lastCompact = false;

                    simpleScoreboard.add(Strings.repeat(" ", spaceCount), line);
                    spaceCount++;
                    line--;
                } else if (element.getName().equalsIgnoreCase("text")) {
                    String text = ChatColor.translateAlternateColorCodes('*', element.getTextNormalize());
                    simpleScoreboard.add(text, line);
                    line--;
                }
            }
        }
        simpleScoreboard.update();
    }

    /*
     * returns lines used
     */
    public int renderObjective(ObjectiveModule objectiveModule) {
        boolean compact = this.compact.contains(objectiveModule);
        int line = this.lines.get(objectiveModule);
        int used = 0;

        if (compact) {
            String existing = "";
            if (simpleScoreboard.get(line) != null) {
                existing = simpleScoreboard.get(line);
            }
            simpleScoreboard.add(existing + objectiveModule.getScoreboardCompactDisplay(), line);
        } else {
            if (objectiveModule instanceof ScoreObjectiveModule) {
                for (TeamModule teamModule : ((ScoreObjectiveModule) objectiveModule).getScores().keySet()) {
                    if(teamModule.isObserver()) continue;

                    simpleScoreboard.remove(line - used);
                    simpleScoreboard.add(((ScoreObjectiveModule) objectiveModule).getScore(teamModule) + " " + teamModule.getColor() + teamModule.getName(), line - used);
                    used++;
                }
            } else {
                simpleScoreboard.add(objectiveModule.getScoreboardDisplay(), line);
                used++;
            }
        }
        return used;
    }

    public void renderLine(int line) {
        for (Module module : this.lines.keySet()) {
            if (module instanceof ObjectiveModule) {
                if (lines.get(module) == line) {
                    renderObjective((ObjectiveModule) module);
                }
            }
        }
        simpleScoreboard.update();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        refresh(event.getObjective());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeamCreate(TeamCreateEvent event) {
        this.setupTeam(event.getTeamModule());
        render();
    }

    private void setupTeam(TeamModule teamModule) {
        Team team = this.simpleScoreboard.getScoreboard().registerNewTeam(teamModule.getId());
        team.setDisplayName(teamModule.getName());
        team.setPrefix(teamModule.getColor() + "[" + teamModule.getName().substring(0, 1) + "] ");
        team.setCanSeeFriendlyInvisibles(true);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        for (Player player : teamModule.getPlayers()) {
            team.addPlayer(player);
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        Team team = this.simpleScoreboard.getScoreboard().getTeam(event.getNewTeam().getId());

        if (event.getOldTeam() != null) {
            Team oldTeam = this.simpleScoreboard.getScoreboard().getTeam(event.getOldTeam().getId());
            if (oldTeam != null) {
                oldTeam.removePlayer(event.getPlayer());
            }
        }

        if (team != null) {
           team.addPlayer(event.getPlayer());
        }

        if (event.getNewTeam().getId().equals(this.teamModule.getId())) {
            simpleScoreboard.send(event.getPlayer());
        }
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);

        for (Team team : this.simpleScoreboard.getScoreboard().getTeams()) {
            team.unregister();
        }
        this.simpleScoreboard = null;
    }

    public TeamModule getTeamModule() {
        return teamModule;
    }

    public SimpleScoreboard getSimpleScoreboard() {
        return simpleScoreboard;
    }
}
