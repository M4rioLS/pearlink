package dev.m4riols.pearlink.data.provider;

import java.util.concurrent.CompletableFuture;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.init.BlockInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class PearlinkBlockTagProvider extends FabricTagProvider.BlockTagProvider{

    public PearlinkBlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    public static final TagKey<Block> TELEPORTABLE_TO_BLOCK = TagKey.of(RegistryKeys.BLOCK, Pearlink.id("teleportable_to_block"));

    @Override
    protected void configure(WrapperLookup wrapperLookup) {

        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(BlockInit.CUSTOM_TELEPORTER_BLOCK);

        valueLookupBuilder(TELEPORTABLE_TO_BLOCK)
            .add(Blocks.AIR)
            .add(Blocks.CAVE_AIR)
            .add(Blocks.VOID_AIR)
            .add(Blocks.STRUCTURE_VOID)
            .add(Blocks.WATER)
            .add(Blocks.LAVA)
            .add(Blocks.POWDER_SNOW)

            .add(Blocks.MOSS_CARPET)
            .add(Blocks.VINE)
            .add(Blocks.CAVE_VINES)
            .add(Blocks.CAVE_VINES_PLANT)
            .add(Blocks.HANGING_ROOTS)
            .add(Blocks.GLOW_LICHEN)
            .add(Blocks.WEEPING_VINES)
            .add(Blocks.WEEPING_VINES_PLANT)
            .add(Blocks.TWISTING_VINES)
            .add(Blocks.TWISTING_VINES_PLANT)
            .add(Blocks.SWEET_BERRY_BUSH)
            .add(Blocks.KELP)
            .add(Blocks.KELP_PLANT)
            .add(Blocks.SEAGRASS)
            .add(Blocks.TALL_SEAGRASS)
            .add(Blocks.SHORT_GRASS)
            .add(Blocks.TALL_GRASS)
            .add(Blocks.FROGSPAWN)
            .add(Blocks.LIGHT)
            .add(Blocks.TORCH)
            .add(Blocks.SOUL_TORCH)
            .add(Blocks.REDSTONE_TORCH)
            .add(Blocks.LEVER)
            .add(Blocks.LADDER)
            .add(Blocks.REDSTONE_WIRE)
            .add(Blocks.COBWEB)
            .add(Blocks.SCULK_VEIN)
            .add(Blocks.PINK_PETALS)
            .add(Blocks.SCAFFOLDING)
            .add(Blocks.TRIPWIRE)
            .add(Blocks.TRIPWIRE_HOOK)

            .addOptionalTag(BlockTags.WOOL_CARPETS)
            .addOptionalTag(BlockTags.BUTTONS)
            .addOptionalTag(BlockTags.PRESSURE_PLATES)
            .addOptionalTag(BlockTags.RAILS)
            .addOptionalTag(BlockTags.SIGNS)
            .addOptionalTag(BlockTags.STANDING_SIGNS)
            .addOptionalTag(BlockTags.BANNERS)
            .addOptionalTag(BlockTags.FLOWERS)
            .addOptionalTag(BlockTags.CROPS)

            ;
    }
}
