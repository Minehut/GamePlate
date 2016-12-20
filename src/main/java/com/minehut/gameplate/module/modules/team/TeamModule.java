package com.minehut.gameplate.module.modules.team;

import com.minehut.gameplate.module.Module;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by Lucas on 12/18/2016.
 */
public class TeamModule extends Module {

    private String id;
    private String name;
    private ChatColor color;

    public TeamModule(String id, String name, ChatColor color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

}
