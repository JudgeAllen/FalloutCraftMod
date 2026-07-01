package com.falloutcraft.fallout.entity;

import com.falloutcraft.fallout.item.FalloutItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 热核弹实体 — 巨型爆炸
 */
public class ThermoNukeEntity extends NuclearBombEntity {

    public ThermoNukeEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThermoNukeEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(level, shooter, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return FalloutItems.THERMO_NUKE.get();
    }
}
