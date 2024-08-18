package cn.backday.event.impl.render;

import com.darkmagician6.eventapi.events.Event;

/**
 * ??3D???????
 */
public class Render3DEvent implements Event {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
