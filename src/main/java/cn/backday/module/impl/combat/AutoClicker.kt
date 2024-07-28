package cn.backday.module.impl.combat

import cn.backday.api.event.impl.game.TickEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.math.MathUtils
import cn.backday.utils.misc.TimerUtils
import cn.backday.value.impl.BoolValue
import cn.backday.value.impl.IntValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.ItemSword
import org.lwjgl.input.Mouse

object AutoClicker : Module("AutoClicker", "auto click", ModuleCategory.Combat) {
    private val maxCps = IntValue("Max CPS", 12, 1, 20)
    private val minCps = IntValue("Min CPS", 8, 1, 20)
    private val blockHit = BoolValue("Block Hit", false)
    private val autoUnBlock = BoolValue("Auto Unblock", false)

    private val timer = TimerUtils()

    @EventTarget
    private fun onTick(event: TickEvent) {
        if (mc.currentScreen == null && Mouse.isButtonDown(0)) {
            if (!blockHit.value && mc.thePlayer.isUsingItem) return

            if (timer.hasTimeElapsed((MathUtils.getRandomInRange(minCps.value, maxCps.value) / 1000).toLong(), true)) {
                mc.leftClickCounter = 0
                mc.clickMouse()

                if (autoUnBlock.value) {
                    if (Mouse.isButtonDown(1)) {
                        if (mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item != null && mc.thePlayer.heldItem.item is ItemSword) {
                            if (mc.thePlayer.isBlocking) {
                                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, false)
                                mc.playerController.onStoppedUsingItem(mc.thePlayer)
                                mc.thePlayer.itemInUseCount = 0
                            } else {
                                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
                                mc.playerController.sendUseItem(
                                    mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
