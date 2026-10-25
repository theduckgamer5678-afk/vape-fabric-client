package com.client.vape.setting;
import java.util.function.Supplier;
public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean defaultValue) { super(name, defaultValue); }
    public BooleanSetting(String name, boolean def, Supplier<Boolean> vis) { super(name, def, vis); }
}
