package com.falloutcraft.fallout.item;

import com.falloutcraft.fallout.effect.FalloutEffects;
import com.falloutcraft.fallout.sound.FalloutSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class GeigerCounterItem extends Item {

    public GeigerCounterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            int radiationLevel = getRadiationLevel(level, player);

            Component message;
            float pitch;

            switch (radiationLevel) {
                case 0 -> {
                    message = Component.translatable("message.falloutcraft.geiger.safe");
                    pitch = 1.2F;
                }
                case 1 -> {
                    message = Component.translatable("message.falloutcraft.geiger.low");
                    pitch = 0.9F;
                }
                case 2 -> {
                    message = Component.translatable("message.falloutcraft.geiger.elevated");
                    pitch = 0.7F;
                }
                default -> {
                    message = Component.translatable("message.falloutcraft.geiger.high");
                    pitch = 0.5F;
                }
            }

            player.displayClientMessage(message, true);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    FalloutSounds.GEIGER_CRACKLE.get(), SoundSource.PLAYERS,
                    0.3F + radiationLevel * 0.2F, pitch);
        }

        player.getCooldowns().addCooldown(itemStack, 20);

        return InteractionResult.SUCCESS;
    }

    /**
     * 检测玩家周围的辐射等级
     * 0 = 安全，1 = 低，2 = 中，3 = 高
     */
    private int getRadiationLevel(Level level, Player player) {
        var holder = FalloutEffects.RADIATION.getHolder().orElse(null);
        if (holder == null) return 0;

        // 检查玩家是否有辐射效果
        MobEffectInstance effect = player.getEffect(holder);
        if (effect != null) {
            return Math.min(effect.getAmplifier() + 2, 3);
        }

        // 检查玩家是否处于辐射云中（通过检测周围的 AreaEffectCloud 粒子类型）
        List<AreaEffectCloud> clouds = level.getEntitiesOfClass(AreaEffectCloud.class,
                player.getBoundingBox().inflate(8));
        for (AreaEffectCloud cloud : clouds) {
            // 如果云朵使用 ENTITY_EFFECT 粒子（我们的辐射云使用默认粒子），标记为有辐射
            // 简化方案：任何接近的云朵都标记为低辐射
            if (!cloud.isRemoved() && cloud.getRadius() > 0.5F) {
                float radius = cloud.getRadius();
                if (radius > 10) return 1;
                if (radius > 5) return 2;
                return 3;
            }
        }

        return 0;
    }
}
