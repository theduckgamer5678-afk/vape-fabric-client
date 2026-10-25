package com.gamers.utility;

import com.gamers.utility.gui.ClickGuiScreen;
import com.gamers.utility.module.ModuleManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class GamersUtilityClient implements ClientModInitializer {
    private ModuleManager moduleManager;
    private boolean wasDown = false;

    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            moduleManager.tick();
            boolean down = InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
            if (down && !wasDown) client.setScreenAndShow(new ClickGuiScreen(moduleManager));
            wasDown = down;
        });
    }
}
