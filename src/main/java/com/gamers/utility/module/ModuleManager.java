package com.gamers.utility.module;
import java.util.ArrayList;
import java.util.List;
public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    public ModuleManager() {
        modules.add(new Modules.KillAura());
        modules.add(new Modules.Sprint());
        modules.add(new Modules.Speed());
        modules.add(new Modules.Fly());
        modules.add(new Modules.Fullbright());
    }
    public List<Module> getModules() { return modules; }
    public List<Module> getByCategory(Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).toList();
    }
    public void tick() {
        for (Module m : modules) if (m.isEnabled()) m.onTick();
    }
}
