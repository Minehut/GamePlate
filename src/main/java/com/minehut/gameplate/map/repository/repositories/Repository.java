package com.minehut.gameplate.map.repository.repositories;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.map.Contributor;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.util.Config;
import com.minehut.gameplate.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public abstract class Repository {

    private static final List<String> requirements = Arrays.asList("map.json", "region", "level.dat");

    private List<LoadedMap> loaded = Lists.newArrayList();
    private Map<String, File> includes = new HashMap<>();
    private String path;
    private File root;
    private int id = -1;

    private static int maxId = 0;

    public int getId() {
        return id;
    }

    public String getSource(boolean show) {
        return show ? path : null;
    }

    protected String getPath() {
        return path;
    }

    public File getRoot() {
        return root;
    }

    public String toChatMessage(boolean op) {
        String source = getSource(op);
        return ChatColor.YELLOW + "#" + getId() + " " + ChatColor.GOLD +
                (source == null ? ChatColor.STRIKETHROUGH + "Hidden" : source);
    }

    public File getInclude(String name) {
        return includes.containsKey(name) ? includes.get(name) : null;
    }

    Repository(String path) throws RotationLoadException, IOException {
        this.path = path;
        this.root = new File(path);
    }

    public void refreshRepo() throws RotationLoadException, IOException {
        if (id == -1) id = maxId++;
        includes.clear();
        root = new File(path);
        if (!root.exists()) root.mkdirs();
        loadIncludes(root);

        loaded = loadMapsIn(root);
    }

    private void loadIncludes(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                if (!child.isFile()) {
                    loadIncludes(child);
                }
            }
        }
    }

    private List<LoadedMap> loadMapsIn(File file) {
        List<LoadedMap> result = new LinkedList<>();
        if (file == null || file.listFiles() == null) {
            return result;
        }
        for (File map : file.listFiles()) {
            if (map.isFile() || map.list() == null) continue;
            if (Arrays.asList(map.list()).containsAll(requirements)) {
                try {
                    result.add(loadMap(map));
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to load map at " + map.getAbsolutePath());
                    if (Config.displayMapLoadErrors) {
                        Bukkit.getLogger().log(Level.INFO, "Showing error, this can be disabled in the config: ");
                        e.printStackTrace();
                    }
                }
            } else {
                result.addAll(loadMapsIn(map));
            }
        }
        return result;
    }

    private LoadedMap loadMap(File map) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File(map, "map.xml"));

        String name = document.getRootElement().getChild("name").getTextNormalize();
        String version = document.getRootElement().getChild("version").getTextNormalize();

        String objective = "";
        if (document.getRootElement().getChild("objective") != null) {
            objective = document.getRootElement().getChild("objective").getTextNormalize();
        }

        List<Contributor> authors = new ArrayList<>();
        for (Element authorsElement : document.getRootElement().getChildren("authors")) {
            for (Element author : authorsElement.getChildren()) {
                authors.add(parseContributor(author));
            }
        }

        List<Contributor> contributors = new ArrayList<>();
        for (Element contributorsElement : document.getRootElement().getChildren("contributors")) {
            for (Element contributor : contributorsElement.getChildren()) {
                contributors.add(parseContributor(contributor));
            }
        }

        List<String> rules = new ArrayList<>();
        for (Element rulesElement : document.getRootElement().getChildren("rules")) {
            for (Element rule : rulesElement.getChildren()) {
                rules.add(rule.getTextNormalize());
            }
        }

        return new LoadedMap(name, version, objective, authors, contributors, rules, map, document);
    }

    /**
     * @return Returns all loaded maps
     */
    public List<LoadedMap> getLoaded() {
        return loaded;
    }

    private static Contributor parseContributor(Element element) {
        return new Contributor(UUID.fromString(element.getAttributeValue("uuid")), element.getAttributeValue("contribution"));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " id:" + id + ", Path:" + path;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass().equals(this.getClass()) && ((Repository) other).path.equals(this.path);
    }

    public static String getNewRepoPath(String repoName) {
        String reposPath = GamePlate.getInstance().getDataFolder().getAbsolutePath() +
                File.separator + "repositories" + File.separator + repoName;
        File repo = new File(reposPath);
        if (!repo.exists()) repo.mkdirs();
        return reposPath;
    }

}
