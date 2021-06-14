package com.leocth.drunkfletchintable.old

import com.google.gson.JsonObject
import com.leocth.drunkfletchintable.mixin.ShapedRecipeHacks
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.collection.DefaultedList

class FletchingRecipe(
    id: Identifier,
    group: String,
    width: Int, height: Int,
    private val inputs: DefaultedList<Ingredient>,
    output: ItemStack
) : ShapedRecipe(id, group, width, height, inputs, output)
{
    override fun getType(): RecipeType<*> = FLETCHING_RECIPE_TYPE

    class Serializer: RecipeSerializer<FletchingRecipe> {
        override fun write(buf: PacketByteBuf, recipe: FletchingRecipe) {
            buf.writeVarInt(recipe.width)
            buf.writeVarInt(recipe.height)
            buf.writeString(recipe.group)
            recipe.inputs.forEach {
                it.write(buf)
            }

            buf.writeItemStack(recipe.output)
        }

        override fun read(id: Identifier, json: JsonObject): FletchingRecipe {
            val group = JsonHelper.getString(json, "group", "")
            val map = ShapedRecipeHacks.callGetComponents(JsonHelper.getObject(json, "key"))
            val strings = ShapedRecipeHacks.callCombinePattern(*ShapedRecipeHacks.callGetPattern(JsonHelper.getArray(json, "pattern")))
            val width = strings[0].length
            val height = strings.size
            val defaultedList = ShapedRecipeHacks.callGetIngredients(strings, map, width, height)
            val itemStack = getItemStack(JsonHelper.getObject(json, "result"))
            return FletchingRecipe(id, group, width, height, defaultedList, itemStack)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): FletchingRecipe {
            val width = buf.readVarInt()
            val height = buf.readVarInt()
            val group = buf.readString()

            val defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY)

            for (k in defaultedList.indices) {
                defaultedList[k] = Ingredient.fromPacket(buf)
            }

            val output = buf.readItemStack()

            return FletchingRecipe(id, group, width, height, defaultedList, output)
        }

    }
}