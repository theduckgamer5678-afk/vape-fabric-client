package com.client.vape.module;
public enum Category { COMBAT("Combat"), BLATANT("Blatant"), RENDER("Visual"), MOVEMENT("Movement"), UTILITY("Utility"), WORLD("World"); private final String name; Category(String name) { this.name = name; } public String getName() { return name; } }
