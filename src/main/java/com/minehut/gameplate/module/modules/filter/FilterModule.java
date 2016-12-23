package com.minehut.gameplate.module.modules.filter;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;

public class FilterModule extends Module {

	private ArrayList<FilterObject> filters;
	
	public FilterModule(ArrayList<FilterObject> filters){
		this.filters = filters;
	}
		
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if(this.shouldBlock(event.getPlayer(), FilterType.ENTRY, event.getTo())){
			event.getPlayer().teleport(event.getFrom());
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.PHYSICAL)){
			if(this.shouldBlock(event.getPlayer(), FilterType.USE, event.getClickedBlock().getLocation())){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if(this.shouldBlock(event.getPlayer(), FilterType.BREAK, event.getBlock().getLocation())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(this.shouldBlock(event.getPlayer(), FilterType.PLACE, event.getBlock().getLocation())){
			event.setCancelled(true);
		}
	}
	
	private boolean shouldBlock(Player player, FilterType type, Location location){
		for(FilterObject f : this.appliedFilters(type, location)){
			if(f.getAccess().equalsIgnoreCase("deny_all_but_team")){
				if(f.getTeamId().equals("*")){
					return false;
				}
				if(!TeamManager.getTeamById(f.getTeamId()).equals(TeamManager.getTeamByPlayer(player))){
					return true;
				}
			}else if(f.getAccess().equalsIgnoreCase("allow_all_but_team")){
				if(f.getTeamId().equals("*")){
					return true;
				}
				if(TeamManager.getTeamById(f.getTeamId()).equals(TeamManager.getTeamByPlayer(player))){
					return true;
				}
			}else if(f.getAccess().equalsIgnoreCase("deny_all")){
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<FilterObject> appliedFilters(FilterType type, Location location){
		ArrayList<FilterObject> applied = new ArrayList<>();
		for(FilterObject f : this.filters){
			if(!f.getFilters().contains(type)){
				continue;
			}
			
			//check if location is in a filter region
			boolean any = false;
			for(String r : f.getRegions()){
				if(RegionModule.getRegionById(r).contains(location.toVector())){
					any = true;
					break;
				}
			};
			if(!any){
				applied.add(f);
			}
		}
		return applied;
	}
	
}
