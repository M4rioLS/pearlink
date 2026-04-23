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
    private static final int PLAYER_INV_START = 0;
    private static final int HOTBAR_START = 27;
    private static final int HOTBAR_END = 36;
    private static final int CONTAINER_SLOT = 36;

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

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot == null || !slot.hasStack()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getStack();
        ItemStack newStack = originalStack.copy();

        if (slotIndex == CONTAINER_SLOT) {
            if (!insertItem(originalStack, PLAYER_INV_START, HOTBAR_END, true)) return ItemStack.EMPTY;
        } else {
            if (!insertItem(originalStack, CONTAINER_SLOT, CONTAINER_SLOT + 1, false)) {
                if (slotIndex < HOTBAR_START) {
                    if (!insertItem(originalStack, HOTBAR_START, HOTBAR_END, false)) return ItemStack.EMPTY;
                } else {
                    if (!insertItem(originalStack, PLAYER_INV_START, HOTBAR_START, false)) return ItemStack.EMPTY;
                }
            }
        }

        if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();
        if (originalStack.getCount() == newStack.getCount()) return ItemStack.EMPTY;
        slot.onTakeItem(player, originalStack);
        return newStack;
    }

    public CustomTeleporterBlockEntity getBlockEntity() {return this.blockEntity;}
}
