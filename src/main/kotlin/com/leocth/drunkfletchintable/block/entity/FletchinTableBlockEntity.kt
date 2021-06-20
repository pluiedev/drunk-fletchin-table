package com.leocth.drunkfletchintable.block.entity

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.entity.modules.FletchinModule
import com.leocth.drunkfletchintable.block.entity.modules.ModuleRegistry
import com.leocth.drunkfletchintable.block.entity.modules.ModuleType
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
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class FletchinTableBlockEntity(blockPos: BlockPos, blockState: BlockState):
    BlockEntity(TYPE, blockPos, blockState), BlockEntityClientSerializable
{
    val modules: MutableMap<Direction, FletchinModule> = mutableMapOf()

    companion object {
        @JvmField
        val TYPE: BlockEntityType<FletchinTableBlockEntity>
                = FabricBlockEntityTypeBuilder.create(::FletchinTableBlockEntity, Blocks.FLETCHING_TABLE).build()


        @JvmStatic
        fun serverTick(world: World, pos: BlockPos, state: BlockState, blockEntity: FletchinTableBlockEntity) {
            for ((_, module) in blockEntity.modules) {
                module.serverTick()
            }
        }
    }

    /// NBT stuff

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        nbt.getCompound("modules") { modulesNbt ->
            for (key in modulesNbt.keys) {
                // ignore invalid directions
                val dir = Direction.byName(key) ?: continue

                modulesNbt.findCompound(key) {
                    val id = it.getIdentifier("id")
                    val moduleType = ModuleRegistry.find(id)
                    if (moduleType == null) {
                        DrunkFletchinTable.LOGGER.warn("Module type not found: $id! Ignoring...")
                        return@findCompound
                    }

                    val module = getOrCreateModule(dir, moduleType)
                    module.readNbt(it)
                }
            }
        }
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        nbt.putCompound("modules") { modulesNbt ->
            for ((dir, module) in modules) {
                modulesNbt.putCompound(dir.getName()) next@{
                    val id = ModuleRegistry.findKey(module.type)
                    if (id == null) {
                        DrunkFletchinTable.LOGGER.error("Module type $module is not registered! What the heck?")
                        return@next
                    }
                    it.putIdentifier("id", id)
                    module.writeNbt(it)
                }
            }
        }
        return nbt
    }

    override fun fromClientTag(nbt: NbtCompound) {
        nbt.getCompound("modules") { modulesNbt ->
            for (key in nbt.keys) {
                // ignore invalid directions
                val dir = Direction.byName(key) ?: continue
                modulesNbt.findCompound(key) {
                    val id = it.getIdentifier("id")
                    val moduleType = ModuleRegistry.find(id)
                    if (moduleType == null) {
                        DrunkFletchinTable.LOGGER.warn("Module type not found: $id! Ignoring...")
                        return@findCompound
                    }

                    val module = getOrCreateModule(dir, moduleType)
                    module.readClientNbt(it)
                }
            }
        }
    }

    override fun toClientTag(nbt: NbtCompound): NbtCompound {
        nbt.putCompound("modules") { modulesNbt ->
            for ((dir, module) in modules) {
                modulesNbt.putCompound(dir.getName()) next@{
                    val id = ModuleRegistry.findKey(module.type)
                    if (id == null) {
                        DrunkFletchinTable.LOGGER.error("Module type $module is not registered! What the heck?")
                        return@next
                    }
                    it.putIdentifier("id", id)
                    module.writeClientNbt(it)
                }
            }
        }
        return nbt
    }

    private fun getOrCreateModule(dir: Direction, moduleType: ModuleType<*>): FletchinModule {
        return modules.compute(dir) { _, old ->
            if (old != null && old.type == moduleType) {
                old
            } else moduleType.factory(this)
        } ?: throw IllegalStateException("somehow `module` is null during this operation; this should NOT happen")
    }
}

