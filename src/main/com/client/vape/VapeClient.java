package com.client.vape;

import com.client.vape.gui.AdvancedClickGuiScreen;
import com.client.vape.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class VapeClient implements ModInitializer {
    public static VapeClient INSTANCE;
    private ModuleManager moduleManager;

    @Override
    public void onInitialize() {
        INSTANCE = this;
        this.moduleManager = new ModuleManager();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            moduleManager.getModules().stream()
                .filter(com.client.vape.module.Module::isEnabled)
                .forEach(com.client.vape.module.Module::onTick);
            
            if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
                if (!(client.currentScreen instanceof AdvancedClickGuiScreen)) {
                    client.setScreen(new AdvancedClickGuiScreen());
                }
            }
        });
    }

    public ModuleManager getModuleManager() { return moduleManager; }
}
