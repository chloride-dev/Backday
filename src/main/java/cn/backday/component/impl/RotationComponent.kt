package cn.backday.component.impl

import cn.backday.api.event.impl.player.*
import cn.backday.api.event.impl.render.LookEvent
import cn.backday.component.Component
import cn.backday.component.impl.rotationcomponent.MovementFix
import cn.backday.utils.math.vector.Vector2f
import cn.backday.utils.player.MovementUtils
import cn.backday.utils.rotation.RotationUtil
import com.darkmagician6.eventapi.EventTarget
import com.darkmagician6.eventapi.types.Priority
import kotlin.math.abs

class RotationComponent : Component() {
    @EventTarget(Priority.LOWEST)
    private fun onUpdate(event: UpdateEvent) {
        if (!active || rotations == null || lastRotations == null || targetRotations == null || lastServerRotations == null) {
            lastServerRotations = Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
            targetRotations = lastServerRotations
            lastRotations = targetRotations
            rotations = lastRotations
        }

        if (active) {
            smooth()
        }

        //        mc.thePlayer.rotationYaw = rotations.x;
//        mc.thePlayer.rotationPitch = rotations.y;
        if (correctMovement == MovementFix.BACKWARDS_SPRINT && active) {
            if (abs(rotations!!.x - Math.toDegrees(MovementUtils.getDirection())) > 45) {
                mc.gameSettings.keyBindSprint.pressed = false
                mc.thePlayer.isSprinting = false
            }
        }
    }

    @EventTarget(Priority.LOWEST)
    private fun onMoveInput(event: MoveInputEvent) {
        if (active && correctMovement == MovementFix.NORMAL && rotations != null) {
            /*
             * Calculating movement fix
             */
            val yaw = rotations!!.x
            MovementUtils.fixMovement(event, yaw)
        }
    }

    @EventTarget(Priority.LOWEST)
    private fun onLook(event: LookEvent) {
        if (active && rotations != null) {
            event.rotation = rotations
        }
    }

    @EventTarget(Priority.LOWEST)
    private fun onStrafe(event: StrafeEvent) {
        if (active && (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL) && rotations != null) {
            event.yaw = rotations!!.x
        }
    }

    @EventTarget(Priority.LOWEST)
    private fun onJump(event: JumpEvent) {
        if (active && (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL || correctMovement == MovementFix.BACKWARDS_SPRINT) && rotations != null) {
            event.yaw = rotations!!.x
        }
    }

    @EventTarget(Priority.LOWEST)
    private fun onMotion(event: MotionEvent) {
        if (active && rotations != null) {
            val yaw = rotations!!.x
            val pitch = rotations!!.y

            event.yaw = yaw
            event.pitch = pitch

            //            mc.thePlayer.rotationYaw = yaw;
//            mc.thePlayer.rotationPitch = pitch;
            mc.thePlayer.renderYawOffset = yaw
            mc.thePlayer.rotationYawHead = yaw
            mc.thePlayer.renderPitchHead = pitch

            lastServerRotations = Vector2f(yaw, pitch)

            if (abs(((rotations!!.x - mc.thePlayer.rotationYaw) % 360).toDouble()) < 1 && abs(
                    (rotations!!.y - mc.thePlayer.rotationPitch).toDouble()
                ) < 1
            ) {
                active = false

                this.correctDisabledRotations()
            }

            lastRotations = rotations
        } else {
            lastRotations = Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        }

        targetRotations = Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        smoothed = false
    }

    private fun correctDisabledRotations() {
        val rotations = Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        val fixedRotations = RotationUtil.resetRotation(RotationUtil.applySensitivityPatch(rotations, lastRotations))

        mc.thePlayer.rotationYaw = fixedRotations.x
        mc.thePlayer.rotationPitch = fixedRotations.y
    }

    companion object {
        private var active = false
        private var smoothed = false
        var rotations: Vector2f? = null
        var lastRotations: Vector2f? = null
        var targetRotations: Vector2f? = null
        var lastServerRotations: Vector2f? = null
        private var rotationSpeed = 0.0
        private var correctMovement: MovementFix? = null

        /*
     * This method must be called on Pre Update Event to work correctly
     */
        fun setRotations(rotations: Vector2f?, rotationSpeed: Double, correctMovement: MovementFix?) {
            targetRotations = rotations
            Companion.rotationSpeed = rotationSpeed * 18
            Companion.correctMovement = correctMovement
            active = true

            smooth()
        }

        fun smooth() {
            if (!smoothed) {
                val lastYaw = lastRotations!!.x
                val lastPitch = lastRotations!!.y
                val targetYaw = targetRotations!!.x
                val targetPitch = targetRotations!!.y

                rotations = RotationUtil.smooth(
                    Vector2f(lastYaw, lastPitch),
                    Vector2f(targetYaw, targetPitch),
                    rotationSpeed + Math.random()
                )

                if (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL) {
                    mc.thePlayer.movementYaw = rotations!!.x
                }

                mc.thePlayer.velocityYaw = rotations!!.x
            }

            smoothed = true

            /*
         * Updating MouseOver
         */
            mc.entityRenderer.getMouseOver(1f)
        }
    }
}
