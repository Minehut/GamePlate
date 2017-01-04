package com.minehut.gameplate.module.modules.friendlyFire;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;

/**
 * Created by luke on 12/31/16.
 */
@BuilderData(load = ModuleLoadTime.LATEST)
public class FriendlyFireModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        boolean on = false;
        if (match.getDocument().getRootElement().getChild("friendlyFire") != null) {
            String s = match.getDocument().getRootElement().getChild("friendlyFire").getTextNormalize();
            on = s.equals("on") || s.equals("allow");
        }

        if (!on) {
            return new ModuleCollection<>(new FriendlyFireModule());
        }

        return null;
    }
}
