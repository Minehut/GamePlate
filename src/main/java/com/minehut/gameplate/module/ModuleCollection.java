package com.minehut.gameplate.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ModuleCollection<M extends Module> extends ArrayList<M> {

    public ModuleCollection() {
    }

    public ModuleCollection(Collection<M> collection) {
        super(collection);
    }

    /**
     * @param modules The collection to create this with
     */
    @SafeVarargs
    public ModuleCollection(M... modules) {
        Collections.addAll(this, modules);
    }

    /**
     * Returns a module with matching class.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends Module> T getModule(Class<T> clazz) {
        for (M module : this) {
            if (clazz.isInstance(module)) return ((T) module);
        }
        return null;
    }

    /**
     * Returns a list of modules with matching class.
     *
     * @param clazz Class which represents the modules
     * @param <T>   Module type to be filtered
     * @return A new module
     */
    @SuppressWarnings("unchecked")
    public <T extends Module> ModuleCollection<T> getModules(Class<T> clazz) {
        ModuleCollection<T> results = new ModuleCollection<T>();
        for (Module module : this) {
            if (clazz.isInstance(module)) results.add((T) module);
        }
        return results;
    }

    /**
     * Unregister all modules
     */
    public void unregisterAll() {
        for (M module : this) {
            module.disable();
        }
    }

}
