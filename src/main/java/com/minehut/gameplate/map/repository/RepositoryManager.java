package com.minehut.gameplate.map.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.map.repository.repositories.DefaultRepository;
import com.minehut.gameplate.map.repository.repositories.FileRepository;
import com.minehut.gameplate.map.repository.repositories.Repository;
import com.minehut.gameplate.util.Config;
import com.minehut.gameplate.util.Strings;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositoryManager {

    private Rotation rotation = new Rotation();
    private List<Repository> repos = Lists.newArrayList();

    public void setupRotation() throws RotationLoadException {
        try {
            refreshRepos();
        } catch (IOException e) {
            throw new RotationLoadException("Could not access the repository. Make sure java has sufficient read and write privileges.");
        }
        refreshRotation();
    }

    /**
     * Refreshes the maps in the repository and the data associated with them
     *
     * @throws RotationLoadException
     */
    public void refreshRepos() throws RotationLoadException, IOException {
        List<Repository> newRepos = Lists.newArrayList(new DefaultRepository());
        for (String repo : Config.repos) {
            newRepos.add(new FileRepository(repo));
        }
        repos = newRepos;
        // Reload maps inside the repos
        for (Repository repo : repos) repo.refreshRepo();

        if (!getLoadedStream().findFirst().isPresent())
            throw new RotationLoadException("No maps were loaded. Are there any maps in the repositories?");
    }

    /**
     * Refreshes the plugin's default rotation
     */
    public void refreshRotation() throws RotationLoadException {
        try {
            rotation.refreshRotation(getLoadedStream());
        } catch (RotationLoadException | IOException e) {
            Bukkit.getLogger().log(Level.WARNING, e.getMessage());
            Bukkit.getLogger().log(Level.WARNING, "Failed to load rotation file, using a temporary rotation instead.");
            rotation.add(getLoadedStream().findFirst().get());
        }
    }

    /**
     * @return Returns the rotation
     */
    public Rotation getRotation() {
        return rotation;
    }

    public Iterable<LoadedMap> getLoadedIterable() {
        return () -> getLoadedStream().iterator();
    }

    public Stream<LoadedMap> getLoadedStream() {
        return repos.stream().flatMap(repository -> repository.getLoaded().stream());
    }

    public List<Repository> getRepos() {
        return repos;
    }

    public int getMapSize() {
        return repos.stream().mapToInt(repo -> repo.getLoaded().size()).sum();
    }

    public Repository getRepo(LoadedMap map) {
        return repos.stream().filter(repository -> repository.getLoaded().contains(map)).findFirst().orElse(null);
    }

    /**
     * @param string The name of map to be searched for
     * @return The map
     */
    public List<LoadedMap> getMap(String string) {
        return getLoadedStream().filter(map -> Strings.matchString(map.getName(), string)).collect(Collectors.toList());
    }

}
