package com.leocth.drunkfletchintable.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeHacks {
    @Invoker static String[] callCombinePattern(String... lines) { return null; }
    @Invoker static int callFindNextIngredient(String pattern) { return 0;}
    @Invoker static int callFindNextIngredientReverse(String pattern) { return 0;}
    @Invoker static String[] callGetPattern(JsonArray json) { return null; }
    @Invoker static Map<String, Ingredient> callGetComponents(JsonObject json) { return null; }
    @Invoker static DefaultedList<Ingredient> callGetIngredients(String[] pattern, Map<String, Ingredient> key, int width, int height) { return null; }
}
