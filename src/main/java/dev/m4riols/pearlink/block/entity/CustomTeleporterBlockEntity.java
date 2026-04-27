package dev.m4riols.pearlink.block.entity;

import org.jetbrains.annotations.Nullable;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.init.BlockEntityTypeInit;
import dev.m4riols.pearlink.network.BlockPosPayload;
import dev.m4riols.pearlink.screenhandler.CustomTeleporterScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CustomTeleporterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload>{
    public static final Text TITLE = Text.translatable("container." + Pearlink.MOD_ID + ".teleporter");

    private final SimpleInventory inventory = new SimpleInventory(1){
        @Override
        public void markDirty(){
            super.markDirty();
            update();
        }

        @Override
        public int getMaxCountPerStack() {
            return 1;
        }
    };

    private final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);

    public CustomTeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeInit.CUSTOM_TELEPORTER_BLOCK_ENTITY, pos, state);
    }


    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CustomTeleporterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }  

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        // TODO: check if it works
        var nbt = super.toInitialChunkDataNbt(registryLookup);
        try (ErrorReporter.Logging logging = new ErrorReporter.Logging(this.getReporterContext(), Pearlink.LOGGER)) {
			NbtWriteView nbtWriteView = NbtWriteView.create(logging, registryLookup);
			this.writeDataWithoutId(nbtWriteView);
			nbt = nbtWriteView.getNbt();
            writeData(nbtWriteView);
		}
        return nbt;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view, this.inventory.getHeldStacks());
    }

    @Override
    protected void writeData(WriteView view) {
        Inventories.writeData(view, this.inventory.getHeldStacks());
        super.writeData(view);
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState state) {
        if (this.world != null) {
            ItemScatterer.spawn(this.world, pos, this.inventory);
        }
    }


    private void update(){
        markDirty();
        if(world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
    }

    public InventoryStorage getInventoryProvider(Direction direction){
        return inventoryStorage;
    }

    public SimpleInventory getInventory(){return this.inventory;}

    public boolean isConnectedTo(CustomTeleporterBlockEntity other) {
        return this.getInventory().getStack(0).getItem() == other.getInventory().getStack(0).getItem();
    }

    public ItemStack getStoredItem() {
        return this.getInventory().getStack(0);
    }

}
