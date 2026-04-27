package dev.m4riols.pearlink.network;


import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

import dev.m4riols.pearlink.Pearlink;

public record TeleporterPayload(List<BlockPos> positions) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TeleporterPayload> ID =
            new CustomPacketPayload.Type<>(Pearlink.id("teleporter_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TeleporterPayload> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(java.util.ArrayList::new)),
                    TeleporterPayload::positions,
                    TeleporterPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
