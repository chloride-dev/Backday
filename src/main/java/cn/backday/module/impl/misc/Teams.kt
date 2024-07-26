package cn.backday.module.impl.misc

import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.value.impl.BoolValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor

object Teams : Module("Teams", "anti attack your teammates", ModuleCategory.Misc) {
    private val scoreboardValue = BoolValue("Scoreboard Check", true)
    private val colorValue = BoolValue("Color Check", true)
    private val armorValue = BoolValue("Armor Color Check", false)

    fun isInYourTeam(entity: EntityLivingBase): Boolean {
        if (!toggled) {
            return false
        }

        if (mc.thePlayer == null) {
            return false
        }

        if (scoreboardValue.get() && mc.thePlayer.team != null && entity.team != null && mc.thePlayer.team.isSameTeam(
                entity.team
            )
        ) {
            return true
        }

        if (armorValue.get()) {
            val entityPlayer = entity as EntityPlayer
            if (mc.thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null) {
                val myHead = mc.thePlayer.inventory.armorInventory[3]
                val myItemArmor = myHead.item as ItemArmor

                val entityHead = entityPlayer.inventory.armorInventory[3]
                val entityItemArmor = entityHead.item as ItemArmor

                if (myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)) {
                    return true
                }
            }
        }

        if (colorValue.get() && mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            val targetName = entity.getDisplayName().getFormattedText().replace("§r", "")
            val clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "")
            return targetName.startsWith("§" + clientName[1])
        }

        return false
    }
}