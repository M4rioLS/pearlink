package dev.m4riols.pearlink.init;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.network.BlockPosPayload;
import dev.m4riols.pearlink.screenhandler.CustomTeleporterScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ScreenHandlerTypeInit {

    public static MenuType<CustomTeleporterScreenHandler> CUSTOM_TELEPORTER =
        register("custom_teleporter", CustomTeleporterScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static <T extends AbstractContainerMenu, D extends CustomPacketPayload> ExtendedMenuType<T, D> register(String name, ExtendedMenuType.ExtendedFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> codec) {
        return Registry.register(BuiltInRegistries.MENU, Pearlink.id(name), new ExtendedMenuType<>(factory, codec));
    }

    public static void load(){}

}
