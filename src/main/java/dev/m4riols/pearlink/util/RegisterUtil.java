package dev.m4riols.pearlink.util;

import dev.m4riols.pearlink.Pearlink;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class RegisterUtil {

    public static <T extends Block> T blockRegister(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, Pearlink.id(name), block);
    }

    public static <T extends Block> T blockRegisterWithItem(String name, T block, Item.Properties settings) {
        T registered = blockRegister(name, block);
        itemRegister(name, new BlockItem(registered, settings.useBlockDescriptionPrefix()));
        return registered;
    }

    public static <T extends Block> T blockRegisterWithItem(String name, T block) {
        return blockRegisterWithItem(name, block, new Item.Properties());
    }

    public static ResourceKey<Block> createBlockRegistryKey(String name) {
        Identifier id = Pearlink.id(name);
        return ResourceKey.create(Registries.BLOCK, id);
    }

    public static ResourceKey<Item> createItemRegistryKey(String name) {
        Identifier id = Pearlink.id(name);
        return ResourceKey.create(Registries.ITEM, id);
    }

    public static <T extends Item> T itemRegister(String name, T item) {
        Identifier id = Pearlink.id(name);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }


}
