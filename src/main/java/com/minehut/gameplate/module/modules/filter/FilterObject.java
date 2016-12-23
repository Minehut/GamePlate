package com.minehut.gameplate.module.modules.filter;

import java.util.ArrayList;

public class FilterObject {
	
	private String teamId;
	private ArrayList<String> regions;
	private String access;
	private ArrayList<FilterType> filters;
	
	public FilterObject(String teamId, ArrayList<String> regions, String access, ArrayList<FilterType> filters){
		this.teamId = teamId;
		this.regions = regions;
		this.access = access;
		this.filters = filters;
	}
	
	public String getTeamId(){
		return this.teamId;
	}
	
	public ArrayList<String> getRegions(){
		return this.regions;
	}
	
	public ArrayList<FilterType> getFilters(){
		return this.filters;
	}
	
	public String getAccess(){
		return this.access;
	}
	
}
