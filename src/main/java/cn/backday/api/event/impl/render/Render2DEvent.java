package cn.backday.api.event.impl.render;

import com.darkmagician6.eventapi.events.Event;

public class Render2DEvent implements Event {
    private final float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
