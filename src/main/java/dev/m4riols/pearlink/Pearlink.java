package dev.m4riols.pearlink;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import dev.m4riols.pearlink.data.state.TeleporterBlockData;
import dev.m4riols.pearlink.init.BlockEntityTypeInit;
import dev.m4riols.pearlink.init.BlockInit;
import dev.m4riols.pearlink.init.EventInit;
import dev.m4riols.pearlink.init.ScreenHandlerTypeInit;
import dev.m4riols.pearlink.network.TeleporterPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.Identifier;

public class Pearlink implements ModInitializer {
	public static final String MOD_ID = "pearlink";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier TELEPORTER_LIST = id("teleporter_list");

    public static final Identifier INITIAL_SYNC = id("initial_sync");

	@Override
	public void onInitialize() {
		LOGGER.info("Logger Loading...");
		BlockInit.load();
		BlockEntityTypeInit.load();
		TeleporterBlockData.load();
		ScreenHandlerTypeInit.load();

		PayloadTypeRegistry.playS2C().register(TeleporterPayload.ID, TeleporterPayload.CODEC);

		EventInit.load();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
			entries.add(BlockInit.CUSTOM_TELEPORTER_BLOCK_ITEM)
		);

    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID,path);
	}

}
