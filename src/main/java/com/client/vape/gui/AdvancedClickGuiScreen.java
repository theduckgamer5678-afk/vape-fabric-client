package com.client.vape.gui;
import com.client.vape.VapeClient;
import com.client.vape.module.*;
import com.client.vape.setting.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
public class AdvancedClickGuiScreen extends Screen {
    private final int colWidth = 120;
    private final int colHeight = 280; 
    private Module activeDropdownModule = null; 
    public AdvancedClickGuiScreen() { super(Text.of("Vape Mod V4 Engine")); }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int startX = 15; int startY = 25;
        for (Category category : Category.values()) {
            context.fill(startX, startY, startX + colWidth, startY + 16, 0xFF111111);
            context.drawText(this.textRenderer, category.getName(), startX + 6, startY + 4, 0xFF00D5D5, false);
            context.fill(startX, startY + 16, startX + colWidth, startY + colHeight, 0xEE0B0B0B);
            int currentY = startY + 20;
            var categoryModules = VapeClient.INSTANCE.getModuleManager().getModulesByCategory(category);
            for (Module module : categoryModules) {
                if (currentY > startY + colHeight - 12) break; 
                int itemColor = module.isEnabled() ? 0xFF00D5D5 : 0xFFFFFFFF;
                context.drawText(this.textRenderer, module.getName(), startX + 6, currentY, itemColor, false);
                if (activeDropdownModule == module) {
                    for (Setting<?> setting : module.getSettings()) {
                        if (!setting.isVisible()) continue;
                        currentY += 12;
                        String label = setting.getName() + ": " + setting.getValue();
                        context.drawText(this.textRenderer, " > " + label, startX + 12, currentY, 0xFF7A7A7A, false);
                    }
                }
                currentY += 14;
            }
            startX += colWidth + 10; 
        }
        super.render(context, mouseX, mouseY, delta);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    public boolean shouldPause() { return false; }
}
