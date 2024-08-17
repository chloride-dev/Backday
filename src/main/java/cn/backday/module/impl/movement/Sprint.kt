package cn.backday.module.impl.movement

import cn.backday.event.impl.game.TickEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.player.MovementUtils
import cn.backday.value.impl.BoolValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.settings.KeyBinding

object Sprint : Module("Sprint", "auto sprint", ModuleCategory.Movement, true) {
    private val legit = BoolValue("Legit Mode", true)

    @EventTarget
    fun onTick(event: TickEvent) {
       // toggled = true
        if (legit.get()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.keyCode, MovementUtils.isMoving())
        } else {
            mc.thePlayer.isSprinting = MovementUtils.isMoving()
        }
    }
}