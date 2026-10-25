package com.gamers.utility.setting;
import java.util.Locale;
public class NumberSetting extends Setting {
    private double value;
    private final double min, max, step;
    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name); this.value = value; this.min = min; this.max = max; this.step = step;
    }
    public double get() { return value; }
    public int getInt() { return (int) Math.round(value); }
    public float getFloat() { return (float) value; }
    public void increase() { set(value + step); }
    public void decrease() { set(value - step); }
    private void set(double v) {
        v = Math.round(v * 100.0) / 100.0;
        value = Math.max(min, Math.min(max, v));
    }
    public double getFraction() { return (value - min) / (max - min); }
    public String format() {
        if (step >= 1.0) return String.format(Locale.ROOT, "%.0f", value);
        if (step >= 0.1) return String.format(Locale.ROOT, "%.1f", value);
        return String.format(Locale.ROOT, "%.2f", value);
    }
}
