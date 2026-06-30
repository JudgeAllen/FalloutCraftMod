package com.falloutcraft.fallout.sound;

import com.falloutcraft.fallout.FalloutCraft;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FalloutSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FalloutCraft.MODID);

    // 盖革计数器噼啪声（可变范围）
    public static final RegistryObject<SoundEvent> GEIGER_CRACKLE = SOUND_EVENTS.register("geiger_crackle",
            () -> SoundEvent.createVariableRangeEvent(Identifier.tryParse(FalloutCraft.MODID + ":geiger_crackle")));

    // 核爆炸声（固定范围 64 格）
    public static final RegistryObject<SoundEvent> NUCLEAR_EXPLOSION = SOUND_EVENTS.register("nuclear_explosion",
            () -> SoundEvent.createFixedRangeEvent(Identifier.tryParse(FalloutCraft.MODID + ":nuclear_explosion"), 64.0F));
}
