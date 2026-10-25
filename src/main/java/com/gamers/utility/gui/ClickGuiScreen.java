package com.gamers.utility.gui;

import com.gamers.utility.module.Category;
import com.gamers.utility.module.Module;
import com.gamers.utility.module.ModuleManager;
import com.gamers.utility.setting.BooleanSetting;
import com.gamers.utility.setting.NumberSetting;
import com.gamers.utility.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private record Hit(int x, int y, int w, int h, Runnable left, Runnable right) {}

    private static final int SIDE = 104, ROW = 28, SETROW = 20;
    private static final int BG = 0xF0141418, SIDEBG = 0xF00E0E12, ROWBG = 0xFF1C1C22,
            ROWHOVER = 0xFF25252D, SETBG = 0xFF17171C, ACCENT = 0xFF2F7BFF,
            TEXT = 0xFFE6E6EA, DIM = 0xFF8A8A94, OFF = 0xFF3A3A44;

    private final ModuleManager manager;
    private final List<Hit> hits = new ArrayList<>();
    private Category selected = Category.COMBAT;
    private Module expanded = null;
    private int curX, curY;

    public ClickGuiScreen(ModuleManager manager) {
        super(Component.literal("Gamers Utility Cheats"));
        this.manager = manager;
    }

    private static boolean over(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private void drawSwitch(GuiGraphicsExtractor g, int x, int y, boolean on) {
        g.fill(x, y, x + 24, y + 10, on ? ACCENT : OFF);
        int kx = on ? x + 14 : x + 2;
        g.fill(kx, y + 1, kx + 8, y + 9, 0xFFFFFFFF);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
        super.extractRenderState(g, mx, my, delta);
        hits.clear();
        curX = mx; curY = my;

        int w = Math.min(420, this.width - 16);
        int h = Math.min(250, this.height - 16);
        int x0 = (this.width - w) / 2, y0 = (this.height - h) / 2;

        g.fill(x0, y0, x0 + w, y0 + h, BG);
        g.fill(x0, y0, x0 + SIDE, y0 + h, SIDEBG);
        g.text(this.font, "Gamers Utility", x0 + 10, y0 + 12, TEXT, false);
        g.text(this.font, "Cheats", x0 + 10, y0 + 24, ACCENT, false);

        int cy = y0 + 50;
        for (Category c : Category.values()) {
            boolean sel = c == selected;
            boolean hov = over(mx, my, x0, cy, SIDE, 22);
            if (sel) g.fill(x0, cy, x0 + 3, cy + 22, ACCENT);
            g.text(this.font, c.getLabel(), x0 + 14, cy + 7, (sel || hov) ? TEXT : DIM, false);
            final Category cat = c;
            hits.add(new Hit(x0, cy, SIDE, 22, () -> selected = cat, null));
            cy += 26;
        }

        int lx = x0 + SIDE + 12, lw = w - SIDE - 24, ly = y0 + 12;
        g.text(this.font, selected.getLabel(), lx, ly, TEXT, false);
        ly += 18;

        for (Module m : manager.getByCategory(selected)) {
            boolean hov = over(mx, my, lx, ly, lw, ROW);
            g.fill(lx, ly, lx + lw, ly + ROW, hov ? ROWHOVER : ROWBG);
            if (m.isEnabled()) g.fill(lx, ly, lx + 3, ly + ROW, ACCENT);
            g.text(this.font, m.getName(), lx + 12, ly + 5, TEXT, false);
            g.text(this.font, m.getDescription(), lx + 12, ly + 16, DIM, false);
            drawSwitch(g, lx + lw - 34, ly + 9, m.isEnabled());
            if (!m.getSettings().isEmpty()) {
                g.text(this.font, expanded == m ? "-" : "+", lx + lw - 48, ly + 10, DIM, false);
            }
            Runnable ex = () -> expanded = (expanded == m) ? null : m;
            hits.add(new Hit(lx, ly, lw, ROW, m::toggle, ex));
            hits.add(new Hit(lx + lw - 56, ly, 18, ROW, ex, ex));
            ly += ROW + 4;

            if (expanded == m) {
                for (Setting s : m.getSettings()) {
                    g.fill(lx + 8, ly, lx + lw, ly + SETROW, SETBG);
                    g.text(this.font, s.getName(), lx + 18, ly + 6, DIM, false);
                    if (s instanceof BooleanSetting b) {
                        drawSwitch(g, lx + lw - 34, ly + 5, b.get());
                        hits.add(new Hit(lx + 8, ly, lw - 8, SETROW, b::toggle, null));
                    } else if (s instanceof NumberSetting n) {
                        int bx = lx + lw;
                        g.text(this.font, "-", bx - 92, ly + 6, TEXT, false);
                        g.text(this.font, n.format(), bx - 76, ly + 6, TEXT, false);
                        g.text(this.font, "+", bx - 14, ly + 6, TEXT, false);
                        int barX = lx + 18, barW = lw - 30;
                        g.fill(barX, ly + SETROW - 3, barX + barW, ly + SETROW - 1, OFF);
                        g.fill(barX, ly + SETROW - 3, barX + (int) (barW * n.getFraction()), ly + SETROW - 1, ACCENT);
                        hits.add(new Hit(bx - 100, ly, 40, SETROW, n::decrease, n::decrease));
                        hits.add(new Hit(bx - 30, ly, 30, SETROW, n::increase, n::increase));
                    }
                    ly += SETROW;
                }
                ly += 4;
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mx = curX, my = curY;
        int b = event.button();
        for (int i = hits.size() - 1; i >= 0; i--) {
            Hit hit = hits.get(i);
            if (mx >= hit.x() && mx < hit.x() + hit.w() && my >= hit.y() && my < hit.y() + hit.h()) {
                if (b == 0 && hit.left() != null) hit.left().run();
                else if (b == 1 && hit.right() != null) hit.right().run();
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
