package com.minehut.gameplate.chat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocaleHandler {

    private final Set<JsonObject> documents;

    public LocaleHandler(Plugin plugin, List<String> locales) throws IOException {
        this.documents = new HashSet<>();
        JsonParser jsonParser = new JsonParser();

        for (String s : locales) {
            documents.add(jsonParser.parse(new JsonReader(new InputStreamReader(plugin.getResource(s)))).getAsJsonObject());
        }
    }

    public JsonObject getLocaleDocument(String locale) {
        for (JsonObject jsonObject : documents) {
            if (locale.equals(jsonObject.get("language").getAsString())) {
                return jsonObject;
            }
        }

        return getLocaleDocument("en");
    }

}
