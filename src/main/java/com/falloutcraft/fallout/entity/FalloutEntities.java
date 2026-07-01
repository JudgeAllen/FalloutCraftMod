package com.falloutcraft.fallout.entity;

import com.falloutcraft.fallout.FalloutCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FalloutEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FalloutCraft.MODID);

    public static final RegistryObject<EntityType<NuclearBombEntity>> NUCLEAR_BOMB =
            ENTITY_TYPES.register("nuclear_bomb",
                    () -> EntityType.Builder.<NuclearBombEntity>of(NuclearBombEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,
                                    FalloutCraft.id("nuclear_bomb"))));

    public static final RegistryObject<EntityType<TacticalNukeEntity>> TACTICAL_NUKE =
            ENTITY_TYPES.register("tactical_nuke",
                    () -> EntityType.Builder.<TacticalNukeEntity>of(TacticalNukeEntity::new, MobCategory.MISC)
                            .sized(0.4F, 0.4F)
                            .clientTrackingRange(64)
                            .updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,
                                    FalloutCraft.id("tactical_nuke"))));

    public static final RegistryObject<EntityType<ThermoNukeEntity>> THERMO_NUKE =
            ENTITY_TYPES.register("thermo_nuke",
                    () -> EntityType.Builder.<ThermoNukeEntity>of(ThermoNukeEntity::new, MobCategory.MISC)
                            .sized(0.6F, 0.6F)
                            .clientTrackingRange(64)
                            .updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,
                                    FalloutCraft.id("thermo_nuke"))));
}
