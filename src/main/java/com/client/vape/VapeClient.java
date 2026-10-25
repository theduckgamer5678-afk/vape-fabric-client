package com.client.vape;

import com.client.vape.gui.AdvancedClickGuiScreen;
import com.client.vape.module.ModuleManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class VapeClient implements ClientModInitializer {
    public static VapeClient INSTANCE;
    private ModuleManager moduleManager;
    private boolean wasDown = false;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        this.moduleManager = new ModuleManager();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            moduleManager.getModules().stream()
                .filter(com.client.vape.module.Module::isEnabled)
                .forEach(com.client.vape.module.Module::onTick);

            boolean down = InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
            if (down && !wasDown) {
                client.setScreenAndShow(new AdvancedClickGuiScreen());
            }
            wasDown = down;
        });
    }

    public ModuleManager getModuleManager() { return moduleManager; }
}
