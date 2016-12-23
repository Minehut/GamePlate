package com.minehut.gameplate.module.modules.filter;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

public class FilterModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

    	ArrayList<FilterObject> filterList = new ArrayList<>();
    	
        if (match.getJson().has("filters")) {
            JsonArray array = match.getJson().get("filters").getAsJsonArray();
            List<JsonObject> filters = new ArrayList<>();
            array.forEach(s -> filters.add(s.getAsJsonObject()));
            for(JsonObject f : filters){
            	String teamId = "";
            	String access = "";
        		ArrayList<String> regions = new ArrayList<>();
        		ArrayList<FilterType> types = new ArrayList<>();
            	if(f.has("team")){
            		teamId = f.get("team").getAsString();
            	}
            	if(f.has("access")){
            		access = f.get("access").getAsString();
            	}else{
            		continue;
            	}
            	if(f.has("regions")){
            		JsonArray regionsJson = f.get("regions").getAsJsonArray();
            		regionsJson.forEach(rj -> regions.add(rj.getAsString()));
            	}else{
            		continue;
            	}
            	if(f.has("types")){
            		JsonArray typesJson = f.get("types").getAsJsonArray();
            		typesJson.forEach(rj -> {
            			if(!this.getValue(rj.getAsString()).equals("")){
            				types.add(FilterType.valueOf(this.getValue(rj.getAsString())));
            			}
            		});
            	}else{
            		continue;
            	}
            	filterList.add(new FilterObject(teamId, regions, access, types));
            }
            return new ModuleCollection<>(new FilterModule(filterList));
        }

        return null;
    }
    
    private String getValue(String s){
    	s = s.replaceAll(" ", "_");
    	for(FilterType ft : FilterType.values()){
    		if(ft.toString().equalsIgnoreCase(s)){
    			return ft.toString();
    		}
    	}
    	return "";
    }
	
}
