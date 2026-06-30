package com.falloutcraft.fallout.effect;

import com.falloutcraft.fallout.FalloutCraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FalloutEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FalloutCraft.MODID);

    // 辐射病效果
    public static final RegistryObject<MobEffect> RADIATION = MOB_EFFECTS.register("radiation", RadiationEffect::new);
}
