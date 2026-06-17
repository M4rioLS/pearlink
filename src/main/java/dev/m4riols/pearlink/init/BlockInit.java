package dev.m4riols.pearlink.init;

import dev.m4riols.pearlink.block.CustomTeleporterBlock;
import dev.m4riols.pearlink.util.RegisterUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockInit {

        public static final CustomTeleporterBlock CUSTOM_TELEPORTER_BLOCK = RegisterUtil.blockRegister("teleport_block", new CustomTeleporterBlock(BlockBehaviour.Properties.of()
            .strength(6.0F, 99.0F)
            .requiresCorrectToolForDrops()
            .setId(RegisterUtil.createBlockRegistryKey("teleport_block"))
            )
        );

        public static final Item CUSTOM_TELEPORTER_BLOCK_ITEM = RegisterUtil.itemRegister("teleport_block", new BlockItem(CUSTOM_TELEPORTER_BLOCK, new Item.Properties()
            .useBlockDescriptionPrefix()
            .stacksTo(64)
            .setId(RegisterUtil.createItemRegistryKey("teleport_block"))
        ));

        public static void load( ){}

    }
