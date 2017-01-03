package com.minehut.gameplate.tabList.entries;

import com.minehut.gameplate.tabList.TabList;
import com.minehut.gameplate.tabList.TabView;
import com.minehut.gameplate.util.PacketUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public abstract class TabEntry {

    public static final String DEFAULT_NAME = "                ";
    public static final Property DEFAULT_PROPERTY = new Property("textures", "eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=", "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=");

    protected GameProfile profile;

    protected TabEntry(GameProfile profile) {
        this.profile = profile;
    }

    protected void load() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
    }

    public void destroy() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        for (TabView view : TabList.getTabViews()) {
            view.destroy(this, -1, true);
        }
    }

    public GameProfile getProfile() {
        return profile;
    }

    public String getName() {
        return getProfile().getName();
    }

    public String getDisplayName(Player viewer) {
        return DEFAULT_NAME;
    }

    public int getPing() {
        return 1000;
    }

    protected void hide() {
        PacketUtils.broadcastPacket(TabList.getTeamPacket(profile.getName(), 80, 3));
    }

    public void setSlot(Player viewer, int i) {
        setSlot(viewer, i, 3);
    }

    public void setSlot(Player viewer, int i, int action) {
        PacketUtils.sendPacket(viewer, TabList.getTeamPacket(getName(), i, action));
    }

    public void broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtils.sendPacket(player, getTabListPacket(player, action));
        }
    }

    protected Packet getTabListPacket(Player viewer, PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
        PacketPlayOutPlayerInfo listPacket = new PacketPlayOutPlayerInfo(action, Arrays.asList(getEntityPlayer(viewer)));

//        int ping = getPing();
//
//        TabList.addPlayerInfoToPacket(listPacket, getPlayerInfo(viewer, listPacket));

        return listPacket;
    }

    public EntityPlayer getEntityPlayer(Player viewer) {
        int ping = getPing();

        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer entityPlayer = new EntityPlayer(server, world, profile, manager);

        try {
            Field field = EntityPlayer.class.getDeclaredField("displayName");
            field.setAccessible(true);
            field.set(entityPlayer, getDisplayName(viewer));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return entityPlayer;
    }

    public static GameProfile getProfile(Property texture, String name) {
        Bukkit.getLogger().log(Level.INFO, "Creating profile with name '" + name + "'");
//        GameProfile game = new GameProfile(UUID.randomUUID(), Strings.trimTo(UUID.randomUUID().toString(), 0, 8));
        GameProfile game = new GameProfile(UUID.randomUUID(), name);
        game.getProperties().put("textures", texture);
        return game;
    }

}
