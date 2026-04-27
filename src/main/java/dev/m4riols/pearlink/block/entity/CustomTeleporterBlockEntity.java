package dev.m4riols.pearlink.block.entity;

import org.jetbrains.annotations.Nullable;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.init.BlockEntityTypeInit;
import dev.m4riols.pearlink.network.BlockPosPayload;
import dev.m4riols.pearlink.screenhandler.CustomTeleporterScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CustomTeleporterBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPosPayload> {
    public static final Component TITLE = Component.translatable("container." + Pearlink.MOD_ID + ".teleporter");

    private final SimpleContainer inventory = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            update();
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    private final ContainerStorage inventoryStorage = ContainerStorage.of(inventory, null);

    public CustomTeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeInit.CUSTOM_TELEPORTER_BLOCK_ENTITY, pos, state);
    }


    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CustomTeleporterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayer player) {
        return new BlockPosPayload(this.worldPosition);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveCustomOnly(registryLookup);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.inventory.getItems());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output, this.inventory.getItems());
        super.saveAdditional(output);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.inventory);
        }
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState state) {
        if (this.world != null) {
            ItemScatterer.spawn(this.world, pos, this.inventory);
        }
    }


    private void update(){
        setChanged();
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public ContainerStorage getInventoryProvider(Direction direction){
        return inventoryStorage;
    }

    public SimpleContainer getInventory(){return this.inventory;}

    public boolean isConnectedTo(CustomTeleporterBlockEntity other) {
        return this.getInventory().getItem(0).getItem() == other.getInventory().getItem(0).getItem();
    }

    public ItemStack getStoredItem() {
        return this.getInventory().getItem(0);
    }

}
