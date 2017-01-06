package com.minehut.gameplate.module.modules.events;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.util.Numbers;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Created by MatrixTunnel on 1/5/2017.
 */
public class EventsModule extends Module {

    public static Element parseTime(Element element) {
        for (Attribute attribute : element.getAttributes()) {
            if (attribute.getName().equals("set")) {
                GameHandler.getGameHandler().getMatch().getCurrentMap().getWorld().setTime(Numbers.parseInt(attribute.getValue()));
            } else if (attribute.getName().equalsIgnoreCase("add")) {
                GameHandler.getGameHandler().getMatch().getCurrentMap().getWorld()
                        .setTime(GameHandler.getGameHandler().getMatch().getCurrentMap().getWorld().getTime() + Numbers.parseInt(attribute.getValue()));
            }
        }

        return element;
    }
}
