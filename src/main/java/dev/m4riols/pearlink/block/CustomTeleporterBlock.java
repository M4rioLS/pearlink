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
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CustomTeleporterBlock extends Block implements EntityBlock {

    private static final long COOLDOWN_PERIOD = 5000;
    private static List<BlockPos> teleportBlockPositions = new ArrayList<>();
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    private HashMap<UUID, Long> teleportCooldown = new HashMap<>();

    public CustomTeleporterBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(LOCKED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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

    public static List<CustomTeleporterBlockEntity> findConnectedTeleportBlocks(Level world, ItemStack item) {
        List<CustomTeleporterBlockEntity> connectedBlocks = new ArrayList<>();

        for (BlockPos pos : teleportBlockPositions) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CustomTeleporterBlockEntity teleportBlockEntity) {
                ItemStack storedItem = teleportBlockEntity.getStoredItem();
                if (ItemStack.isSameItem(storedItem, item)) {
                    connectedBlocks.add(teleportBlockEntity);
                }
            }
        }

        return connectedBlocks;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(LOCKED)) {
            sendActionBar(player, Component.translatable("message.pearlink.teleporter_locked"));
            world.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.FAIL;
        }
        if (!world.isClientSide()) {
            if (world.getBlockEntity(pos) instanceof CustomTeleporterBlockEntity teleporter)
                player.openMenu(teleporter);
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Factory-instantiate (rather than `new`) so other mods can intercept via the block entity type.
        return BlockEntityTypeInit.CUSTOM_TELEPORTER_BLOCK_ENTITY.create(pos, state);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            CustomTeleporterBlock.addTeleportBlockPos(world.getServer(), pos);
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!world.isClientSide()) {
            CustomTeleporterBlock.removeTeleportBlockPos(world.getServer(), pos);
        }
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        TeleporterBlockData teleporterData = TeleporterBlockData.getServerState(world.getServer());
        teleporterData.removeTeleportBlockPos(pos);
        world.updateNeighbourForOutputSignal(pos, this);
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }

    private void teleportPlayer(Player player, BlockPos targetPos, ServerLevel world) {
        long currentTime = System.currentTimeMillis();

        UUID playerUUID = player.getUUID();
        long lastCollisionTime = teleportCooldown.getOrDefault(playerUUID, 0L);

        if (currentTime - lastCollisionTime > COOLDOWN_PERIOD) {
            BlockPos destination = targetPos.above();
            if (world.getBlockState(destination).is(PearlinkBlockTagProvider.TELEPORTABLE_TO_BLOCK) && world.getBlockState(destination.above()).is(PearlinkBlockTagProvider.TELEPORTABLE_TO_BLOCK)) {
                player.teleportTo(destination.getX() + 0.5, destination.getY(), destination.getZ() + 0.5);

                teleportCooldown.put(playerUUID, currentTime);
                if (player instanceof ServerPlayer)
                    MiscUtil.grantAdvancement((ServerPlayer) player, Pearlink.id("adventure/slipgate_surfer"));
                world.playSound(null, destination, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else {
                sendActionBar(player, Component.literal("The teleport destination is obstructed."));
            }
        }
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(world, pos, state, entity);

        if (!world.isClientSide() && entity instanceof Player player) {
            CustomTeleporterBlockEntity blockEntity = (CustomTeleporterBlockEntity) world.getBlockEntity(pos);
            if (blockEntity == null) return;

            ItemStack itemInBlock = blockEntity.getStoredItem();
            if (!itemInBlock.isEmpty()) {
                List<CustomTeleporterBlockEntity> connectedBlocks = CustomTeleporterBlock.findConnectedTeleportBlocks(world, itemInBlock);

                if (!connectedBlocks.isEmpty()) {
                    BlockPos block_pos = null;
                    for (int i = 0; i < connectedBlocks.size(); i++) {
                        BlockPos bpos = connectedBlocks.get(i).getBlockPos();
                        if (!bpos.equals(pos)) {
                            block_pos = bpos;
                            break;
                        }
                    }

                    if (block_pos != null)
                        teleportPlayer(player, block_pos, (ServerLevel) world);
                    else sendActionBar(player, Component.literal("Trying to tp to same block."));
                } else {
                    sendActionBar(player, Component.literal("No connected teleport block found."));
                }
            }
        } else if (world.isClientSide() && entity instanceof Player) {
            spawnParticles(world, pos);
        }

    }

    private void spawnParticles(Level world, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            double x = pos.getX() + world.getRandom().nextDouble();
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + world.getRandom().nextDouble();

            world.addParticle(ParticleTypes.PORTAL, x, y, z, 0, 0, 0);
        }
    }

    private static void sendActionBar(Player player, Component message) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(message, true);
        }
    }
}
