package cn.backday.module.impl.combat

import cn.backday.event.impl.player.MotionEvent
import cn.backday.event.impl.render.Render2DEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.math.MathUtils
import cn.backday.utils.math.vector.Vector2f
import cn.backday.utils.misc.TargetUtil
import cn.backday.utils.rotation.RotationUtil
import cn.backday.value.impl.BoolValue
import cn.backday.value.impl.IntValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MovingObjectPosition
import org.lwjgl.input.Keyboard

object AimAssist : Module("AimAssist", "auto aim", ModuleCategory.Combat, Keyboard.KEY_F) {
    private val strengthMax = IntValue("Max Strength", 30, 1, 100)
    private val strengthMin = IntValue("Min Strength", 10, 1, 100)
    private val onRotate = BoolValue("On Mouse Movement", false)

    private var rotations: Vector2f? = null
    private var lastRotations: Vector2f? = null

    override fun onDisable() {
        EntityRenderer.mouseAddedX = 0f
        EntityRenderer.mouseAddedY = 0f
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.isPre) {
            lastRotations = rotations
            rotations = null


            if (mc.objectMouseOver.typeOfHit === MovingObjectPosition.MovingObjectType.ENTITY) return

            var target: Entity? = null

            for (entity in mc.theWorld.loadedEntityList) {
                if (TargetUtil.shouldAddEntity(entity, 5.0)) {
                    target = entity as EntityLivingBase?
                    break
                }
            }

            rotations = RotationUtil.calculate(target)
        }
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (rotations == null || lastRotations == null || (onRotate.get() && (mc.mouseHelper.deltaX == 0) && (mc.mouseHelper.deltaY == 0))) {
            return
        }

        val rotations =
            Vector2f(lastRotations!!.x + (rotations!!.x - lastRotations!!.x) * mc.timer.renderPartialTicks, 0f)
        val f = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f
        val gcd = f * f * f * 8.0f
        val i = if (mc.gameSettings.invertMouse) -1 else 1
        val f2 = mc.mouseHelper.deltaX + ((rotations.x - mc.thePlayer.rotationYaw) * (MathUtils.getRandomInRange(
            strengthMin.get().toFloat(), strengthMax.get().toFloat()
        ) / 100) - mc.mouseHelper.deltaX) * gcd
        val f3 = mc.mouseHelper.deltaY - mc.mouseHelper.deltaY * gcd

        mc.thePlayer.setAngles(f2, f3 * i.toFloat())
    }
}