package dev.m4riols.pearlink.data.provider;

import java.util.concurrent.CompletableFuture;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.init.BlockInit;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class PearlinkBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public PearlinkBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    public static final TagKey<Block> TELEPORTABLE_TO_BLOCK = TagKey.create(Registries.BLOCK, Pearlink.id("teleportable_to_block"));

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {

        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
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
