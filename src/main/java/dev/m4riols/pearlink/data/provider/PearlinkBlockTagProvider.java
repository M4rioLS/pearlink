package dev.m4riols.pearlink.data.provider;

import java.util.concurrent.CompletableFuture;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.init.BlockInit;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class PearlinkBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public PearlinkBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Block> TELEPORTABLE_TO_BLOCK = TagKey.create(Registries.BLOCK, Pearlink.id("teleportable_to_block"));

    // MC 26.2: tag appenders accept ResourceKey<Block> rather than Block values.
    private static ResourceKey<Block> key(Block block) {
        return block.builtInRegistryHolder().key();
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(key(BlockInit.CUSTOM_TELEPORTER_BLOCK));

        builder(TELEPORTABLE_TO_BLOCK)
            .add(key(Blocks.AIR))
            .add(key(Blocks.CAVE_AIR))
            .add(key(Blocks.VOID_AIR))
            .add(key(Blocks.STRUCTURE_VOID))
            .add(key(Blocks.WATER))
            .add(key(Blocks.LAVA))
            .add(key(Blocks.POWDER_SNOW))

            .add(key(Blocks.MOSS_CARPET))
            .add(key(Blocks.VINE))
            .add(key(Blocks.CAVE_VINES))
            .add(key(Blocks.CAVE_VINES_PLANT))
            .add(key(Blocks.HANGING_ROOTS))
            .add(key(Blocks.GLOW_LICHEN))
            .add(key(Blocks.WEEPING_VINES))
            .add(key(Blocks.WEEPING_VINES_PLANT))
            .add(key(Blocks.TWISTING_VINES))
            .add(key(Blocks.TWISTING_VINES_PLANT))
            .add(key(Blocks.SWEET_BERRY_BUSH))
            .add(key(Blocks.KELP))
            .add(key(Blocks.KELP_PLANT))
            .add(key(Blocks.SEAGRASS))
            .add(key(Blocks.TALL_SEAGRASS))
            .add(key(Blocks.SHORT_GRASS))
            .add(key(Blocks.TALL_GRASS))
            .add(key(Blocks.FROGSPAWN))
            .add(key(Blocks.LIGHT))
            .add(key(Blocks.TORCH))
            .add(key(Blocks.SOUL_TORCH))
            .add(key(Blocks.REDSTONE_TORCH))
            .add(key(Blocks.LEVER))
            .add(key(Blocks.LADDER))
            .add(key(Blocks.REDSTONE_WIRE))
            .add(key(Blocks.COBWEB))
            .add(key(Blocks.SCULK_VEIN))
            .add(key(Blocks.PINK_PETALS))
            .add(key(Blocks.SCAFFOLDING))
            .add(key(Blocks.TRIPWIRE))
            .add(key(Blocks.TRIPWIRE_HOOK))

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