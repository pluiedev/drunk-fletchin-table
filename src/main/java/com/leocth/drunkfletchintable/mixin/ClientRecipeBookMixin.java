package com.leocth.drunkfletchintable.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.leocth.drunkfletchintable.DrunkFletchinTable.FLETCHING_RECIPE_TYPE;

@Mixin(ClientRecipeBook.class)
@Environment(EnvType.CLIENT)
public abstract class ClientRecipeBookMixin {
    /** mojang why tho **/
    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> info) {
        RecipeType<?> recipeType = recipe.getType();
        if (recipeType == FLETCHING_RECIPE_TYPE) {
            info.setReturnValue(RecipeBookGroup.UNKNOWN);
        }
    }
}
