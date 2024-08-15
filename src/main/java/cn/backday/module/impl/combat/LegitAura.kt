package cn.backday.module.impl.combat

import cn.backday.api.event.impl.player.UpdateEvent
import cn.backday.component.impl.RotationComponent
import cn.backday.component.impl.rotationcomponent.MovementFix
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.math.CPSUtils
import cn.backday.utils.math.MathUtils
import cn.backday.utils.math.vector.Vector2f
import cn.backday.utils.misc.TargetUtil
import cn.backday.utils.misc.TimerUtils
import cn.backday.utils.rotation.RotationUtil
import cn.backday.value.impl.BoolValue
import cn.backday.value.impl.FloatValue
import cn.backday.value.impl.IntValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemFishingRod
import net.minecraft.item.ItemSword
import org.lwjgl.input.Keyboard


object LegitAura : Module("LegitAura", "killaura but legit", ModuleCategory.Combat, Keyboard.KEY_R) {
    private val searchRange = FloatValue("Range", 5f, 0f, 8f)
    private val cps = IntValue("CPS", 15, 1, 20)
    private val cpsRange = FloatValue("CPS Random Strength", 1f, 0.1f, 5f)
    private val maxRotationSpeed = IntValue("Max Rotation Speed", 60, 1, 180)
    private val minRotationSpeed = IntValue("Min Rotation Speed", 40, 1, 180)
    private val autoBlock = BoolValue("AutoBlock", true)
    private val blockDelay = IntValue("BlockDelay", 2, 1, 10, autoBlock::get)
    private val autoRod = BoolValue("AutoRod", true)


    private val timer = TimerUtils()

    private var target: EntityLivingBase? = null
    private var blocking = false
    private var fishingRodThrow = false
    private var fishingRodSwitchOld = 0

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.currentScreen != null) return

        if (!autoBlock.get()) blocking = false

        target = null

        for (entity in mc.theWorld.loadedEntityList) {
            if (TargetUtil.shouldAddEntity(entity, searchRange.get().toDouble())) {
                target = entity as EntityLivingBase?
                break
            }
        }

        val rotationSpeed = MathUtils.getRandomInRange(maxRotationSpeed.get(), minRotationSpeed.get()).toDouble()

        // Rotations
        if (target != null) {
            val rotation = RotationUtil.getRotationsToEntity(target, true)
            val rotationVec = Vector2f(rotation[0], rotation[1])

            RotationComponent.setRotations(rotationVec, rotationSpeed / 18, MovementFix.NORMAL)
            mc.thePlayer.rotationYaw = RotationComponent.rotations!!.x
            mc.thePlayer.rotationPitch = RotationComponent.rotations!!.y
        }


        // Attack & AutoRod
        if (target != null) {
            val cps = CPSUtils.generate(cps.get().toDouble(), cpsRange.get().toDouble())

            if (mc.thePlayer.ticksExisted % blockDelay.get() == 0) {
                startBlock()
            } else {
                stopBlock()
            }

            if (shouldAttack(cps)) {
                stopBlock()
                mc.clickMouse()

                if (autoRod.get()) {
                    for (i in mc.thePlayer.inventory.mainInventory.indices) {
                        if (i > 9) break

                        val itemStack = mc.thePlayer.inventory.mainInventory[i]

                        if (itemStack != null && itemStack.item is ItemFishingRod) {
                            if (fishingRodThrow) {
                                mc.rightClickDelayTimer = 0
                                mc.rightClickMouse()
                                mc.thePlayer.inventory.currentItem = fishingRodSwitchOld
                                fishingRodThrow = false
                            } else {
                                fishingRodSwitchOld = mc.thePlayer.inventory.currentItem
                                mc.thePlayer.inventory.currentItem = i
                                mc.rightClickDelayTimer = 0
                                mc.rightClickMouse()
                                fishingRodThrow = true
                            }

                            break
                        }
                    }
                }
            }
        } else {
            stopBlock()
        }
    }

    private fun startBlock() {
        if (autoBlock.get() && !blocking) {
            if (mc.thePlayer.heldItem?.item is ItemSword) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())
                blocking = true
            }
        }
    }

    private fun stopBlock() {
        if (autoBlock.get() && blocking) {
            if (mc.thePlayer.heldItem?.item is ItemSword) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, false)
                mc.playerController.onStoppedUsingItem(mc.thePlayer)
                mc.thePlayer.itemInUseCount = 0
                blocking = false
            }
        }
    }

    private fun shouldAttack(cps: Double): Boolean {
        val aps = 20 / cps
        return timer.hasTimeElapsed((50 * aps).toLong(), true)
    }
}