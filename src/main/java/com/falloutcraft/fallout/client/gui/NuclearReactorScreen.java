package com.falloutcraft.fallout.client.gui;

import com.falloutcraft.fallout.inventory.NuclearReactorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class NuclearReactorScreen extends AbstractContainerScreen<NuclearReactorMenu> {
    private static final Identifier TEXTURE =
            Identifier.tryParse("falloutcraft:textures/gui/container/nuclear_reactor.png");

    public NuclearReactorScreen(NuclearReactorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics g, float delta, int mx, int my) {
        int x = leftPos, y = topPos;
        // 画背景
        g.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256F, 256F);

        // 火焰动画 (13px)
        int burn = menu.getBurnProgress();
        if (burn > 0)
            g.blit(TEXTURE, x + 56, y + 36 + 13 - burn, 176, 13 - burn, 14, burn, 256F, 256F);

        // 温度条 (60px)
        int temp = Math.min(menu.getTemperature() * 60 / 1000, 60);
        if (temp > 0)
            g.fill(x + 152, y + 8 + 60 - temp, x + 164, y + 68, 0xFFFF6600);
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        super.render(g, mx, my, delta); // super 负责画槽位、物品、tooltip
        this.renderTooltip(g, mx, my);

        int x = leftPos, y = topPos;
        int temp = menu.getTemperature();

        // 状态文字
        String status = temp >= 800 ? "熔毁!" : temp > 200 ? "运行中" : "空闲";
        int color = temp >= 800 ? 0xFF3333 : temp > 200 ? 0x33FF33 : 0xAAAAAA;
        g.drawString(font, status, x + 8, y + 6, color);

        // 温度数字
        g.drawString(font, temp + "C", x + 80, y + 70, 0xCCCCCC);
    }
}
