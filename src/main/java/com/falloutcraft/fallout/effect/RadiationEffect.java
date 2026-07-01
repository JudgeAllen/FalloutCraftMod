package com.falloutcraft.fallout.effect;

import com.falloutcraft.fallout.Config;
import com.falloutcraft.fallout.item.FalloutItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        // HARMFUL（有害效果），颜色为绿色 (0x44FF44)
        super(MobEffectCategory.HARMFUL, 0x44FF44);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        // 如果生命值大于 1，则造成伤害（避免杀死后还继续扣血）
        if (livingEntity.getHealth() > 1.0F) {
            // 检查玩家是否穿着防辐射装甲
            if (livingEntity instanceof Player player) {
                int armorPieces = countRadiationArmorPieces(player);
                if (armorPieces >= 4) {
                    return false; // 全套装甲 → 完全免疫
                }
                float damage = (float) (Config.radiationDamageAmount * (amplifier + 1));
                if (armorPieces == 3) {
                    damage *= 0.5F; // 三件 → 伤害减半
                }
                livingEntity.hurt(livingEntity.damageSources().magic(), damage);
            } else {
                // 非玩家实体（如怪物），正常造成伤害
                livingEntity.hurt(livingEntity.damageSources().magic(), (float) (Config.radiationDamageAmount * (amplifier + 1)));
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = Math.max(Config.radiationDamageInterval / (amplifier + 1), 5);
        return duration % interval == 0;
    }

    /**
     * 统计玩家穿着的防辐射装甲件数
     * 4 = 全套，3 = 三件，以此类推
     */
    private int countRadiationArmorPieces(Player player) {
        int count = 0;
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(FalloutItems.RADIATION_HELMET.get())) count++;
        if (player.getItemBySlot(EquipmentSlot.CHEST).is(FalloutItems.RADIATION_CHESTPLATE.get())) count++;
        if (player.getItemBySlot(EquipmentSlot.LEGS).is(FalloutItems.RADIATION_LEGGINGS.get())) count++;
        if (player.getItemBySlot(EquipmentSlot.FEET).is(FalloutItems.RADIATION_BOOTS.get())) count++;
        return count;
    }
}
