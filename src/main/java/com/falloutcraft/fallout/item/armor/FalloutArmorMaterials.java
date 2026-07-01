package com.falloutcraft.fallout.item.armor;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;

/**
 * 防辐射装甲材料定义
 * 钻石级防护 + 辐射免疫
 */
public final class FalloutArmorMaterials {

    /** 铀锭修复材料标签 */
    public static final TagKey<Item> URANIUM_REPAIR =
            TagKey.create(Registries.ITEM, Identifier.tryParse("falloutcraft:repair_radiation"));

    /** 装备资源注册表键 */
    private static final ResourceKey<net.minecraft.core.Registry<EquipmentAsset>> EQUIPMENT_REGISTRY =
            ResourceKey.createRegistryKey(Identifier.tryParse("minecraft:equipment_asset"));

    /** 防辐射装甲资源键 */
    public static final ResourceKey<EquipmentAsset> EQUIPMENT_KEY =
            ResourceKey.create(EQUIPMENT_REGISTRY, Identifier.tryParse("falloutcraft:radiation"));

    // 护甲值
    public static final int HELMET_DEFENSE = 3;
    public static final int CHESTPLATE_DEFENSE = 8;
    public static final int LEGGINGS_DEFENSE = 6;
    public static final int BOOTS_DEFENSE = 3;

    // 耐久
    public static final int DURABILITY_MULTIPLIER = 33;
    public static final int HELMET_DURABILITY = 11 * DURABILITY_MULTIPLIER;
    public static final int CHESTPLATE_DURABILITY = 16 * DURABILITY_MULTIPLIER;
    public static final int LEGGINGS_DURABILITY = 15 * DURABILITY_MULTIPLIER;
    public static final int BOOTS_DURABILITY = 13 * DURABILITY_MULTIPLIER;

    // 附魔能力
    public static final int ENCHANT_VALUE = 12;

    // 盔甲韧性
    public static final float TOUGHNESS = 2.0F;
}
