package com.minehut.gameplate.tabList;

import com.google.common.collect.Lists;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.tabList.entries.*;
import com.minehut.gameplate.util.PacketUtils;
import com.mojang.authlib.GameProfile;
import com.sk89q.minecraft.util.commands.ChatColor;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by luke on 1/2/17.
 */
public class TabList extends Module {

    private static long TAB_UPDATE_TIME = 5L;

    public static int columnsPerTeam = 0;

    // Entries
    private static Map<UUID, PlayerTabEntry> fakePlayer = new HashMap<>();
    private static Map<String, TeamTabEntry> teamTitles = new HashMap<>();
    private static List<EmptyTabEntry> emptyPlayers = new ArrayList<>();

    // Views
    private static Map<Player, TabView> playerView = new HashMap<>();

    // Update handling
    private static boolean scheduledUpdate = false;
    private static Set<TabEntry> updateEntries = new HashSet<>();
    private static Set<TeamModule> teamNeedUpdate = new HashSet<>();

    public TabList() {
        Bukkit.getPluginManager().registerEvents(this, GamePlate.getInstance());
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                List<EntityPlayer> entityPlayers = new ArrayList<>();
                for (PlayerTabEntry entry : fakePlayer.values()) {
                    entityPlayers.add(entry.getEntityPlayer(null));
                }

                PacketPlayOutPlayerInfo listPacket =
                        new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, entityPlayers);
                PacketUtils.broadcastPacket(listPacket);
            }
        }, 0L, 600L);
        scheduler.scheduleSyncRepeatingTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (SkinTabEntry entry : teamTitles.values()) {
                    entry.setHat(true);
                }
            }
        }, 0L, 20L);
        scheduler.scheduleSyncRepeatingTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (SkinTabEntry entry : teamTitles.values()) {
                    entry.setHat(false);
                }
            }
        }, 5L, 20L);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void beforeCycleComplete(CycleCompleteEvent event) {
        List<String> names = Lists.newArrayList();
        for (Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        PacketUtils.broadcastPacket(getTeamPacket(names, 80, 3));
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        columnsPerTeam = Math.max(4 / (TeamManager.getTeamModules().size() - 1), 1);
        resetTeams();
        updateAll();
        for (PlayerTabEntry entry : fakePlayer.values()) {
            entry.broadcastCreateSkinParts();
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerView.put(event.getPlayer(), new TabView(event.getPlayer()));
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerView.remove(event.getPlayer());
        removePlayer(event.getPlayer());
        updateAll();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTeamChange(PlayerChangeTeamEvent event) {
        addUpdateTeam(event.getNewTeam());
        renderAllTeamTitles();
        updateAll();
    }

//    @EventHandler
//    public void onTeamChangeName(TeamNameChangeEvent event) {
//        renderTeamTitle(event.getTeam());
//    }

//    @EventHandler (priority = EventPriority.HIGHEST)
//    public void onDisplayNameChange(PlayerNameUpdateEvent event) {
//        if (!playerView.containsKey(event.getPlayer())) return;
//        addUpdateName(getPlayer(event.getPlayer()));
//    }

//    @EventHandler
//    public void onRankChange(RankChangeEvent event) {
//        if (!event.isOnline()) return;
//        addUpdateTeam(Teams.getTeamByPlayer(event.getPlayer()));
//        updateAll();
//    }

//    @EventHandler
//    public void onPlayerChangeSkinParts(PlayerSkinPartsChangeEvent event) {
//        getPlayer(event.getPlayer()).setHat(event.getPlayer().getSkinParts().contains(Skin.Part.HAT));
//    }

    private static void updateAll() {
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Bukkit.getScheduler().runTaskLaterAsynchronously(GamePlate.getInstance(), new Runnable() {
                @Override
                public void run() {
                    scheduledUpdate = false;
                    try {
                        for (TabView view : playerView.values()) view.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, TAB_UPDATE_TIME);
        }
    }

    private static void addUpdateName(TabEntry entry) {
        if (updateEntries.size() == 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {
                        Set<TabEntry> entries = new HashSet<>(updateEntries);
                        updateEntries.clear();

                        List<EntityPlayer> entityPlayers = new ArrayList<>();
                        boolean compact = false;
                        for (TabEntry entry : entries) {
                            if (entry instanceof PlayerTabEntry) {
                                entry.broadcastTabListPacket(
                                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
                            } else {
                                compact = true;
                                entityPlayers.add(entry.getEntityPlayer(null));
//                                addPlayerInfoToPacket(listPacket, entry.getPlayerInfo(null, listPacket));
                            }
                        }

                        PacketPlayOutPlayerInfo listPacket = new PacketPlayOutPlayerInfo(
                                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, entityPlayers);

                        if (compact) PacketUtils.broadcastPacket(listPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, TAB_UPDATE_TIME);
        }
        updateEntries.add(entry);
    }

    private static void addUpdateTeam(TeamModule team) {
        if (team != null) {
            teamNeedUpdate.add(team);
        }
    }

    public static boolean updateTeam(TeamModule team) {
        if (teamNeedUpdate.contains(team)) {
            teamNeedUpdate.remove(team);
            return true;
        }
        return false;
    }

    public static void renderAllTeamTitles() {
        for (TabEntry entry : teamTitles.values()) {
            addUpdateName(entry);
        }
    }

    public static void renderTeamTitle(TeamModule team) {
        if (team.isObserver()) return;
        addUpdateName(getTeam(team));
    }

    public static List<TabEntry> getTabEntries() {
        List<TabEntry> result = Lists.newArrayList();
        result.addAll(fakePlayer.values());
        result.addAll(teamTitles.values());
        result.addAll(emptyPlayers);
        return result;
    }

    public static Collection<TabView> getTabViews() {
        return playerView.values();
    }

    public static PlayerTabEntry getPlayer(Player player) {
        if (!fakePlayer.containsKey(player.getUniqueId())) {
            fakePlayer.put(player.getUniqueId(), new PlayerTabEntry(player));
        }
        return fakePlayer.get(player.getUniqueId());
    }

    private static void removePlayer(Player player) {
        if (!fakePlayer.containsKey(player.getUniqueId())) return;
        TabEntry entry = getPlayer(player);
        entry.destroy();
        fakePlayer.remove(player.getUniqueId());
    }

    public static TeamTabEntry getTeam(TeamModule team) {
        if (!teamTitles.containsKey(team.getId()))
            teamTitles.put(team.getId(), new TeamTabEntry(team));
        return teamTitles.get(team.getId());
    }

    private static void resetTeams() {
        if (teamTitles.isEmpty()) return;
        for (TabEntry entry : teamTitles.values()) {
            entry.destroy();
        }
        teamTitles.clear();
    }

    public static EmptyTabEntry getFakePlayer(TabView view) {
        for (EmptyTabEntry emptyPlayer : emptyPlayers) {
            if (view.getEmptyPlayers().contains(emptyPlayer)) continue;
            return emptyPlayer;
        }
        EmptyTabEntry fakePlayer = new EmptyTabEntry();
        emptyPlayers.add(fakePlayer);
        return fakePlayer;
    }

    public static Packet getTeamPacket(String player, int slot, int action) {
        return getTeamPacket(player == null ? Collections.<String>emptyList() : Collections.singletonList(player),
                slot, action);
    }

    public static Packet getTeamPacket(Collection<String> players, int slot, int action) {
        String team = "\000TabView" + (slot < 10 ? "0" + slot : slot);

        ScoreboardTeam scoreboardTeam = new ScoreboardTeam(new Scoreboard(), team);

        scoreboardTeam.setDisplayName(team);
        scoreboardTeam.setPrefix(ChatColor.AQUA.toString());
        scoreboardTeam.setSuffix("");
        scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        scoreboardTeam.setAllowFriendlyFire(false);
        scoreboardTeam.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.NEVER);

        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam(scoreboardTeam, action);

        try {
            Field field = PacketPlayOutScoreboardTeam.class.getDeclaredField("h");
            field.setAccessible(true);
            ((Collection<String>) field.get(packetPlayOutScoreboardTeam)).addAll(players);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return packetPlayOutScoreboardTeam;
    }

    public void sendFakePlayer(Player player, String name) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);

        PacketPlayOutPlayerInfo t = new PacketPlayOutPlayerInfo();

        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer entityPlayer = new EntityPlayer(server, world, profile, manager);

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);


    }
}
