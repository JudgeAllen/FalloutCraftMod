package com.falloutcraft.fallout.client;

import com.falloutcraft.fallout.FalloutCraft;
import com.falloutcraft.fallout.block.FalloutBlocks;
import com.falloutcraft.fallout.client.gui.NuclearReactorScreen;
import com.falloutcraft.fallout.effect.FalloutEffects;
import com.falloutcraft.fallout.entity.FalloutEntities;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayer;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 客户端事件处理类
 * 负责注册实体渲染器和辐射屏幕覆盖层
 */
public final class ClientHandlers {

    @Mod.EventBusSubscriber(modid = FalloutCraft.MODID, value = Dist.CLIENT)
    public static final class ModBus {
        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(FalloutEntities.NUCLEAR_BOMB.get(),
                    context -> new ThrownItemRenderer<>(context, 1.0F, true));
            event.registerEntityRenderer(FalloutEntities.TACTICAL_NUKE.get(),
                    context -> new ThrownItemRenderer<>(context, 1.0F, true));
            event.registerEntityRenderer(FalloutEntities.THERMO_NUKE.get(),
                    context -> new ThrownItemRenderer<>(context, 1.0F, true));
        }
    }

    // FMLClientSetupEvent 在 MOD 总线上，需要单独的订阅者
    @Mod.EventBusSubscriber(modid = FalloutCraft.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModBusEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() ->
                net.minecraft.client.gui.screens.MenuScreens.register(
                        FalloutBlocks.REACTOR_MENU.get(), NuclearReactorScreen::new));
        }
    }

    /**
     * 初始化辐射覆盖层，在 Mod 事件总线上监听 AddGuiOverlayLayersEvent
     */
    public static void init() {
        AddGuiOverlayLayersEvent.BUS.addListener(event -> {
            ForgeLayeredDraw vanillaRoot = event.getLayeredDraw();
            vanillaRoot.add(
                    ForgeLayeredDraw.POTION_EFFECTS,
                    Identifier.tryParse(FalloutCraft.MODID + ":radiation_overlay"),
                    (ForgeLayer) (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                        Minecraft mc = Minecraft.getInstance();
                        if (mc.player == null || mc.level == null) return;

                        var holder = FalloutEffects.RADIATION.getHolder().orElse(null);
                        if (holder == null) return;
                        MobEffectInstance effect = mc.player.getEffect(holder);
                        if (effect == null) return;

                        int amplifier = effect.getAmplifier();
                        float intensity = 0.12F + (amplifier * 0.08F);
                        intensity = Math.min(intensity, 0.55F);

                        int alpha = (int) (intensity * 255);
                        int color = (alpha << 24) | 0x44FF44;

                        int screenWidth = mc.getWindow().getGuiScaledWidth();
                        int screenHeight = mc.getWindow().getGuiScaledHeight();
                        guiGraphics.fill(0, 0, screenWidth, screenHeight, color);
                    }
            );
        });
    }
}
