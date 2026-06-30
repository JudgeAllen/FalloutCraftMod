package com.falloutcraft.fallout.item;

import com.falloutcraft.fallout.FalloutCraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FalloutItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FalloutCraft.MODID);

    // 核弹
    public static final RegistryObject<Item> NUCLEAR_BOMB = ITEMS.register("nuclear_bomb",
            () -> new NuclearBombItem(new Item.Properties()
                    .setId(ITEMS.key("nuclear_bomb"))
                    .stacksTo(1)));

    // 盖革计数器
    public static final RegistryObject<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
            () -> new GeigerCounterItem(new Item.Properties()
                    .setId(ITEMS.key("geiger_counter"))
                    .stacksTo(1)));

    // 抗辐射药丸
    public static final RegistryObject<Item> ANTI_RADIATION_PILL = ITEMS.register("anti_radiation_pill",
            () -> new AntiRadiationPillItem(new Item.Properties()
                    .setId(ITEMS.key("anti_radiation_pill"))
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .alwaysEdible()
                            .nutrition(0)
                            .saturationModifier(0)
                            .build())));
}
