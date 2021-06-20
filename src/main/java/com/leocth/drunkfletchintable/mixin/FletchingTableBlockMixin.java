package com.leocth.drunkfletchintable.mixin;

import com.leocth.drunkfletchintable.block.FletchingTableDelegate;
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FletchingTableBlock.class)
public abstract class FletchingTableBlockMixin
        extends CraftingTableBlock
        implements BlockEntityProvider {

    private static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    protected FletchingTableBlockMixin(Settings settings) { super(settings); }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return FletchingTableDelegate.getPlacementState(getDefaultState(), ctx);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return FletchingTableDelegate.createBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return FletchingTableDelegate.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(
            type,
            FletchinTableBlockEntity.TYPE,
            world.isClient ? null : FletchinTableBlockEntity::serverTick
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        FletchingTableDelegate.appendProperties(builder);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }
}
