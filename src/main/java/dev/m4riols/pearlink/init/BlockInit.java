package dev.m4riols.pearlink.init;

import dev.m4riols.pearlink.block.CustomTeleporterBlock;
import dev.m4riols.pearlink.util.RegisterUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockInit {

        public static final CustomTeleporterBlock CUSTOM_TELEPORTER_BLOCK = RegisterUtil.blockRegister("teleport_block", new CustomTeleporterBlock(AbstractBlock.Settings.create()
            .strength(6.0F, 99.0F)
            .requiresTool()
            .registryKey(RegisterUtil.createBlockRegistryKey("teleport_block"))
            )
        );

        public static final Item CUSTOM_TELEPORTER_BLOCK_ITEM = RegisterUtil.itemRegister("teleport_block", new BlockItem(CUSTOM_TELEPORTER_BLOCK, new Item.Settings()
            .useBlockPrefixedTranslationKey()
            .maxCount(64)
            .registryKey(RegisterUtil.createItemRegistryKey("teleport_block"))
        ));

        public static void load( ){}

    }
