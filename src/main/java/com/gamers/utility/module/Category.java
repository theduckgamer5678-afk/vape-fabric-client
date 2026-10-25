package com.gamers.utility.module;
public enum Category {
    COMBAT("Combat"), MOVEMENT("Movement"), VISUAL("Visual");
    private final String label;
    Category(String label) { this.label = label; }
    public String getLabel() { return label; }
}
