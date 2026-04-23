package dev.m4riols.pearlink.network;


import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import dev.m4riols.pearlink.Pearlink;

public record TeleporterPayload(List<BlockPos> positions) implements CustomPayload {

    public static final CustomPayload.Id<TeleporterPayload> ID =
            new CustomPayload.Id<>(Pearlink.id("teleporter_sync"));

    public static final PacketCodec<RegistryByteBuf, TeleporterPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.collection(java.util.ArrayList::new, BlockPos.PACKET_CODEC),
                    TeleporterPayload::positions,
                    TeleporterPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
