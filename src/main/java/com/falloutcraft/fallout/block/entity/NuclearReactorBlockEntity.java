package com.falloutcraft.fallout.block.entity;

import com.falloutcraft.fallout.Config;
import com.falloutcraft.fallout.block.FalloutBlocks;
import com.falloutcraft.fallout.block.NuclearReactorBlock;
import com.falloutcraft.fallout.effect.FalloutEffects;
import com.falloutcraft.fallout.inventory.NuclearReactorMenu;
import com.falloutcraft.fallout.item.FalloutItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NuclearReactorBlockEntity extends BaseContainerBlockEntity {

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_COOLANT = 1;
    public static final int SLOT_OUTPUT_1 = 2;
    public static final int SLOT_OUTPUT_2 = 3;
    public static final int CONTAINER_SIZE = 4;

    // ContainerData 索引
    public static final int DATA_TEMP = 0;
    public static final int DATA_BURN_TIME = 1;
    public static final int DATA_BURN_DUR = 2;
    public static final int DATA_WASTE = 3;
    public static final int DATA_COUNT = 4;

    // 物品栏
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    // 运行状态
    public int temperature;        // 0~1000
    public int burnTime;           // 剩余燃料刻数
    public int burnDuration;       // 当前燃料总刻数
    public int wasteProgress;      // 废料进度
    public int wasteTicks;         // 经验产出计时
    public int heatTick;           // 升温计时器 (每20tick=1秒升1度)
    public int coolTick;           // 自然冷却计时 (每200tick=10秒降1度)
    public int coolantHeat;        // 当前冷却物已吸收的热量

    /** 冰块/蓝冰/水桶的冷却量从 Config 读取 */
    private static int icePoints() { return Config.reactorIceCooling; }
    private static int blueIcePoints() { return Config.reactorBlueIceCooling; }
    private static int waterPoints() { return Config.reactorWaterCooling; }

    public final ContainerData dataAccess = new ContainerData() {
        public int get(int i) { return switch(i) { case 0->temperature; case 1->burnTime; case 2->burnDuration; case 3->wasteProgress; default->0; }; }
        public void set(int i, int v) { switch(i) { case 0->temperature=v; case 1->burnTime=v; case 2->burnDuration=v; case 3->wasteProgress=v; } }
        public int getCount() { return DATA_COUNT; }
    };

    public NuclearReactorBlockEntity(BlockPos pos, BlockState state) {
        super(FalloutBlocks.REACTOR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override protected Component getDefaultName() { return Component.translatable("container.falloutcraft.reactor"); }
    @Override protected NonNullList<ItemStack> getItems() { return items; }
    @Override protected void setItems(NonNullList<ItemStack> l) { items = l; }
    @Override public int getContainerSize() { return CONTAINER_SIZE; }
    @Override protected AbstractContainerMenu createMenu(int id, Inventory inv) { return new NuclearReactorMenu(id, inv, this, dataAccess); }

    // ====== Server Tick ======

    public static void serverTick(Level level, BlockPos pos, BlockState state, NuclearReactorBlockEntity be) {
        if (!(level instanceof ServerLevel sl)) return;
        boolean dirty = false;

        // ---- 1. 燃烧 ----
        if (be.burnTime > 0) {
            be.burnTime--;
            be.heatTick++;
            if (be.heatTick >= 20) {
                be.heatTick = 0;
                processHeat(be);
            }
            dirty = true;
        } else {
            be.coolTick++;
            if (be.coolTick >= 200) {
                be.coolTick = 0;
                be.temperature = Math.max(be.temperature - 1, 0);
                dirty = true;
            }
        }

        // ---- 更新外观等级 ----
        int newLevel = calcLevel(be.temperature);
        int oldLevel = state.getValue(NuclearReactorBlock.LEVEL);
        if (newLevel != oldLevel) {
            level.setBlock(pos, state.setValue(NuclearReactorBlock.LEVEL, newLevel), 3);
        }

        // ---- 2. 吃新燃料 ----
        if (be.burnTime <= 0 && be.temperature < 900) {
            ItemStack f = be.items.get(SLOT_FUEL);
            if (f.is(FalloutItems.URANIUM_INGOT.get())) {
                f.shrink(1);
                be.burnTime = Config.reactorFuelBurnTime;
                be.burnDuration = Config.reactorFuelBurnTime;
                dirty = true;
            }
        }

        // ---- 3. 产废料 + 经验 ----
        if (be.temperature > 200 && be.burnTime > 0) {
            be.wasteProgress++;
            be.wasteTicks++;
            if (be.wasteProgress >= Config.reactorWasteInterval) {
                be.wasteProgress = 0;
                tryPut(be, new ItemStack(FalloutItems.NUCLEAR_WASTE.get()));
            }
            if (be.wasteTicks >= 1200) {
                be.wasteTicks = 0;
                ExperienceOrb.award(sl, pos.getCenter(), 2 + level.random.nextInt(4));
            }
            dirty = true;
        }

        // ---- 4. 熔毁风险 ----
        if (be.temperature >= Config.reactorMeltdownTemp && be.burnTime > 0 && level.random.nextInt(40) == 0)
            be.temperature = Math.min(be.temperature + 5, 1000);

        // ---- 5. 爆炸 ----
        if (be.temperature >= 1000) {
            level.explode(null, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, 8F, Level.ExplosionInteraction.TNT);
            level.removeBlock(pos, false);
        }

        // ---- 6. 周边辐射 ----
        if (be.temperature > 200 && be.burnTime > 0 && level.random.nextInt(100) == 0) {
            var h = FalloutEffects.RADIATION.getHolder().orElse(null);
            if (h != null) {
                AreaEffectCloud aec = new AreaEffectCloud(level, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5);
                aec.setRadius(5F); aec.setWaitTime(0); aec.setDuration(40); aec.setRadiusPerTick(0F);
                aec.addEffect(new MobEffectInstance(h, 100, 0));
                level.addFreshEntity(aec);
            }
        }

        if (dirty) be.setChanged();
    }

    /** 处理每 1 秒产生的 1 度热量 */
    private static void processHeat(NuclearReactorBlockEntity be) {
        ItemStack c = be.items.get(SLOT_COOLANT);

        // 水桶：瞬时大幅降温
        if (c.is(Items.WATER_BUCKET)) {
            be.temperature = Math.max(be.temperature - Config.reactorWaterCooling, 0);
            be.items.set(SLOT_COOLANT, new ItemStack(Items.BUCKET));
            be.coolantHeat = 0;
            return;
        }

        // 冰块：每1秒吸1度，攒够N度消耗1个 (默认30秒=1冰)
        if (c.is(Items.ICE)) {
            be.coolantHeat++;
            if (be.coolantHeat >= Config.reactorIceCooling) {
                c.shrink(1);
                be.coolantHeat = 0;
            }
            return;
        }

        // 蓝冰：同上，默认240秒=1蓝冰
        if (c.is(Items.BLUE_ICE)) {
            be.coolantHeat++;
            if (be.coolantHeat >= Config.reactorBlueIceCooling) {
                c.shrink(1);
                be.coolantHeat = 0;
            }
            return;
        }

        // 没有冷却物 → 升温
        be.coolantHeat = 0;
        be.temperature = Math.min(be.temperature + 1, 1000);
    }

    /** 根据温度计算外观等级 0~4 */
    private static int calcLevel(int temp) {
        if (temp >= 800) return 4;
        if (temp >= 600) return 3;
        if (temp >= 200) return 2;
        if (temp > 0) return 1;
        return 0;
    }

    private static void tryPut(NuclearReactorBlockEntity be, ItemStack stack) {
        for (int s : new int[]{SLOT_OUTPUT_1, SLOT_OUTPUT_2}) {
            ItemStack cur = be.items.get(s);
            if (cur.isEmpty()) { be.items.set(s, stack); return; }
            if (ItemStack.isSameItemSameComponents(cur, stack) && cur.getCount() < cur.getMaxStackSize()) { cur.grow(1); return; }
        }
    }
}
