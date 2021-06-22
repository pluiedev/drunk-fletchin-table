package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.item.DftItemTags
import com.leocth.drunkfletchintable.screen.TippingScreenHandler
import com.leocth.drunkfletchintable.util.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.registry.Registry
import kotlin.reflect.KProperty

class TippingModule(blockEntity: FletchinTableBlockEntity): FletchinModule(blockEntity) {
    private val ticker = Ticker(2, this::finishedTipping)

    private var potionStack = ItemStack.EMPTY
        set(value) { field = whenPotionStackIsSet(value) }

    private var arrowStack = ItemStack.EMPTY
    private var productStack = ItemStack.EMPTY
    private val potion = PotionWithAmount(Potions.EMPTY, 0, this::checkPotionStack)

    override fun serverTick() {
        if (!canWork) {
            ticker.reset()
            return
        }
        ticker.tick()
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        nbt.getCompound("ticker", ticker::readNbt)
        potionStack = nbt.getItemStack("potionStack")
        arrowStack = nbt.getItemStack("arrowStack")
        productStack = nbt.getItemStack("productStack")
        nbt.putCompound("potion", potion::readNbt)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        nbt.putCompound("ticker", ticker::writeNbt)
        nbt.putItemStack("potionStack", potionStack)
        nbt.putItemStack("arrowStack", arrowStack)
        nbt.putItemStack("productStack", productStack)
        nbt.putCompound("potion", potion::writeNbt)
    }

    override fun readClientNbt(nbt: NbtCompound) { readNbt(nbt) }

    override fun writeClientNbt(nbt: NbtCompound) { writeNbt(nbt) }

    // TODO: this doesn't work since potionStack is a property
    private val inv = ListBasedInventory(this::potionStack, this::arrowStack, this::productStack)

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity): ScreenHandler
            = TippingScreenHandler(syncId, playerInv, inv, screenHandlerContext)

    override fun getDisplayName(): Text = TranslatableText("screen.drunkfletchintable.tipping")



    @Environment(EnvType.CLIENT)
    override fun createButton(x: Int, y: Int): Button
            = Button(x, y, 28, 182)



    // TODO: calculating this every tick is a bit wasteful
    private val canWork
        get() = !potion.isEmpty &&
                !arrowStack.isEmpty &&
                PotionUtil.getPotion(arrowStack) != potion.type &&
                !productStack.isFull &&
                // product stack is either empty or matches the type of the potion
                (productStack.isEmpty || PotionUtil.getPotion(productStack) == potion.type)

    private fun finishedTipping() {
        potion.amount -= 1
        arrowStack.decrement(1)
        if (productStack.isEmpty) {
            // make a new product stack
            productStack = ItemStack(Items.TIPPED_ARROW)
            PotionUtil.setPotion(productStack, potion.type)
        } else {
            productStack.increment(1)
        }
    }

    private fun renewPotionFromStack(stack: ItemStack): Boolean {
        if (!stack.isIn(DftItemTags.POTIONS)) return false

        /* TODO: See if we need to use a different method to verify if the stack is a potion

            This relies on `PotionUtil.getPotion`'s result to determine whether the new value is a potion.
            However, one can easily create a custom item that has a `Potion` tag to fool our system that it is a potion,
            and since this dispenses a glass bottle out after processing, I am not very sure if this is a right response
            since some items might not need a glass bottle to craft. Maybe I will add a tag if necessary.
         */

        val newType = PotionUtil.getPotion(stack)
        if (newType == Potions.EMPTY) return false

        potion.type = newType
        potion.amount = USES_PER_POTION_ITEM
        return true
    }

    private fun whenPotionStackIsSet(value: ItemStack): ItemStack {
        if (value.isEmpty) return value

        // if the potion was empty yet successfully renewed, return an empty bottle
        return if (potion.isEmpty && renewPotionFromStack(value)) {
            ItemStack(Items.GLASS_BOTTLE, value.count)
        } else value
    }

    private fun checkPotionStack() {
        if (renewPotionFromStack(potionStack)) {
            potionStack = ItemStack(Items.GLASS_BOTTLE, potionStack.count)
        }
    }

    override val type: ModuleType<*> = TYPE

    companion object {
        val TYPE = ModuleType(::TippingModule)
    }
}

// TODO create a config for this
private const val USES_PER_POTION_ITEM = 24

class PotionWithAmount(type: Potion, amount: Int, private val onDepletion: () -> Unit): NbtSerializable {
    private var _type: Potion = type
    private var _amount: Int = amount

    var type: Potion
        get() = _type
        set(value) {
            if (value == Potions.EMPTY)
                clear()
            else
                _type = value
        }
    var amount: Int
        get() = _amount
        set(value) {
            if (value <= 0)
                clear()
            else
                _amount = value
        }

    private fun clear() {
        _type = Potions.EMPTY
        _amount = 0
        onDepletion()
    }

    val isEmpty get() = type == Potions.EMPTY && amount >= 0

    override fun readNbt(nbt: NbtCompound) {
        type = Registry.POTION.get(nbt.getIdentifier("type"))
        amount = nbt.getInt("amount")
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putIdentifier("type", Registry.POTION.getId(type))
        nbt.putInt("amount", amount)
    }
}