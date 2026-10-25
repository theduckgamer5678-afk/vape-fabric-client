package com.client.vape.gui;

import com.client.vape.VapeClient;
import com.client.vape.module.Category;
import com.client.vape.module.Module;
import com.client.vape.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class AdvancedClickGuiScreen extends Screen {
    private final int colWidth = 120;
    private final int colHeight = 280;
    private Module activeDropdownModule = null;

    public AdvancedClickGuiScreen() { super(Component.literal("Vape Mod V4 Engine")); }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        int startX = 15; int startY = 25;
        for (Category category : Category.values()) {
            g.fill(startX, startY, startX + colWidth, startY + 16, 0xFF111111);
            g.text(this.font, category.getName(), startX + 6, startY + 4, 0xFF00D5D5, false);
            g.fill(startX, startY + 16, startX + colWidth, startY + colHeight, 0xEE0B0B0B);
            int currentY = startY + 20;
            var categoryModules = VapeClient.INSTANCE.getModuleManager().getModulesByCategory(category);
            for (Module module : categoryModules) {
                if (currentY > startY + colHeight - 12) break;
                int itemColor = module.isEnabled() ? 0xFF00D5D5 : 0xFFFFFFFF;
                g.text(this.font, module.getName(), startX + 6, currentY, itemColor, false);
                if (activeDropdownModule == module) {
                    for (Setting<?> setting : module.getSettings()) {
                        if (!setting.isVisible()) continue;
                        currentY += 12;
                        String label = setting.getName() + ": " + setting.getValue();
                        g.text(this.font, " > " + label, startX + 12, currentY, 0xFF7A7A7A, false);
                    }
                }
                currentY += 14;
            }
            startX += colWidth + 10;
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();
        int startX = 15; int startY = 25;
        for (Category category : Category.values()) {
            int currentY = startY + 20;
            var categoryModules = VapeClient.INSTANCE.getModuleManager().getModulesByCategory(category);
            for (Module module : categoryModules) {
                if (mouseX >= startX && mouseX <= startX + colWidth && mouseY >= currentY && mouseY <= currentY + 12) {
                    if (button == 0) module.toggle();
                    else if (button == 1) activeDropdownModule = (activeDropdownModule == module) ? null : module;
                    return true;
                }
                if (activeDropdownModule == module) currentY += (module.getSettings().size() * 12);
                currentY += 14;
            }
            startX += colWidth + 10;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
