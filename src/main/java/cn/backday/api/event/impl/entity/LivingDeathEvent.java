package cn.backday.api.event.impl.entity;

import net.minecraft.entity.EntityLivingBase;

public interface LivingDeathEvent {
    EntityLivingBase getEntity();
}
