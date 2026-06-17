package dev.m4riols.pearlink;

import java.util.List;

import dev.m4riols.pearlink.init.ScreenHandlerTypeInit;
import dev.m4riols.pearlink.network.TeleporterPayload;
import dev.m4riols.pearlink.screen.CustomTeleporterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;


public class PearlinkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ScreenHandlerTypeInit.CUSTOM_TELEPORTER, CustomTeleporterScreen::new);
        registerClientReceivers();
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(TeleporterPayload.ID, (payload, context) -> {
            List<BlockPos> receivedPositions = payload.positions();

            // Client state must be touched on the main client thread, not the networking thread.
            context.client().execute(() -> {
                System.out.println("Received teleporter positions: " + receivedPositions.size());
            });
        });
    }
}
