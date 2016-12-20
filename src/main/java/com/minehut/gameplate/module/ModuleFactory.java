package com.minehut.gameplate.module;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.modules.buildHeight.BuildHeightModuleBuilder;
import com.minehut.gameplate.module.modules.teamManager.TeamManagerBuilder;
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
                TeamManagerBuilder.class
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
                        results.addAll(builder.load(match));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                if (time != ModuleLoadTime.NORMAL) ;
                else try {
                    results.addAll(builder.load(match));
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
