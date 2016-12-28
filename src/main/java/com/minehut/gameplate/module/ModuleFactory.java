package com.minehut.gameplate.module;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.modules.arrowRemove.ArrowRemoveModuleBuilder;
import com.minehut.gameplate.module.modules.buildHeight.BuildHeightModuleBuilder;
import com.minehut.gameplate.module.modules.connection.ConnectionModuleBuilder;
import com.minehut.gameplate.module.modules.filter.FilterModuleBuilder;
import com.minehut.gameplate.module.modules.gameComplete.GameCompleteModuleBuilder;
import com.minehut.gameplate.module.modules.inventoryView.InventoryViewModuleBuilder;
import com.minehut.gameplate.module.modules.lives.LivesModuleBuilder;
import com.minehut.gameplate.module.modules.matchTimer.MatchTimerModuleBuilder;
import com.minehut.gameplate.module.modules.objectives.ObjectivesModuleBuilder;
import com.minehut.gameplate.module.modules.objectives.lastAlive.LastAliveGameObjectiveBuilder;
import com.minehut.gameplate.module.modules.observers.ObserversModuleBuilder;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.module.modules.respawn.RespawnModuleBuilder;
import com.minehut.gameplate.module.modules.spawn.SpawnModuleBuilder;
import com.minehut.gameplate.module.modules.teamManager.TeamManagerBuilder;
import com.minehut.gameplate.module.modules.timers.TimersBuilder;
import com.minehut.gameplate.module.modules.tracker.TrackerBuilder;
import com.minehut.gameplate.module.modules.visibility.VisibilityModuleBuilder;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

public class ModuleFactory {

    private Set<Class<? extends ModuleBuilder>> builderClasses;
    private final List<ModuleBuilder> builders;

    private void addBuilders() {
        this.builderClasses.addAll(Arrays.asList(
                BuildHeightModuleBuilder.class,
                TeamManagerBuilder.class,
                ArrowRemoveModuleBuilder.class,
                GameCompleteModuleBuilder.class,
                LivesModuleBuilder.class,
                RespawnModuleBuilder.class,
                ObserversModuleBuilder.class,
                MatchTimerModuleBuilder.class,
                InventoryViewModuleBuilder.class,
                TrackerBuilder.class,
                VisibilityModuleBuilder.class,
                ObjectivesModuleBuilder.class,
                LastAliveGameObjectiveBuilder.class,
                RegionModuleBuilder.class,
                ConnectionModuleBuilder.class,
                SpawnModuleBuilder.class,
                FilterModuleBuilder.class,
                TimersBuilder.class
        ));
    }

    public ModuleFactory() {
        this.builders = new ArrayList<>();
        this.builderClasses = new HashSet<>();
        this.addBuilders();
        for (Class<? extends ModuleBuilder> clazz : builderClasses) {
            try {
                builders.add(clazz.getConstructor().newInstance());
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().log(Level.SEVERE, clazz.getName() + " is an invalid ModuleBuilder.");
                e.printStackTrace();
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public ModuleCollection<Module> build(Match match, ModuleLoadTime time) {
        ModuleCollection<Module> results = new ModuleCollection<>();
        for (ModuleBuilder builder : builders) {
            try {
                if (builder.getClass().getAnnotation(BuilderData.class).load().equals(time)) {
                    try {
                        ModuleCollection moduleCollection = builder.load(match);
                        if (moduleCollection != null) {
                            results.addAll(builder.load(match));
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                if (time != ModuleLoadTime.NORMAL) ;
                else try {
                    ModuleCollection moduleCollection = builder.load(match);
                    if (moduleCollection != null) {
                        results.addAll(builder.load(match));
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return results;
    }

    public void registerBuilder(ModuleBuilder builder) {
        builders.add(builder);
    }

    public void unregisterBuilder(ModuleBuilder builder) {
        builders.remove(builder);
    }

}
