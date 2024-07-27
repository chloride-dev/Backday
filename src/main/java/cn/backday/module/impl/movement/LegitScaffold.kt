package cn.backday.module.impl.movement

import cn.backday.api.event.impl.player.MotionEvent
import cn.backday.api.event.impl.player.MoveInputEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.player.PlayerUtils
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.block.BlockAir
import net.minecraft.item.ItemBlock
import org.lwjgl.input.Keyboard

object LegitScaffold : Module("LegitScaffold", "auto sneak", ModuleCategory.Movement, Keyboard.KEY_G) {
    private var sneaked = false
    private var ticksOverEdge = -1

    override fun onDisable() {
        if (sneaked) {
            sneaked = false
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.isPre) {
            if (mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item !is ItemBlock) {
                if (sneaked) {
                    sneaked = false
                }

                return
            }

            if (mc.thePlayer.onGround && (PlayerUtils.blockRelativeToPlayer(
                    0.0, -1.0, 0.0
                ) is BlockAir) && !mc.gameSettings.keyBindForward.isKeyDown
            ) {
                if (!sneaked) {
                    sneaked = true
                }
            } else if (sneaked) {
                sneaked = false
            }

            if (sneaked) {
                mc.gameSettings.keyBindSprint.pressed = false
            }

            if (sneaked) {
                ticksOverEdge++
            } else {
                ticksOverEdge = 0
            }
        }
    }

    @EventTarget
    fun onMoveInput(event: MoveInputEvent) {
        event.isSneak = sneaked || mc.gameSettings.keyBindSneak.isKeyDown
    }
}