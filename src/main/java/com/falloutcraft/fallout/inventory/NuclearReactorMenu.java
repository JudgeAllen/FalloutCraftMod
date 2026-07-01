package com.falloutcraft.fallout.inventory;

import com.falloutcraft.fallout.block.FalloutBlocks;
import com.falloutcraft.fallout.block.entity.NuclearReactorBlockEntity;
import com.falloutcraft.fallout.item.FalloutItems;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NuclearReactorMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data;

    // 客户端构造器
    public NuclearReactorMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(NuclearReactorBlockEntity.CONTAINER_SIZE),
                new SimpleContainerData(NuclearReactorBlockEntity.DATA_COUNT));
    }

    // 服务端构造器
    public NuclearReactorMenu(int containerId, Inventory playerInv, Container container, ContainerData data) {
        super(FalloutBlocks.REACTOR_MENU.get(), containerId);
        this.container = container;
        this.data = data;

        // 反应堆物品栏：燃料(56,17) 冷却(56,53) 输出1(116,30) 输出2(134,30)
        this.addSlot(new FuelSlot(container, NuclearReactorBlockEntity.SLOT_FUEL, 56, 17));
        this.addSlot(new CoolantSlot(container, NuclearReactorBlockEntity.SLOT_COOLANT, 56, 53));
        this.addSlot(new Slot(container, NuclearReactorBlockEntity.SLOT_OUTPUT_1, 116, 35));
        this.addSlot(new Slot(container, NuclearReactorBlockEntity.SLOT_OUTPUT_2, 134, 35));

        // 玩家物品栏
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));

        this.addDataSlots(data);
    }

    public ContainerData getData() { return data; }
    public int getTemperature() { return data.get(NuclearReactorBlockEntity.DATA_TEMP); }
    public int getBurnProgress() {
        int dur = data.get(NuclearReactorBlockEntity.DATA_BURN_DUR);
        return dur > 0 ? data.get(NuclearReactorBlockEntity.DATA_BURN_TIME) * 13 / dur : 0;
    }
    public int getMeltdownTemp() {
        // 从 Data 无法直接拿到配置值，但 Screen 可以从温度自行判断颜色
        return 800; // 默认值，Screen 展示用
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return result;
        ItemStack stack = slot.getItem();
        result = stack.copy();

        if (index < NuclearReactorBlockEntity.CONTAINER_SIZE) {
            if (!this.moveItemStackTo(stack, NuclearReactorBlockEntity.CONTAINER_SIZE, this.slots.size(), true))
                return ItemStack.EMPTY;
        } else {
            if (stack.is(FalloutItems.URANIUM_INGOT.get())) {
                if (!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else if (stack.is(Items.WATER_BUCKET) || stack.is(Items.ICE) || stack.is(Items.BLUE_ICE)) {
                if (!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(stack, 0, NuclearReactorBlockEntity.CONTAINER_SIZE, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        return result;
    }

    @Override public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    // 燃料槽：只接受铀锭
    private static class FuelSlot extends Slot {
        public FuelSlot(Container c, int i, int x, int y) { super(c, i, x, y); }
        @Override public boolean mayPlace(ItemStack s) { return s.is(FalloutItems.URANIUM_INGOT.get()); }
    }
    // 冷却槽：只接受水桶/冰/蓝冰
    private static class CoolantSlot extends Slot {
        public CoolantSlot(Container c, int i, int x, int y) { super(c, i, x, y); }
        @Override public boolean mayPlace(ItemStack s) {
            return s.is(Items.WATER_BUCKET) || s.is(Items.ICE) || s.is(Items.BLUE_ICE);
        }
    }
}
