package dev.m4riols.pearlink.init;

import java.util.List;

import dev.m4riols.pearlink.block.entity.CustomTeleporterBlockEntity;
import dev.m4riols.pearlink.data.state.TeleporterBlockData;
import dev.m4riols.pearlink.network.TeleporterPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class EventInit {

    public static void load() {
		ItemStorage.SIDED.registerForBlockEntity(CustomTeleporterBlockEntity::getInventoryProvider, BlockEntityTypeInit.CUSTOM_TELEPORTER_BLOCK_ENTITY);

    	ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.getPlayer();
			TeleporterBlockData teleporterData = TeleporterBlockData.getServerState(server);
			List<BlockPos> positions = teleporterData.getTeleportBlockPositions();

           	TeleporterPayload payload = new TeleporterPayload(positions);

            ServerPlayNetworking.send(player, payload);
    	});
    }
}
