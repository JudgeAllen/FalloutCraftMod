package com.falloutcraft.fallout;

import com.falloutcraft.fallout.block.FalloutBlocks;
import com.falloutcraft.fallout.client.ClientHandlers;
import com.falloutcraft.fallout.effect.FalloutEffects;
import com.falloutcraft.fallout.entity.FalloutEntities;
import com.falloutcraft.fallout.item.FalloutItems;
import com.falloutcraft.fallout.particle.FalloutParticles;
import com.falloutcraft.fallout.sound.FalloutSounds;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// 这里的值应该与 META-INF/mods.toml 文件中的条目匹配
@Mod(FalloutCraft.MODID)
public final class FalloutCraft {
    // 在一个公共位置定义模组 ID，供所有内容引用
    public static final String MODID = "falloutcraft";
    // 直接引用 slf4j 日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();

    // 创造模式标签延迟注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 辐射纪元创造标签
    public static final RegistryObject<CreativeModeTab> FALLOUT_TAB = CREATIVE_MODE_TABS.register("fallout_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> new ItemStack(FalloutItems.NUCLEAR_BOMB.get()))
                    .title(Component.translatable("itemGroup.falloutcraft"))
                    .displayItems((parameters, output) -> {
                        output.accept(FalloutBlocks.URANIUM_ORE_ITEM.get());
                        output.accept(FalloutItems.RAW_URANIUM.get());
                        output.accept(FalloutItems.URANIUM_INGOT.get());
                        output.accept(FalloutItems.PLUTONIUM_INGOT.get());
                        output.accept(FalloutItems.NUCLEAR_CORE.get());
                        output.accept(FalloutItems.RADIATION_HELMET.get());
                        output.accept(FalloutItems.RADIATION_CHESTPLATE.get());
                        output.accept(FalloutItems.RADIATION_LEGGINGS.get());
                        output.accept(FalloutItems.RADIATION_BOOTS.get());
                        output.accept(FalloutItems.TACTICAL_NUKE.get());
                        output.accept(FalloutItems.NUCLEAR_BOMB.get());
                        output.accept(FalloutItems.THERMO_NUKE.get());
                        output.accept(FalloutBlocks.NUCLEAR_REACTOR_ITEM.get());
                        output.accept(FalloutItems.NUCLEAR_WASTE.get());
                        output.accept(FalloutItems.GEIGER_COUNTER.get());
                        output.accept(FalloutItems.ANTI_RADIATION_PILL.get());
                    })
                    .build());

    /**
     * 快捷创建 Identifier 的辅助方法
     */
    public static Identifier id(String path) {
        return Identifier.tryParse(MODID + ":" + path);
    }

    public FalloutCraft(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        // 注册模组加载的 commonSetup 方法
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        // 注册所有延迟注册器到模组事件总线上
        FalloutBlocks.BLOCKS.register(modBusGroup);
        FalloutBlocks.BLOCK_ENTITY_TYPES.register(modBusGroup);
        FalloutBlocks.MENU_TYPES.register(modBusGroup);
        FalloutItems.ITEMS.register(modBusGroup);
        FalloutEntities.ENTITY_TYPES.register(modBusGroup);
        FalloutEffects.MOB_EFFECTS.register(modBusGroup);
        FalloutSounds.SOUND_EVENTS.register(modBusGroup);
        FalloutParticles.PARTICLE_TYPES.register(modBusGroup);
        CREATIVE_MODE_TABS.register(modBusGroup);

        // 将物品注册到创造模式标签
        BuildCreativeModeTabContentsEvent.BUS.addListener(FalloutCraft::addCreative);

        // 注册我们模组的 ForgeConfigSpec，以便 Forge 可以为我们创建和加载配置文件
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // 初始化客户端覆盖层（仅在客户端生效，但注册本身无副作用）
        ClientHandlers.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("辐射纪元 通用设置已加载");

        if (Config.logDirtBlock)
            LOGGER.info("泥土方块 >> {}", ForgeRegistries.BLOCKS.getKey(net.minecraft.world.level.block.Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("物品 >> {}", item.toString()));
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        // 目前所有物品都在自定义标签中，此处保留以供将来扩展
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("辐射纪元 客户端设置已加载");
            LOGGER.info("Minecraft 玩家名 >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
