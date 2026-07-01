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

    // ==================== 炸弹配置（三级） ====================
    private static final ForgeConfigSpec.IntValue BOMB_EXPLOSION_RADIUS = BUILDER
            .comment("标准核弹的爆炸半径")
            .defineInRange("bomb.explosionRadius", 25, 5, 100);

    private static final ForgeConfigSpec.IntValue TACTICAL_EXPLOSION_RADIUS = BUILDER
            .comment("战术核弹的爆炸半径")
            .defineInRange("bomb.tacticalExplosionRadius", 10, 3, 30);

    private static final ForgeConfigSpec.IntValue THERMO_EXPLOSION_RADIUS = BUILDER
            .comment("热核弹的爆炸半径")
            .defineInRange("bomb.thermoExplosionRadius", 50, 20, 150);

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

    // ==================== 反应堆配置 ====================
    private static final ForgeConfigSpec.IntValue REACTOR_FUEL_BURN_TIME = BUILDER
            .comment("铀锭在反应堆中的燃烧时间（刻）")
            .defineInRange("reactor.fuelBurnTime", 2400, 400, 12000);

    private static final ForgeConfigSpec.IntValue REACTOR_HEAT_RATE = BUILDER
            .comment("反应堆每tick升温量")
            .defineInRange("reactor.heatRate", 1, 1, 20);

    private static final ForgeConfigSpec.IntValue REACTOR_COOL_RATE = BUILDER
            .comment("反应堆无燃料时每tick自然降温量")
            .defineInRange("reactor.coolRate", 1, 0, 20);

    private static final ForgeConfigSpec.IntValue REACTOR_MELTDOWN_TEMP = BUILDER
            .comment("反应堆熔毁温度阈值")
            .defineInRange("reactor.meltdownTemp", 800, 200, 1000);

    private static final ForgeConfigSpec.IntValue REACTOR_WASTE_INTERVAL = BUILDER
            .comment("反应堆产生核废料的间隔（刻）")
            .defineInRange("reactor.wasteInterval", 600, 100, 2400);

    private static final ForgeConfigSpec.IntValue REACTOR_COOLANT_COOLDOWN = BUILDER
            .comment("冷却液消耗的冷却时间（刻），防止瞬间吃完")
            .defineInRange("reactor.coolantCooldown", 40, 5, 200);

    private static final ForgeConfigSpec.IntValue REACTOR_ICE_COOLING = BUILDER
            .comment("一个冰块能吸收的热量（每1=抵挡1秒升温），默认30 → 1铀锭=20冰")
            .defineInRange("reactor.iceCooling", 30, 1, 200);

    private static final ForgeConfigSpec.IntValue REACTOR_BLUE_ICE_COOLING = BUILDER
            .comment("一个蓝冰能吸收的热量，默认240 → 1铀锭≈2.5蓝冰")
            .defineInRange("reactor.blueIceCooling", 240, 10, 1000);

    private static final ForgeConfigSpec.IntValue REACTOR_WATER_COOLING = BUILDER
            .comment("水桶瞬时降温量")
            .defineInRange("reactor.waterCooling", 300, 50, 1000);

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
    public static int tacticalExplosionRadius;
    public static int thermoExplosionRadius;
    public static int reactorFuelBurnTime;
    public static int reactorHeatRate;
    public static int reactorCoolRate;
    public static int reactorMeltdownTemp;
    public static int reactorWasteInterval;
    public static int reactorCoolantCooldown;
    public static int reactorIceCooling;
    public static int reactorBlueIceCooling;
    public static int reactorWaterCooling;
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
        tacticalExplosionRadius = TACTICAL_EXPLOSION_RADIUS.get();
        thermoExplosionRadius = THERMO_EXPLOSION_RADIUS.get();
        reactorFuelBurnTime = REACTOR_FUEL_BURN_TIME.get();
        reactorHeatRate = REACTOR_HEAT_RATE.get();
        reactorCoolRate = REACTOR_COOL_RATE.get();
        reactorMeltdownTemp = REACTOR_MELTDOWN_TEMP.get();
        reactorWasteInterval = REACTOR_WASTE_INTERVAL.get();
        reactorCoolantCooldown = REACTOR_COOLANT_COOLDOWN.get();
        reactorIceCooling = REACTOR_ICE_COOLING.get();
        reactorBlueIceCooling = REACTOR_BLUE_ICE_COOLING.get();
        reactorWaterCooling = REACTOR_WATER_COOLING.get();
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
