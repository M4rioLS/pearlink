package dev.m4riols.pearlink.util;

import dev.m4riols.pearlink.Pearlink;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RegisterUtil {

    public static <T extends Block> T blockRegister(String name, T block) {
        return Registry.register(Registries.BLOCK, Pearlink.id(name), block);
    }

    public static <T extends Block> T blockRegisterWithItem(String name, T block, Item.Settings settings) {
        T registered = blockRegister(name, block);
        itemRegister(name, new BlockItem(registered, settings.useBlockPrefixedTranslationKey()));
        return registered;
    }

    public static <T extends Block> T blockRegisterWithItem(String name, T block) {
        return blockRegisterWithItem(name, block, new Item.Settings());
    }

    public static RegistryKey<Block> createBlockRegistryKey(String name) {
        Identifier id = Pearlink.id(name);
        return RegistryKey.of(RegistryKeys.BLOCK, id);
    }

    public static RegistryKey<Item> createItemRegistryKey(String name) {
            Identifier id = Pearlink.id(name);
            return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    public static <T extends Item> T itemRegister(String name, T item) {
        Identifier id = Pearlink.id(name);
        return Registry.register(Registries.ITEM, id, item);
    }


}
