package com.falloutcraft.fallout.effect;

import com.falloutcraft.fallout.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        // HARMFUL（有害效果），颜色为绿色 (0x44FF44)
        super(MobEffectCategory.HARMFUL, 0x44FF44);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        // 如果生命值大于 1，则造成伤害（避免杀死后还继续扣血）
        if (livingEntity.getHealth() > 1.0F) {
            livingEntity.hurt(livingEntity.damageSources().magic(), (float) (Config.radiationDamageAmount * (amplifier + 1)));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // 每 Config.radiationDamageInterval 刻触发一次，等级越高越快
        int interval = Math.max(Config.radiationDamageInterval / (amplifier + 1), 5);
        return duration % interval == 0;
    }
}
