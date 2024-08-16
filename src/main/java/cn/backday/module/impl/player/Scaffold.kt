package cn.backday.module.impl.player

import cn.backday.api.event.impl.player.UpdateEvent
import cn.backday.component.impl.RotationComponent
import cn.backday.component.impl.rotationcomponent.MovementFix
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.math.MathUtils
import cn.backday.utils.math.vector.Vector2f
import cn.backday.value.impl.BoolValue
import cn.backday.value.impl.FloatValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.block.Block
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.lwjgl.input.Keyboard

object Scaffold : Module("Scaffold", "Simulate sneaking while placing blocks", ModuleCategory.Player) {
    private val placeRange = FloatValue("Place Range", 1f, 1f, 6f)
    private val rotationSpeed = FloatValue("Rotation Speed", 180f, 1f, 360f)

    private var blockPlaced = false
    private var isScaffolding = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.currentScreen != null || !mc.thePlayer.onGround) return

        // Automatically enable scaffolding if a block is found in the hotbar
        if (!isScaffolding) {
            val blockInHotbar = findBlockInHotbar()
            if (blockInHotbar != null) {
                isScaffolding = true
            } else {
                this.toggle() // Disable module if no blocks are available
                return
            }
        }

        val blockBelow = BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)
        if (mc.theWorld.isAirBlock(blockBelow)) {
            // Calculate head rotation
            val yawToPlace = mc.thePlayer.rotationYaw + 180 // Always facing opposite direction

            // Set head rotation to the block placement position
            RotationComponent.setRotations(Vector2f(yawToPlace, mc.thePlayer.rotationPitch), rotationSpeed.get().toDouble(), MovementFix.NORMAL)

            placeBlockScaffold(blockBelow)
        } else {
            // Disable sprinting and sneaking when not placing blocks
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, false)
            mc.thePlayer.isSprinting = false
        }
    }

    private fun placeBlockScaffold(blockPos: BlockPos) {
        for (direction in EnumFacing.values()) {
            val offsetPos = blockPos.offset(direction)
            val block = mc.theWorld.getBlockState(offsetPos).block

            if (block != Blocks.air && block.canCollideCheck(mc.theWorld.getBlockState(offsetPos), false)) {
                val side = direction.opposite
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.heldItem, offsetPos, side, mc.objectMouseOver.hitVec)
                mc.thePlayer.swingItem()
                blockPlaced = true
                break
            }
        }
    }

    private fun findBlockInHotbar(): Item? {
        for (i in 0 until 9) {
            val itemStack = mc.thePlayer.inventory.mainInventory[i]
            if (itemStack != null && itemStack.item is ItemBlock) {
                return itemStack.item
            }
        }
        return null
    }
}
