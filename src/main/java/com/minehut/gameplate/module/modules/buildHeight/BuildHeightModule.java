package com.minehut.gameplate.module.modules.buildHeight;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by luke on 12/19/16.
 */
public class BuildHeightModule extends Module {
    private final int height;

    protected BuildHeightModule(int height) {
        this.height = height;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.ERROR_BUILD_HEIGHT, ChatUtil.HIGHLIGHT.toString() + height + ChatColor.GRAY.toString());
        }
        if ((event.getBlock().getType().equals(Material.ACACIA_DOOR) || event.getBlock().getType().equals(Material.BIRCH_DOOR) || event.getBlock().getType().equals(Material.DARK_OAK_DOOR) || event.getBlock().getType().equals(Material.IRON_DOOR_BLOCK) || event.getBlock().getType().equals(Material.JUNGLE_DOOR) || event.getBlock().getType().equals(Material.SPRUCE_DOOR) || event.getBlock().getType().equals(Material.WOOD_DOOR) || event.getBlock().getType().equals(Material.WOODEN_DOOR) || event.getBlock().getType().equals(Material.LONG_GRASS)) && event.getBlock().getY() + 1 >= height) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();

            ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.ERROR_BUILD_HEIGHT, ChatUtil.HIGHLIGHT.toString() + height + ChatColor.GRAY.toString());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.ERROR_BUILD_HEIGHT, ChatUtil.HIGHLIGHT.toString() + height + ChatColor.GRAY.toString());
        }
    }

    @EventHandler
    public void onEmptyBucket(PlayerBucketEmptyEvent event) {
        Block toFill = event.getBlockClicked().getRelative(event.getBlockFace());
        if (toFill.getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.ERROR_BUILD_HEIGHT, ChatUtil.HIGHLIGHT.toString() + height + ChatColor.GRAY.toString());
        }
    }

    @EventHandler
    public void onFillBucket(PlayerBucketFillEvent event) {
        Block toEmpty = event.getBlockClicked().getRelative(event.getBlockFace());
        if (toEmpty.getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.ERROR_BUILD_HEIGHT, ChatUtil.HIGHLIGHT.toString() + height + ChatColor.GRAY.toString());
        }
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        Set<BlockState> blocksAboveHeight = new HashSet<>();
        for (BlockState blockState : event.getBlocks()) {
            if (blockState.getY() >= height) {
                blocksAboveHeight.add(blockState);
            }
        }
        for (BlockState blockState : blocksAboveHeight) {
            event.getBlocks().remove(blockState);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.getBlock().getRelative(event.getDirection()).getY() >= height){
            event.setCancelled(true);
        } else {
            for (Block block : event.getBlocks()) {
                if (block.getRelative(event.getDirection()).getY() >= height) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.getRelative(event.getDirection()).getY() >= height) {
                event.setCancelled(true);
            }
        }
    }
}
