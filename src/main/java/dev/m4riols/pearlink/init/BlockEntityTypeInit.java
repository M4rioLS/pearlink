package dev.m4riols.pearlink.init;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.block.entity.CustomTeleporterBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityTypeInit {
    public static final BlockEntityType<CustomTeleporterBlockEntity> CUSTOM_TELEPORTER_BLOCK_ENTITY =
    register("teleporter_block_entity", FabricBlockEntityTypeBuilder.create(CustomTeleporterBlockEntity::new,
    BlockInit.CUSTOM_TELEPORTER_BLOCK).build());


    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type){
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Pearlink.id(name), type);
    }

    public static void load(){

    }
}
