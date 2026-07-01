package com.falloutcraft.fallout.item;

import com.falloutcraft.fallout.FalloutCraft;
import com.falloutcraft.fallout.item.armor.FalloutArmorMaterials;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class FalloutItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FalloutCraft.MODID);

    // ============ Phase 1 物品 ============

    public static final RegistryObject<Item> NUCLEAR_BOMB = ITEMS.register("nuclear_bomb",
            () -> new NuclearBombItem(new Item.Properties()
                    .setId(ITEMS.key("nuclear_bomb"))
                    .stacksTo(1)));

    public static final RegistryObject<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
            () -> new GeigerCounterItem(new Item.Properties()
                    .setId(ITEMS.key("geiger_counter"))
                    .stacksTo(1)));

    public static final RegistryObject<Item> ANTI_RADIATION_PILL = ITEMS.register("anti_radiation_pill",
            () -> new AntiRadiationPillItem(new Item.Properties()
                    .setId(ITEMS.key("anti_radiation_pill"))
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .alwaysEdible()
                            .nutrition(0)
                            .saturationModifier(0)
                            .build())));

    // ============ Phase 2.1 铀矿物品 ============

    public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new Item(new Item.Properties()
                    .setId(ITEMS.key("raw_uranium"))
                    .stacksTo(64)));

    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new Item(new Item.Properties()
                    .setId(ITEMS.key("uranium_ingot"))
                    .stacksTo(64)));

    public static final RegistryObject<Item> PLUTONIUM_INGOT = ITEMS.register("plutonium_ingot",
            () -> new Item(new Item.Properties()
                    .setId(ITEMS.key("plutonium_ingot"))
                    .stacksTo(64)));

    public static final RegistryObject<Item> NUCLEAR_CORE = ITEMS.register("nuclear_core",
            () -> new Item(new Item.Properties()
                    .setId(ITEMS.key("nuclear_core"))
                    .stacksTo(16)));

    // ============ Phase 2.2 防辐射装甲 ============

    /**
     * 构建装甲物品属性
     * 不使用 .armor() API（1.21.11 MCP 未完全映射），改用 DataComponents 手动设定
     */
    private static Item.Properties armorProps(String name, ArmorType type, int defense, EquipmentSlot slot) {
        Identifier armorId = Identifier.tryParse("falloutcraft:armor/" + type.getSerializedName());
        // 护甲值属性
        ItemAttributeModifiers attribs = ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(armorId, defense, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot))
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(Identifier.tryParse("falloutcraft:armor/" + type.getSerializedName() + "_tough"),
                                FalloutArmorMaterials.TOUGHNESS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot))
                .build();
        // Equippable 组件
        Equippable equip = Equippable.builder(slot)
                .setEquipSound(SoundEvents.ARMOR_EQUIP_IRON)
                .setAsset(FalloutArmorMaterials.EQUIPMENT_KEY)
                .build();
        // 最大耐久
        int maxDamage = switch (type) {
            case HELMET -> 363;
            case CHESTPLATE -> 528;
            case LEGGINGS -> 495;
            case BOOTS -> 429;
            default -> 363;
        };

        return new Item.Properties()
                .setId(ITEMS.key(name))
                .durability(maxDamage)
                .component(DataComponents.ATTRIBUTE_MODIFIERS, attribs)
                .component(DataComponents.EQUIPPABLE, equip)
                .component(DataComponents.ENCHANTABLE, new net.minecraft.world.item.enchantment.Enchantable(
                        FalloutArmorMaterials.ENCHANT_VALUE));
    }

    public static final RegistryObject<Item> RADIATION_HELMET = ITEMS.register("radiation_helmet",
            () -> new Item(armorProps("radiation_helmet", ArmorType.HELMET, 3, EquipmentSlot.HEAD)));

    public static final RegistryObject<Item> RADIATION_CHESTPLATE = ITEMS.register("radiation_chestplate",
            () -> new Item(armorProps("radiation_chestplate", ArmorType.CHESTPLATE, 8, EquipmentSlot.CHEST)));

    public static final RegistryObject<Item> RADIATION_LEGGINGS = ITEMS.register("radiation_leggings",
            () -> new Item(armorProps("radiation_leggings", ArmorType.LEGGINGS, 6, EquipmentSlot.LEGS)));

    public static final RegistryObject<Item> RADIATION_BOOTS = ITEMS.register("radiation_boots",
            () -> new Item(armorProps("radiation_boots", ArmorType.BOOTS, 3, EquipmentSlot.FEET)));

    // ============ Phase 2.3 多级核弹 ============

    public static final RegistryObject<Item> TACTICAL_NUKE = ITEMS.register("tactical_nuke",
            () -> new TacticalNukeItem(new Item.Properties()
                    .setId(ITEMS.key("tactical_nuke"))
                    .stacksTo(1)));

    public static final RegistryObject<Item> THERMO_NUKE = ITEMS.register("thermo_nuke",
            () -> new ThermoNukeItem(new Item.Properties()
                    .setId(ITEMS.key("thermo_nuke"))
                    .stacksTo(1)));

    // ============ Phase 2.4 核废料 ============

    public static final RegistryObject<Item> NUCLEAR_WASTE = ITEMS.register("nuclear_waste",
            () -> new Item(new Item.Properties()
                    .setId(ITEMS.key("nuclear_waste"))
                    .stacksTo(64)));
}
