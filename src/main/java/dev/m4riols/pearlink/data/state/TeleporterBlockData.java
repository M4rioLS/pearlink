package dev.m4riols.pearlink.data.state;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.m4riols.pearlink.block.CustomTeleporterBlock;

public class TeleporterBlockData extends PersistentState {

    private List<BlockPos> teleportBlockPositions;

    public static final Codec<TeleporterBlockData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.listOf().fieldOf("positions").orElse(Collections.emptyList()).forGetter(TeleporterBlockData::getTeleportBlockPositions)
            ).apply(instance, TeleporterBlockData::new)
    );

    public TeleporterBlockData(List<BlockPos> positions) {
        this.teleportBlockPositions = new ArrayList<>(positions);
    }

    public TeleporterBlockData() {
        this(new ArrayList<>());
    }

    public void addTeleportBlockPos(BlockPos pos) {
        if (!teleportBlockPositions.contains(pos)) {
            teleportBlockPositions.add(pos);
            markDirty();
            syncWithTeleporterBlock();
        }
    }

    public void removeTeleportBlockPos(BlockPos pos) {
        if (teleportBlockPositions.remove(pos)) {
            markDirty();
            syncWithTeleporterBlock();
        }
    }

    public List<BlockPos> getTeleportBlockPositions() {
        return this.teleportBlockPositions;
    }

    public static final PersistentStateType<TeleporterBlockData> TYPE = new PersistentStateType<>(
            "teleporter_block_data",
            TeleporterBlockData::new,
            TeleporterBlockData.CODEC,
            null
    );

    public static TeleporterBlockData getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        TeleporterBlockData state = persistentStateManager.getOrCreate(
                TYPE
        );

        state.syncWithTeleporterBlock();

        return state;
    }

    private void syncWithTeleporterBlock() {
         CustomTeleporterBlock.setTeleportBlockPositions(this.teleportBlockPositions);
    }

    public static void load() {}
}
