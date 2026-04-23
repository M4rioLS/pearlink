package dev.m4riols.pearlink.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;

public class SingleItemSlot extends Slot {

    public SingleItemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.getStack().isEmpty() && stack.getCount() > 0;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        return true;
    }
}
