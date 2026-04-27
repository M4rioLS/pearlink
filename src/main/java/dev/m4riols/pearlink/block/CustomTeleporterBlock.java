package dev.m4riols.pearlink.block;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.block.entity.CustomTeleporterBlockEntity;
import dev.m4riols.pearlink.data.provider.PearlinkBlockTagProvider;
import dev.m4riols.pearlink.data.state.TeleporterBlockData;
import dev.m4riols.pearlink.init.BlockEntityTypeInit;
import dev.m4riols.pearlink.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomTeleporterBlock extends Block implements BlockEntityProvider{

    private static final long COOLDOWN_PERIOD = 5000;
    private static List<BlockPos> teleportBlockPositions = new ArrayList<>();
    public static final BooleanProperty LOCKED = Properties.LOCKED;

    private HashMap<UUID, Long> teleportCooldown = new HashMap<>();

    public CustomTeleporterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
            .with(LOCKED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LOCKED);
    }

    public static void addTeleportBlockPos(MinecraftServer world, BlockPos pos) {
        TeleporterBlockData data = TeleporterBlockData.getServerState(world);
        data.addTeleportBlockPos(pos);
        setTeleportBlockPositions(data.getTeleportBlockPositions());
    }

    public static void removeTeleportBlockPos(MinecraftServer world, BlockPos pos) {
        TeleporterBlockData data = TeleporterBlockData.getServerState(world);
        data.removeTeleportBlockPos(pos);
        setTeleportBlockPositions(data.getTeleportBlockPositions());
    }

    public static List<BlockPos> getTeleportBlockPositions() {
        return teleportBlockPositions;
    }

    public static void setTeleportBlockPositions(List<BlockPos> positions) {
        teleportBlockPositions = positions;
    }

    public static List<CustomTeleporterBlockEntity> findConnectedTeleportBlocks(World world, ItemStack item) {
        List<CustomTeleporterBlockEntity> connectedBlocks = new ArrayList<>();

        for (BlockPos pos : teleportBlockPositions) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CustomTeleporterBlockEntity teleportBlockEntity) {
                ItemStack storedItem = teleportBlockEntity.getStoredItem();
                if (ItemStack.areItemsEqual(storedItem, item)) {
                    connectedBlocks.add(teleportBlockEntity);
                }
            }
        }

        return connectedBlocks;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){
        if (state.get(LOCKED)) {
            player.sendMessage(Text.translatable("message.pearlink.teleporter_locked"), true);
            world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.FAIL;
        }
        if (!world.isClient()) {
            if(world.getBlockEntity(pos) instanceof CustomTeleporterBlockEntity teleporter)
                player.openHandledScreen(teleporter);
        }
        return ActionResult.SUCCESS;
    }
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        // Factory-instantiate (rather than `new`) so other mods can intercept via the block entity type.
        return BlockEntityTypeInit.CUSTOM_TELEPORTER_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            CustomTeleporterBlock.addTeleportBlockPos(world.getServer(), pos);
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!world.isClient()) {
            CustomTeleporterBlock.removeTeleportBlockPos(world.getServer(), pos);
        }
        super.afterBreak(world, player, pos, state, blockEntity, tool);
    }

    @Override
    public void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        TeleporterBlockData teleporterData = TeleporterBlockData.getServerState(world.getServer());
        teleporterData.removeTeleportBlockPos(pos);
        world.updateComparators(pos, this);
        super.onStateReplaced(state, world, pos, moved);
    }

    private void teleportPlayer(PlayerEntity player, BlockPos targetPos, ServerWorld world) {
        long currentTime = System.currentTimeMillis();

        UUID playerUUID = player.getUuid();
        long lastCollisionTime = teleportCooldown.getOrDefault(playerUUID, 0L);

        if (currentTime - lastCollisionTime > COOLDOWN_PERIOD) {
            BlockPos destination = targetPos.up();
            if (world.getBlockState(destination).isIn(PearlinkBlockTagProvider.TELEPORTABLE_TO_BLOCK) && world.getBlockState(destination.up()).isIn(PearlinkBlockTagProvider.TELEPORTABLE_TO_BLOCK)) {
                player.requestTeleport(destination.getX() + 0.5, destination.getY(), destination.getZ() + 0.5);

                teleportCooldown.put(playerUUID, currentTime);
                if (player instanceof ServerPlayerEntity)
                    MiscUtil.grantAdvancement((ServerPlayerEntity) player,  Pearlink.id("adventure/slipgate_surfer"));
                world.playSound(null, destination, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                player.sendMessage(Text.of("The teleport destination is obstructed."), true);
            }
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);

        if (!world.isClient() && entity instanceof PlayerEntity player) {
            CustomTeleporterBlockEntity blockEntity = (CustomTeleporterBlockEntity) world.getBlockEntity(pos);
            if (blockEntity == null) return;

            ItemStack itemInBlock = blockEntity.getStoredItem();
            if (!itemInBlock.isEmpty()) {
                List<CustomTeleporterBlockEntity> connectedBlocks = CustomTeleporterBlock.findConnectedTeleportBlocks(world, itemInBlock);

                if (!connectedBlocks.isEmpty()) {
                    BlockPos block_pos = null;
                    for (int i = 0; i < connectedBlocks.size(); i++) {
                        BlockPos bpos = connectedBlocks.get(i).getPos();
                        if (!bpos.equals(pos)) {
                            block_pos = bpos;
                            break;
                        }
                    }

                    if (block_pos != null)
                        teleportPlayer(player, block_pos, (ServerWorld) world);
                    else player.sendMessage(Text.of("Trying to tp to same block."), true);
                } else {
                    player.sendMessage(Text.of("No connected teleport block found."), true);
                }
            }
        } else if (world.isClient() && entity instanceof PlayerEntity) {
            spawnParticles(world, pos);
        }

    }

    private void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            double x = pos.getX() + world.random.nextDouble();
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + world.random.nextDouble();

            world.addParticleClient(ParticleTypes.PORTAL, x, y, z, 0, 0, 0);
        }
    }
}
