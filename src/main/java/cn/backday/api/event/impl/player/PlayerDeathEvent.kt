package cn.backday.api.event.impl.player

import net.minecraft.entity.EntityLivingBase
import com.darkmagician6.eventapi.events.Event

class PlayerDeathEvent(val entity: EntityLivingBase) : Event
