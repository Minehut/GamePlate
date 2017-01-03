package com.minehut.gameplate.tabList.entries;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.tabList.TabList;
import com.minehut.gameplate.tabList.TabView;
import com.minehut.gameplate.util.PacketUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class SkinTabEntry extends TabEntry {

//    private int id = Bukkit.allocateEntityId();
    private int id = 9999934;
    private boolean hat = false;

    public SkinTabEntry(GameProfile profile) {
        super(profile);
    }

    protected void load() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        broadcastCreateSkinParts();
    }

    public void destroy() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        PacketUtils.broadcastPacket(deleteSkinParts());
        for (TabView view : TabList.getTabViews()) {
            view.destroy(this, -1, true);
        }
    }

    public void broadcastCreateSkinParts() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.broadcastPacket(createSkinPartsPacket());
            }
        });
    }

    public void createSkinParts(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.sendPacket(player, createSkinPartsPacket());
            }
        });
    }

    public void setHat(boolean hat) {
        if (hat == this.hat) {
            return;
        }
        this.hat = hat;
        updateSkinParts();
    }

    private List<DataWatcher.Item<?>> getDataList() {
//        return Watchers.toList(hat ? Watchers.HAT_ON : Watchers.HAT_OFF);
        return null;
    }

    private void updateSkinParts() {
//        PacketUtils.broadcastPacket(PacketUtils.createMetadataPacket(id, getDataList()));
    }

    private Packet createSkinPartsPacket() {
        UUID uuid = getProfile().getId();
        EntityHuman entityHuman = new EntityPlayer(
                MinecraftServer.getServer(),
                MinecraftServer.getServer().getWorldServer(0),
                new GameProfile(uuid, uuid.toString()),
                new PlayerInteractManager(MinecraftServer.getServer().getWorldServer(0)));
        return new PacketPlayOutNamedEntitySpawn(entityHuman);
    }

    private Packet deleteSkinParts() {
        return new PacketPlayOutEntityDestroy(id);
    }

}
