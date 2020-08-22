@file:JvmName("DrunkFletchinTable")
package com.leocth.drunkfletchintable

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
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

val TATERTATER = TinyTaterrBlokk()

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

    Registry.register(Registry.BLOCK, Identifier(MODID, "tatertater"), TATERTATER)
    Registry.register(Registry.ITEM, Identifier(MODID, "tatertater"), BlockItem(TATERTATER, Item.Settings()))

    BlockRenderLayerMap.INSTANCE.putBlock(TATERTATER, RenderLayer.getCutoutMipped())
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

    ModelLoadingRegistry.INSTANCE.registerAppender { _, out ->
        // "fake blocks"
        out.accept(ModelIdentifier("$MODID:tipping_widget_unloaded#"))
        out.accept(ModelIdentifier("$MODID:tipping_widget_loaded#"))
        out.accept(ModelIdentifier("$MODID:tipping_widget_pouring#"))
        out.accept(ModelIdentifier("$MODID:tipping_widget_liquid#"))
        out.accept(ModelIdentifier("$MODID:tipping_widget_pouring_liquid#"))
        out.accept(ModelIdentifier("$MODID:tipping_widget_bottle#"))
        out.accept(ModelIdentifier("$MODID:tipping_widget_bottle_mounted#"))
        out.accept(ModelIdentifier("$MODID:naif#inventory"))
    }
}
