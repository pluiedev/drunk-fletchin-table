@file:JvmName("DrunkFletchinTable")
package com.leocth.drunkfletchintable

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

const val MODID = "drunkfletchintable"

val FLETCHIN_TABLE = Identifier(MODID, "fletchin_table")
lateinit var FLETCHIN_TABLE_BE: BlockEntityType<FletchinTableBlockEntity>
lateinit var FLETCHIN_TABLE_SH: ScreenHandlerType<FletchinTableScreenHandler>
lateinit var FLETCHING_RECIPE_TYPE: RecipeType<FletchingRecipe>
lateinit var FLETCHING_RECIPE_SERIALIZER: RecipeSerializer<FletchingRecipe>

fun init() {
    FLETCHIN_TABLE_BE = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        FLETCHIN_TABLE,
        BlockEntityType.Builder.create(
            Supplier { FletchinTableBlockEntity() },
            Blocks.FLETCHING_TABLE
        ).build(null)
    )

    FLETCHIN_TABLE_SH = ScreenHandlerRegistry.registerSimple(FLETCHIN_TABLE)
    { syncId, playerInventory ->
        FletchinTableScreenHandler(syncId, playerInventory)
    }

    FLETCHING_RECIPE_TYPE = RecipeType.register("$MODID:fletching")

    FLETCHING_RECIPE_SERIALIZER = RecipeSerializer.register("$MODID:fletching", FletchingRecipe.Serializer())

    registerC2SPackets()
}

fun clientInit() {
    ScreenRegistry.register(FLETCHIN_TABLE_SH)
    { handler, playerInventory, title ->
        FletchinTableScreen(handler, playerInventory, title)
    }

    BlockEntityRendererRegistry.INSTANCE.register(FLETCHIN_TABLE_BE)
    { dispatcher -> FletchinTableBERenderer(dispatcher) }

    registerS2CPackets()
}
