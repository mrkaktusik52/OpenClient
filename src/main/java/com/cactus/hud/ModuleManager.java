package com.cactus.hud;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final List<Module> modules = new ArrayList<>();

    public static void register(Module module) {
        modules.add(module);
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static <T extends Module> T getModule(Class<T> clazz) {
        for (Module module : modules) {
            if (clazz.isInstance(module)) {
                return clazz.cast(module);
            }
        }

        return null;
    }

    public static Module getModule(String id) {
        for (Module module : modules) {
            if (module.getId().equalsIgnoreCase(id)) {
                return module;
            }
        }

        return null;
    }
}