package dev.m4riols.pearlink.screenhandler;

import dev.m4riols.pearlink.block.entity.CustomTeleporterBlockEntity;
import dev.m4riols.pearlink.init.BlockInit;
import dev.m4riols.pearlink.init.ScreenHandlerTypeInit;
import dev.m4riols.pearlink.network.BlockPosPayload;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CustomTeleporterScreenHandler extends AbstractContainerMenu {
    private static final int PLAYER_INV_START = 0;
    private static final int HOTBAR_START = 27;
    private static final int HOTBAR_END = 36;
    private static final int CONTAINER_SLOT = 36;

    private final CustomTeleporterBlockEntity blockEntity;
    private final ContainerLevelAccess context;

    public CustomTeleporterScreenHandler(int syncId, Inventory playerInventory, BlockPosPayload payload){
        this(syncId, playerInventory, (CustomTeleporterBlockEntity) playerInventory.player.level().getBlockEntity(payload.pos()));
    }

    public CustomTeleporterScreenHandler(int syncId, Inventory playerInventory, CustomTeleporterBlockEntity blockEntity) {
        super(ScreenHandlerTypeInit.CUSTOM_TELEPORTER, syncId);

        this.blockEntity = blockEntity;
        this.context = ContainerLevelAccess.create(this.blockEntity.getLevel(), this.blockEntity.getBlockPos());

        SimpleContainer inventory = this.blockEntity.getInventory();
        checkContainerSize(inventory, 1);
        inventory.startOpen(playerInventory.player);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addBlockInventory(inventory);

    }

    private void addPlayerHotbar(Inventory playerInventory){
        for(int column = 0; column < 9; column++){

            addSlot(new Slot(playerInventory, column, 8 + (18*column), 142));
        }
    }

    private void addPlayerInventory(Inventory playerInventory){
        for(int row=0;row < 3; row++){
            for(int column = 0; column < 9; column ++){
                addSlot(new Slot(playerInventory, 9 + column + row * 9, 8 + (18*column), 84 + row * 18));
            }
        }
    }

    private void addBlockInventory(Container inventory){
        addSlot(new SingleItemSlot(inventory, 0, 80, 33));
    }

    @Override
    public void removed(Player player){
        super.removed(player);
        this.blockEntity.getInventory().stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(context, player, BlockInit.CUSTOM_TELEPORTER_BLOCK);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack newStack = originalStack.copy();

        if (slotIndex == CONTAINER_SLOT) {
            if (!moveItemStackTo(originalStack, PLAYER_INV_START, HOTBAR_END, true)) return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(originalStack, CONTAINER_SLOT, CONTAINER_SLOT + 1, false)) {
                if (slotIndex < HOTBAR_START) {
                    if (!moveItemStackTo(originalStack, HOTBAR_START, HOTBAR_END, false)) return ItemStack.EMPTY;
                } else {
                    if (!moveItemStackTo(originalStack, PLAYER_INV_START, HOTBAR_START, false)) return ItemStack.EMPTY;
                }
            }
        }

        if (originalStack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        if (originalStack.getCount() == newStack.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, originalStack);
        return newStack;
    }

    public CustomTeleporterBlockEntity getBlockEntity() {return this.blockEntity;}
}
