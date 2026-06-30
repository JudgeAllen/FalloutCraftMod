package com.falloutcraft.fallout;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = FalloutCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // ==================== 炸弹配置 ====================
    private static final ForgeConfigSpec.IntValue BOMB_EXPLOSION_RADIUS = BUILDER
            .comment("核弹的爆炸半径")
            .defineInRange("bomb.explosionRadius", 25, 5, 100);

    // ==================== 辐射云配置 ====================
    private static final ForgeConfigSpec.IntValue RADIATION_CLOUD_RADIUS = BUILDER
            .comment("残留辐射云的半径")
            .defineInRange("radiation.cloudRadius", 15, 5, 50);

    private static final ForgeConfigSpec.IntValue RADIATION_CLOUD_DURATION = BUILDER
            .comment("辐射云的持续时间（刻，20刻 = 1秒）")
            .defineInRange("radiation.cloudDuration", 6000, 100, 120000);

    // ==================== 辐射效果配置 ====================
    private static final ForgeConfigSpec.IntValue RADIATION_DAMAGE_INTERVAL = BUILDER
            .comment("辐射伤害的间隔（刻）")
            .defineInRange("radiation.damageInterval", 40, 5, 200);

    private static final ForgeConfigSpec.DoubleValue RADIATION_DAMAGE_AMOUNT = BUILDER
            .comment("每次辐射伤害的生命值")
            .defineInRange("radiation.damageAmount", 1.0, 0.5, 10.0);

    // ==================== 旧配置保留 ====================
    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("是否在通用设置时记录泥土方块")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("一个魔法数字")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("你希望魔法数字的介绍信息是什么")
            .define("magicNumberIntroduction", "The magic number is... ");

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("在通用设置时要记录的物品列表。")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    // ==================== 公共字段 ====================
    public static int bombExplosionRadius;
    public static int radiationCloudRadius;
    public static int radiationCloudDuration;
    public static int radiationDamageInterval;
    public static double radiationDamageAmount;
    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(Identifier.tryParse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        bombExplosionRadius = BOMB_EXPLOSION_RADIUS.get();
        radiationCloudRadius = RADIATION_CLOUD_RADIUS.get();
        radiationCloudDuration = RADIATION_CLOUD_DURATION.get();
        radiationDamageInterval = RADIATION_DAMAGE_INTERVAL.get();
        radiationDamageAmount = RADIATION_DAMAGE_AMOUNT.get();
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(Identifier.tryParse(itemName)))
                .collect(Collectors.<Item>toSet());
    }
}
