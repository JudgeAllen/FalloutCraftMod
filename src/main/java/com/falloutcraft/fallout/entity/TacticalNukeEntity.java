package com.falloutcraft.fallout.entity;

import com.falloutcraft.fallout.item.FalloutItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 战术核弹实体 — 小型爆炸
 */
public class TacticalNukeEntity extends NuclearBombEntity {

    public TacticalNukeEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TacticalNukeEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(level, shooter, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return FalloutItems.TACTICAL_NUKE.get();
    }
}
