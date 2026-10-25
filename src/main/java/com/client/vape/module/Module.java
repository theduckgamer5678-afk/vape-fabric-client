package com.client.vape.module;
import com.client.vape.setting.Setting;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;
public abstract class Module {
    private final String name;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public Module(String name, Category category) { this.name = name; this.category = category; }
    public void addSettings(Setting<?>... settingsIn) { this.settings.addAll(List.of(settingsIn)); }
    public List<Setting<?>> getSettings() { return settings; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { this.enabled = !this.enabled; if (enabled) onEnable(); else onDisable(); }
    protected void onEnable() {}
    protected void onDisable() {}
    public void onTick() {}
}
