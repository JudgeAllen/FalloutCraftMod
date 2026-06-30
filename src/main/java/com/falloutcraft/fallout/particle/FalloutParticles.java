package com.falloutcraft.fallout.particle;

import com.falloutcraft.fallout.FalloutCraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FalloutParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FalloutCraft.MODID);

    // 蘑菇云粒子
    public static final RegistryObject<SimpleParticleType> MUSHROOM_CLOUD =
            PARTICLE_TYPES.register("mushroom_cloud", () -> new SimpleParticleType(true));

    // 辐射粒子
    public static final RegistryObject<SimpleParticleType> RADIATION =
            PARTICLE_TYPES.register("radiation", () -> new SimpleParticleType(true));
}
