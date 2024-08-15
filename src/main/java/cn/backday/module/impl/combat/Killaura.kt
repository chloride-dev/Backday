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
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.input.Keyboard

object Killaura : Module("Killaura", "Automatically attack entities while keeping player view steady", ModuleCategory.Combat, Keyboard.KEY_K) {
    private val searchRange = FloatValue("Range", 5f, 0f, 8f)
    private val cps = IntValue("CPS", 10, 1, 20)
    private val cpsRange = FloatValue("CPS Random Strength", 1f, 0.1f, 5f)
    private val autoBlock = BoolValue("AutoBlock", false)
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

        if (target != null) {
            // Calculate the angles needed to aim at the target
            val rotation = RotationUtil.getRotationsToEntity(target, true)
            val targetYaw = rotation[0]
            val targetPitch = rotation[1]

            // Apply rotation but keep player's view steady
            RotationComponent.setRotations(Vector2f(targetYaw, targetPitch), 1.0, MovementFix.NORMAL)

            // Check if the target direction and player's view direction are different
            val yawDifference = Math.abs(MathUtils.wrapAngleTo180_float(targetYaw - mc.thePlayer.rotationYaw))
            if (yawDifference > 90) {
                mc.thePlayer.isSprinting = false
            }

            // Handle weapon switch
            if (autoRod.get()) {
                // Check if the player is holding a fishing rod and if the weapon needs to be switched
                if (mc.thePlayer.inventory.currentItem < 9) {
                    // Find the first weapon and switch to it
                    for (i in mc.thePlayer.inventory.mainInventory.indices) {
                        val itemStack = mc.thePlayer.inventory.mainInventory[i]
                        if (itemStack != null && itemStack.item is ItemSword) {
                            mc.thePlayer.inventory.currentItem = i
                            break
                        }
                    }
                }
            } else {
                // Switch to a weapon if available, but avoid conflict with autoRod
                var foundWeapon = false
                for (i in mc.thePlayer.inventory.mainInventory.indices) {
                    val itemStack = mc.thePlayer.inventory.mainInventory[i]
                    if (itemStack != null && itemStack.item is ItemSword) {
                        if (mc.thePlayer.inventory.currentItem != i) {
                            mc.thePlayer.inventory.currentItem = i
                            foundWeapon = true
                        }
                        break
                    }
                }
                if (!foundWeapon) {
                    // If no weapon was found, do not switch
                }
            }

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
