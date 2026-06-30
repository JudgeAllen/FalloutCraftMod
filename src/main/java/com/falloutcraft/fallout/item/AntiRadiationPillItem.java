package com.falloutcraft.fallout.item;

import com.falloutcraft.fallout.effect.FalloutEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AntiRadiationPillItem extends Item {

    public AntiRadiationPillItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide()) {
            var holder = FalloutEffects.RADIATION.getHolder().orElse(null);
            if (holder != null) {
                MobEffectInstance currentEffect = livingEntity.getEffect(holder);
                if (currentEffect != null) {
                    livingEntity.removeEffect(holder);
                }
            }
        }

        return super.finishUsingItem(stack, level, livingEntity);
    }
}
