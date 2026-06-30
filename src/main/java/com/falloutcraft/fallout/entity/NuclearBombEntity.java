package com.falloutcraft.fallout.entity;

import com.falloutcraft.fallout.Config;
import com.falloutcraft.fallout.effect.FalloutEffects;
import com.falloutcraft.fallout.item.FalloutItems;
import com.falloutcraft.fallout.sound.FalloutSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class NuclearBombEntity extends ThrowableItemProjectile {

    public NuclearBombEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public NuclearBombEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(FalloutEntities.NUCLEAR_BOMB.get(), shooter, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return FalloutItems.NUCLEAR_BOMB.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            explode();
        }
    }

    private void explode() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        // 1. 主爆炸 — 破坏方块 + 自带火焰/烟雾
        serverLevel.explode(this, x, y, z, Config.bombExplosionRadius, Level.ExplosionInteraction.TNT);

        // 2. 蘑菇云 — 用微型爆炸链构建，保证视觉效果
        buildMushroomCloud(serverLevel);

        // 3. 辐射区域
        spawnRadiationZones(serverLevel);

        // 4. 爆炸音效
        serverLevel.playSound(null, x, y, z,
                FalloutSounds.NUCLEAR_EXPLOSION.get(), SoundSource.BLOCKS,
                4.0F, (1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.2F));

        this.discard();
    }

    private void buildMushroomCloud(ServerLevel level) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        // 中心火球 (不用 ExplosionInteraction，用 TNT 才有视觉效果)
        level.explode(this, x, y + 2, z, 2.0F, Level.ExplosionInteraction.TNT);
        level.explode(this, x, y + 5, z, 1.5F, Level.ExplosionInteraction.TNT);
        level.explode(this, x, y + 8, z, 1.0F, Level.ExplosionInteraction.TNT);
        level.explode(this, x, y + 12, z, 1.0F, Level.ExplosionInteraction.TNT);

        // 烟柱 — 一连串小爆炸 (2-20格高度)
        for (int h = 0; h < 15; h++) {
            double height = 1.0 + h * 1.3;
            double offsetX = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.8;
            level.explode(this, x + offsetX, y + height, z + offsetZ, 0.3F, Level.ExplosionInteraction.TNT);
        }

        // 蘑菇云顶 — 3层环状小爆炸
        for (int ring = 1; ring <= 4; ring++) {
            double radius = ring * 2.5;
            int count = (int)(radius * 2.5);
            for (int a = 0; a < count; a++) {
                double angle = a * 6.2832 / count;
                double px = x + Math.cos(angle) * radius;
                double pz = z + Math.sin(angle) * radius;
                double py = y + 18 + level.random.nextDouble() * 3;
                level.explode(this, px, py, pz, 0.5F + level.random.nextFloat() * 0.3F, Level.ExplosionInteraction.TNT);
            }
        }
    }

    private void spawnRadiationZones(ServerLevel serverLevel) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        var holder = FalloutEffects.RADIATION.getHolder().orElse(null);
        if (holder == null) return;

        // 主辐射云（中心）
        AreaEffectCloud mainCloud = new AreaEffectCloud(serverLevel, x, y + 1, z);
        mainCloud.setRadius(Config.radiationCloudRadius);
        mainCloud.setWaitTime(20);
        mainCloud.setDuration(Config.radiationCloudDuration);
        mainCloud.addEffect(new MobEffectInstance(holder, 200, 0));
        serverLevel.addFreshEntity(mainCloud);

        // 3 个卫星云
        for (int i = 0; i < 3; i++) {
            double offsetX = (level().random.nextDouble() - 0.5) * 10;
            double offsetZ = (level().random.nextDouble() - 0.5) * 10;
            AreaEffectCloud satellite = new AreaEffectCloud(serverLevel,
                    x + offsetX, y + 1, z + offsetZ);
            satellite.setRadius(Config.radiationCloudRadius * 0.4F);
            satellite.setWaitTime(40);
            satellite.setDuration(Config.radiationCloudDuration / 2);
            satellite.addEffect(new MobEffectInstance(holder, 100, 1));
            serverLevel.addFreshEntity(satellite);
        }
    }
}
