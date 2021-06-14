package com.leocth.drunkfletchintable.block.entity

import com.leocth.drunkfletchintable.block.entity.modules.CraftingModule
import com.leocth.drunkfletchintable.block.entity.modules.TippingModule
import com.leocth.drunkfletchintable.util.*
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Nameable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FletchinTableBlockEntity(blockPos: BlockPos, blockState: BlockState):
    BlockEntity(TYPE, blockPos, blockState),
    BlockEntityClientSerializable, NamedScreenHandlerFactory
{
    var craftingModule: CraftingModule? = null
        private set
    var tippingModule: TippingModule? = null
        private set

    // TODO: this is too rigid for my tastes
    override fun readNbt(nbt: NbtCompound) {
        nbt.getCompound("modules") { modules ->
            modules.findCompound("crafting") {
                val crafting = craftingModule ?: CraftingModule(this)
                crafting.readNbt(it)
                craftingModule = crafting
            }
            modules.findCompound("tipping") {
                val tipping = tippingModule ?: TippingModule(this)
                tipping.readNbt(it)
                tippingModule = tipping
            }
        }
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putCompound("modules") { modules ->
            craftingModule?.let { crafting ->
                modules.putCompound("crafting") {
                    crafting.writeNbt(it)
                }
            }
            tippingModule?.let { tipping ->
                modules.putCompound("tipping") {
                    tipping.writeNbt(it)
                }
            }
        }
        return nbt
    }

    override fun fromClientTag(nbt: NbtCompound) {
        nbt.getCompound("modules") { modules ->
            modules.findCompound("crafting") {
                craftingModule?.readClientNbt(it)
            }
            modules.findCompound("tipping") {
                tippingModule?.readClientNbt(it)
            }
        }
    }

    override fun toClientTag(nbt: NbtCompound): NbtCompound {
        nbt.putCompound("modules") { modules ->
            craftingModule?.let { crafting ->
                modules.putCompound("crafting") {
                    crafting.writeClientNbt(it)
                }
            }
            tippingModule?.let { tipping ->
                modules.putCompound("tipping") {
                    tipping.writeClientNbt(it)
                }
            }
        }
        return nbt
    }

    override fun getDisplayName() = TranslatableText("container.drunkfletchintable.fletchin_table")

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmField
        val TYPE: BlockEntityType<FletchinTableBlockEntity>
                = FabricBlockEntityTypeBuilder.create(::FletchinTableBlockEntity, Blocks.FLETCHING_TABLE).build()


        @JvmStatic
        fun serverTick(world: World, pos: BlockPos, state: BlockState, blockEntity: FletchinTableBlockEntity) {
            with(blockEntity) {
                tippingModule?.serverTick()
                craftingModule?.serverTick()
            }
        }
    }
}

