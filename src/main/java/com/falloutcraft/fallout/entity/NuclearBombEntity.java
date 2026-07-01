package com.falloutcraft.fallout.entity;

import com.falloutcraft.fallout.Config;
import com.falloutcraft.fallout.effect.FalloutEffects;
import com.falloutcraft.fallout.item.FalloutItems;
import com.falloutcraft.fallout.sound.FalloutSounds;
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
        super(getEntityTypeForItem(stack), shooter, level, stack);
    }

    /** 根据 ItemStack 中的物品类型返回对应的 EntityType */
    private static EntityType<? extends ThrowableItemProjectile> getEntityTypeForItem(ItemStack stack) {
        Item item = stack.getItem();
        if (item == FalloutItems.TACTICAL_NUKE.get()) return FalloutEntities.TACTICAL_NUKE.get();
        if (item == FalloutItems.THERMO_NUKE.get()) return FalloutEntities.THERMO_NUKE.get();
        return FalloutEntities.NUCLEAR_BOMB.get();
    }

    @Override
    protected Item getDefaultItem() {
        return FalloutItems.NUCLEAR_BOMB.get();
    }

    // ==================== 爆炸参数（子类可覆盖） ====================

    /** 爆炸半径 */
    protected int getExplosionRadius() {
        Item item = getDefaultItem();
        if (item == FalloutItems.TACTICAL_NUKE.get()) return Config.tacticalExplosionRadius;
        if (item == FalloutItems.THERMO_NUKE.get()) return Config.thermoExplosionRadius;
        return Config.bombExplosionRadius;
    }

    /** 辐射云半径 */
    protected int getCloudRadius() {
        Item item = getDefaultItem();
        if (item == FalloutItems.TACTICAL_NUKE.get()) return 8;
        if (item == FalloutItems.THERMO_NUKE.get()) return 25;
        return Config.radiationCloudRadius;
    }

    /** 辐射云持续时间（刻） */
    protected int getCloudDuration() {
        Item item = getDefaultItem();
        if (item == FalloutItems.TACTICAL_NUKE.get()) return 3600;     // 3 分钟
        if (item == FalloutItems.THERMO_NUKE.get()) return 12000;    // 10 分钟
        return Config.radiationCloudDuration;                          // 5 分钟
    }

    /** 蘑菇云规模系数 */
    protected float getMushroomScale() {
        Item item = getDefaultItem();
        if (item == FalloutItems.TACTICAL_NUKE.get()) return 0.4F;
        if (item == FalloutItems.THERMO_NUKE.get()) return 2.0F;
        return 1.0F;
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

        // 1. 主爆炸
        serverLevel.explode(this, x, y, z, getExplosionRadius(), Level.ExplosionInteraction.TNT);

        // 2. 蘑菇云
        buildMushroomCloud(serverLevel);

        // 3. 辐射区域
        spawnRadiationZones(serverLevel);

        // 4. 音效
        serverLevel.playSound(null, x, y, z,
                FalloutSounds.NUCLEAR_EXPLOSION.get(), SoundSource.BLOCKS,
                4.0F, (1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.2F));

        this.discard();
    }

    private void buildMushroomCloud(ServerLevel level) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        float scale = getMushroomScale();

        // 中心火球（数量缩放）
        int fireballs = Math.max(1, (int)(4 * scale));
        for (int i = 0; i < fireballs; i++) {
            double h = 2 + i * 3.5 / scale;
            float r = 2.0F * scale;
            level.explode(this, x, y + h, z, Math.max(0.5F, r * 0.3F), Level.ExplosionInteraction.TNT);
        }

        // 烟柱（数量缩放）
        int pillars = (int)(15 * scale);
        for (int h = 0; h < pillars; h++) {
            double height = 1.0 + h * 1.3;
            double offsetX = (level.random.nextDouble() - 0.5) * 0.8 * scale;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.8 * scale;
            level.explode(this, x + offsetX, y + height, z + offsetZ, 0.3F, Level.ExplosionInteraction.TNT);
        }

        // 蘑菇云顶（环数 + 半径缩放）
        int rings = (int)(4 * scale);
        for (int ring = 1; ring <= rings; ring++) {
            double radius = ring * 2.5 * scale;
            int count = (int)(radius * 2.5);
            for (int a = 0; a < count; a++) {
                double angle = a * 6.2832 / count;
                double px = x + Math.cos(angle) * radius;
                double pz = z + Math.sin(angle) * radius;
                double py = y + 18 * scale + level.random.nextDouble() * 3;
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

        int cloudRadius = getCloudRadius();
        int duration = getCloudDuration();

        AreaEffectCloud mainCloud = new AreaEffectCloud(serverLevel, x, y + 1, z);
        mainCloud.setRadius(cloudRadius);
        mainCloud.setWaitTime(20);
        mainCloud.setDuration(duration);
        mainCloud.addEffect(new MobEffectInstance(holder, 200, 0));
        serverLevel.addFreshEntity(mainCloud);

        // 卫星云数量根据规模调整
        int satellites = (cloudRadius > 20) ? 6 : 3;
        for (int i = 0; i < satellites; i++) {
            double offsetX = (level().random.nextDouble() - 0.5) * 10;
            double offsetZ = (level().random.nextDouble() - 0.5) * 10;
            AreaEffectCloud satellite = new AreaEffectCloud(serverLevel,
                    x + offsetX, y + 1, z + offsetZ);
            satellite.setRadius(cloudRadius * 0.4F);
            satellite.setWaitTime(40);
            satellite.setDuration(duration / 2);
            satellite.addEffect(new MobEffectInstance(holder, 100, 1));
            serverLevel.addFreshEntity(satellite);
        }
    }
}
