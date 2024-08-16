package cn.backday.module.impl.movement

import cn.backday.api.event.impl.player.EventStrafe
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.value.impl.BoolValue
import com.darkmagician6.eventapi.EventTarget
import cn.backday.utils.player.RotationManager

object StrafeFix : Module("StrafeFix", "fixes strafing", ModuleCategory.Movement, true) {
    private val allowSprint = BoolValue("Allow Sprint", false)

    @EventTarget
    fun onStrafe(event: EventStrafe) {
        if (!RotationManager.active) return
        if (!allowSprint.get()) {
            mc.thePlayer.isSprinting = false
        }
        event.yaw = RotationManager.rotations.x
    }
}
