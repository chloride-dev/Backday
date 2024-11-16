package cn.backday.module.impl.combat

import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.misc.TimerUtils
import cn.backday.value.impl.BoolValue
import cn.backday.value.impl.FloatValue
import cn.backday.value.impl.IntValue
import cn.backday.value.impl.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.lwjgl.input.Keyboard

object Killaura : Module(
    "KillAura",
    "Automatically attack entities while keeping player view steady",
    ModuleCategory.Combat,
    Keyboard.KEY_K
) {
    private val maxCps = IntValue("Max CPS", 12, 1, 20)
    private val minCps = IntValue("Min CPS", 8, 1, 20)
    private val range = FloatValue("Range", 3.2F, 1.0F, 8.0F)
    private val attackTiming = ListValue("Attack Event", arrayOf("Pre", "Post"), "Pre")
    private val autoBlock = BoolValue("AutoBlock", false)
    private val autoBlockTiming =
        ListValue("AutoBlock Event", arrayOf("After Attack", "Before Attack", "Both"), "After Attack", autoBlock::get)
    private val autoBlockMode = ListValue("AutoBlock Mode", arrayOf("Packet", "Test"), "Packet", autoBlock::get)

    var target: EntityLivingBase? = null
    private val targets = mutableListOf<EntityLivingBase>()
    private val timer = TimerUtils()
    private var canAttack = false
    private var canBlock = false
    var isBlock = false

    override fun onDisable() {
        target = null
        targets.clear()
        canAttack = false
        canBlock = false
        if (isBlock) {
            sendPacket(
                C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN,
                    EnumFacing.DOWN
                )
            )
        }
        isBlock = false
        super.onDisable()
    }


}
