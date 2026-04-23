package dev.m4riols.pearlink.screenhandler;

import dev.m4riols.pearlink.block.entity.CustomTeleporterBlockEntity;
import dev.m4riols.pearlink.init.BlockInit;
import dev.m4riols.pearlink.init.ScreenHandlerTypeInit;
import dev.m4riols.pearlink.network.BlockPosPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class CustomTeleporterScreenHandler extends ScreenHandler{
    private final CustomTeleporterBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    public CustomTeleporterScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload){
        this(syncId,playerInventory, (CustomTeleporterBlockEntity)playerInventory.player.getEntityWorld().getBlockEntity(payload.pos()));
    }

    public CustomTeleporterScreenHandler(int syncId, PlayerInventory playerInventory, CustomTeleporterBlockEntity blockEntity) {
        super(ScreenHandlerTypeInit.CUSTOM_TELEPORTER, syncId);

        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(),this.blockEntity.getPos());

        SimpleInventory inventory = this.blockEntity.getInventory();
        checkSize(inventory, 1);
        inventory.onOpen(playerInventory.player);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addBlockInventory(inventory);

    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int column = 0; column < 9; column++){

            addSlot(new Slot(playerInventory, column, 8 + (18*column), 142));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int row=0;row < 3; row++){
            for(int column = 0; column < 9; column ++){
                addSlot(new Slot(playerInventory, 9 + column + row * 9, 8 + (18*column), 84 + row * 18));
            }
        }
    }

    private void addBlockInventory(SimpleInventory inventory){
        addSlot(new SingleItemSlot(inventory,0, 80, 33));
    }

    @Override
    public void onClosed(PlayerEntity player){
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, BlockInit.CUSTOM_TELEPORTER_BLOCK);
    }

    // TODO: needs testing, cf. AbstractFurnaceScreenHandler
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (slotIndex == 0) {
                if (!this.insertItem(originalStack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(originalStack, newStack);
            } else {
                if (!this.slots.get(0).hasStack() && !this.insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

                if (slotIndex >= 1 && slotIndex < 28) {
                    if (!this.insertItem(originalStack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 28 && slotIndex < 37) {
                    if (!this.insertItem(originalStack, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }

    public CustomTeleporterBlockEntity getBlockEntity() {return this.blockEntity;}
}
