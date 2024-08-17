package cn.backday.utils.misc

import cn.backday.module.impl.client.Target
import cn.backday.module.impl.misc.Teams
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer

object TargetUtil {
    private val mc = Minecraft.getMinecraft()

    fun shouldAddEntity(entity: Entity, range: Double): Boolean {
        if (entity == mc.thePlayer) return false

        if (entity !is EntityLivingBase) return false

        if (!Target.deaths.get() && !entity.isEntityAlive) return false

        if (mc.thePlayer.getDistanceToEntity(entity) > range) return false

        if (!Target.invisibles.get() && entity.isInvisible) return false

        if (!Target.teams.get() && Teams.isInYourTeam(entity)) return false

        if (Target.player.get() && entity is EntityPlayer) {
            return true
        }

        if (Target.mobs.get() && entity is EntityMob) {
            return true
        }

        if (Target.animals.get() && entity is EntityAnimal) {
            return true
        }

        return false
    }
}