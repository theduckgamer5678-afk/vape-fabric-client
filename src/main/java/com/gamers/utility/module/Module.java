package com.gamers.utility.module;
import com.gamers.utility.setting.Setting;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
public abstract class Module {
    protected final Minecraft mc = Minecraft.getInstance();
    private final String name, description;
    private final Category category;
    private final List<Setting> settings = new ArrayList<>();
    private boolean enabled;
    protected Module(String name, String description, Category category) {
        this.name = name; this.description = description; this.category = category;
    }
    protected <T extends Setting> T add(T s) { settings.add(s); return s; }
    public List<Setting> getSettings() { return settings; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { enabled = !enabled; if (enabled) onEnable(); else onDisable(); }
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
