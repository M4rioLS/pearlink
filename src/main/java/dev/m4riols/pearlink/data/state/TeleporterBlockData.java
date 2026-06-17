package dev.m4riols.pearlink.data.state;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

import dev.m4riols.pearlink.Pearlink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.m4riols.pearlink.block.CustomTeleporterBlock;



public class TeleporterBlockData extends SavedData {

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
            setDirty();
            syncWithTeleporterBlock();
        }
    }

    public void removeTeleportBlockPos(BlockPos pos) {
        if (teleportBlockPositions.remove(pos)) {
            setDirty();
            syncWithTeleporterBlock();
        }
    }

    public List<BlockPos> getTeleportBlockPositions() {
        return this.teleportBlockPositions;
    }

    public static final SavedDataType<TeleporterBlockData> TYPE = new SavedDataType<>(
            Pearlink.id("teleporter_block_data"),
            TeleporterBlockData::new,
            TeleporterBlockData.CODEC,
            (DataFixTypes) null
    );

    public static TeleporterBlockData getServerState(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        SavedDataStorage dataStorage = overworld.getDataStorage();

        TeleporterBlockData state = dataStorage.computeIfAbsent(TYPE);

        state.syncWithTeleporterBlock();

        return state;
    }

    private void syncWithTeleporterBlock() {
         CustomTeleporterBlock.setTeleportBlockPositions(this.teleportBlockPositions);
    }

    public static void load() {}
}
