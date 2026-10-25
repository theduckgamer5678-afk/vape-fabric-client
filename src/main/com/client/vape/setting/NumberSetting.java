package com.client.vape.setting;
import java.util.function.Supplier;
public class NumberSetting extends Setting<Double> {
    private final double min, max, inc;
    public NumberSetting(String name, double def, double min, double max, double inc) { super(name, def); this.min = min; this.max = max; this.inc = inc; }
    public NumberSetting(String name, double def, double min, double max, double inc, Supplier<Boolean> vis) { super(name, def, vis); this.min = min; this.max = max; this.inc = inc; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return inc; }
}
