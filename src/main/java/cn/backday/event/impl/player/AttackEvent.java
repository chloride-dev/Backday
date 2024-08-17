package cn.backday.event.impl.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.entity.Entity;

public class AttackEvent extends EventCancellable {
    private Entity entity;

    public AttackEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
