package com.leocth.drunkfletchintable.old

import com.mojang.datafixers.util.Pair
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockRenderView
import java.util.*
import java.util.function.Function
import java.util.function.Supplier


abstract class TippingWidgetModel: UnbakedModel, BakedModel, FabricBakedModel {
    private val SPRITE_ID = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, Identifier("minecraft:block/furnace_top"))
    private lateinit var sprite: Sprite
    private var fletchingTableModel: BakedModel? = null
    private lateinit var unloadedMesh: Mesh
    private lateinit var loadedMesh: Mesh
    private lateinit var pouringMesh: Mesh

    override fun getModelDependencies() = mutableListOf<Identifier>()

    override fun bake(
        loader: ModelLoader,
        textureGetter: Function<SpriteIdentifier, Sprite>,
        settings: ModelBakeSettings,
        modelId: Identifier
    ): BakedModel? {
        sprite = textureGetter.apply(SPRITE_ID)
        val renderer = RendererAccess.INSTANCE.renderer
        val builder = renderer.meshBuilder()
        val emitter = builder.emitter
        val finder = renderer.materialFinder()

        val glassMat = finder.blendMode(0, BlendMode.TRANSLUCENT).find()
        val stdMat = finder.blendMode(0, BlendMode.SOLID).find()

        val bottleModel = loader.bake(Identifier("$MODID:tipping_widget/bottle"), settings)
        val bottleMountedModel = loader.bake(Identifier("$MODID:tipping_widget/bottle_mounted"), settings)
        val liquidModel = loader.bake(Identifier("$MODID:tipping_widget/liquid"), settings)
        val pouringLiquidModel = loader.bake(Identifier("$MODID:tipping_widget/pouring_liquid"), settings)
        val unloadedModel = loader.bake(Identifier("$MODID:tipping_widget/unloaded"), settings)
        val loadedModel = loader.bake(Identifier("$MODID:tipping_widget/loaded"), settings)
        val pouringModel = loader.bake(Identifier("$MODID:tipping_widget/pouring"), settings)
        fletchingTableModel = loader.bake(Identifier("minecraft:fletching_table"), settings)

        val random = Random()

        bottleModel?.getQuads(null, null, random)?.forEach {
            emitter.material(glassMat)
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }
        unloadedModel?.getQuads(null, null, random)?.forEach {
            emitter.material(stdMat)
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }
        unloadedMesh = builder.build()

        emitter.material(glassMat)
        bottleMountedModel?.getQuads(null, null, random)?.forEach {
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }
        emitter.material(stdMat)
        loadedModel?.getQuads(null, null, random)?.forEach {
            emitter.material(stdMat)
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }
        emitter.material(stdMat)
        liquidModel?.getQuads(null, null, random)?.forEach {
            emitter.material(stdMat)
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }

        loadedMesh = builder.build()

        emitter.material(glassMat)
        bottleMountedModel?.getQuads(null, null, random)?.forEach {
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }
        emitter.material(stdMat)
        pouringModel?.getQuads(null, null, random)?.forEach {
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }
        liquidModel?.getQuads(null, null, random)?.forEach {
            emitter.fromVanilla(it.vertexData, 0, false)
            emitter.emit()
        }

        return this
    }

    override fun getTextureDependencies(
        unbakedModelGetter: Function<Identifier, UnbakedModel>?,
        unresolvedTextureReferences: MutableSet<Pair<String, String>>?
    ): MutableCollection<SpriteIdentifier> {
        TODO("Not yet implemented")
    }

    override fun getQuads(state: BlockState?, face: Direction?, random: Random?): MutableList<BakedQuad> {
        TODO("Not yet implemented")
    }

    override fun getSprite(): Sprite {
        TODO("Not yet implemented")
    }

    override fun useAmbientOcclusion(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasDepth(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTransformation(): ModelTransformation {
        TODO("Not yet implemented")
    }

    override fun isSideLit(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isBuiltin(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY

    override fun isVanillaAdapter() = false

    override fun emitItemQuads(p0: ItemStack?, p1: Supplier<Random>?, p2: RenderContext?) {
        TODO("Not yet implemented")
    }

    override fun emitBlockQuads(
        view: BlockRenderView,
        state: BlockState,
        pos: BlockPos,
        random: Supplier<Random>,
        ctx: RenderContext
    ) {
        val be = view.getBlockEntity(pos)
        ctx.fallbackConsumer().accept(fletchingTableModel)

    }
}