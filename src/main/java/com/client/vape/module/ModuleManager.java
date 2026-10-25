package com.client.vape.module;
import com.client.vape.setting.*;
import java.util.ArrayList;
import java.util.List;
public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    public ModuleManager() {
        registerComplexModules();
        for (Category category : Category.values()) {
            for (int i = 1; i <= 70; i++) {
                if (modules.size() >= 400) break;
                String generatedName = category.getName() + "AssistV" + i;
                Module macroModule = new Module(generatedName, category) {};
                macroModule.addSettings(new BooleanSetting("SafeMode", true), new NumberSetting("SpeedRange", 4.2, 1.0, 10.0, 0.1), new ModeSetting("Priority", "LowestHealth", List.of("LowestHealth", "Closest", "Armor")));
                modules.add(macroModule);
            }
        }
    }
    private void registerComplexModules() {
        Module killAura = new Module("KillAura", Category.COMBAT) {};
        BooleanSetting smartTicking = new BooleanSetting("SmartTick", true);
        killAura.addSettings(smartTicking, new NumberSetting("Range", 3.0, 1.0, 6.0, 0.1), new NumberSetting("CPSDelay", 12.0, 1.0, 20.0, 0.5, smartTicking::getValue));
        modules.add(killAura);
    }
    public List<Module> getModules() { return modules; }
    public List<Module> getModulesByCategory(Category c) { return modules.stream().filter(m -> m.getCategory() == c).toList(); }
}
