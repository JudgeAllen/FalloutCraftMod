package com.falloutcraft.fallout.block;

import com.falloutcraft.fallout.FalloutCraft;
import com.falloutcraft.fallout.block.entity.NuclearReactorBlockEntity;
import com.falloutcraft.fallout.inventory.NuclearReactorMenu;
import com.falloutcraft.fallout.item.FalloutItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public final class FalloutBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FalloutCraft.MODID);

    // ============ Phase 2.1 ============

    public static final RegistryObject<Block> URANIUM_ORE = BLOCKS.register("uranium_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .setId(BLOCKS.key("uranium_ore"))
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Item> URANIUM_ORE_ITEM = FalloutItems.ITEMS.register("uranium_ore",
            () -> new BlockItem(URANIUM_ORE.get(), new Item.Properties()
                    .setId(FalloutItems.ITEMS.key("uranium_ore"))));

    // ============ Phase 2.4 ============

    public static final RegistryObject<NuclearReactorBlock> NUCLEAR_REACTOR = BLOCKS.register("nuclear_reactor",
            () -> new NuclearReactorBlock(BlockBehaviour.Properties.of()
                    .setId(BLOCKS.key("nuclear_reactor"))
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 10.0F)
                    .lightLevel(state -> 7)));

    public static final RegistryObject<Item> NUCLEAR_REACTOR_ITEM = FalloutItems.ITEMS.register("nuclear_reactor",
            () -> new BlockItem(NUCLEAR_REACTOR.get(), new Item.Properties()
                    .setId(FalloutItems.ITEMS.key("nuclear_reactor"))));

    // ============ BlockEntityType ============

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FalloutCraft.MODID);

    public static final RegistryObject<BlockEntityType<NuclearReactorBlockEntity>> REACTOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("nuclear_reactor",
                    () -> new BlockEntityType<>(NuclearReactorBlockEntity::new,
                            Set.of(NUCLEAR_REACTOR.get())));

    // ============ MenuType ============

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FalloutCraft.MODID);

    public static final RegistryObject<MenuType<NuclearReactorMenu>> REACTOR_MENU =
            MENU_TYPES.register("nuclear_reactor",
                    () -> new MenuType<>(NuclearReactorMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
