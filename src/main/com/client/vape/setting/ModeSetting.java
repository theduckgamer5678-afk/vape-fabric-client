package com.client.vape.setting;
import java.util.List;
import java.util.function.Supplier;
public class ModeSetting extends Setting<String> {
    private final List<String> modes;
    public ModeSetting(String name, String def, List<String> modes) { super(name, def); this.modes = modes; }
    public ModeSetting(String name, String def, List<String> modes, Supplier<Boolean> vis) { super(name, def, vis); this.modes = modes; }
    public List<String> getModes() { return modes; }
}
