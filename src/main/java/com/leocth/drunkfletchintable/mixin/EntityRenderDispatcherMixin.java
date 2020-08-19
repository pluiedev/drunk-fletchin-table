package com.leocth.drunkfletchintable.mixin;

import com.leocth.drunkfletchintable.tweaks.ImprovedArrowEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * this feels evil
 */
@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @ModifyArg(
            method = "registerRenderers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;" +
                            "register(Lnet/minecraft/entity/EntityType;Lnet/minecraft/client/render/entity/EntityRenderer;)V",
                    ordinal = 2
            ), index = 1)
    private EntityRenderer<?> register(EntityRenderer<?> renderer) {
        return new ImprovedArrowEntityRenderer(renderer.getRenderManager());
    }
}
