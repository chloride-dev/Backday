package cn.backday.event.impl.render;

import cn.backday.utils.math.vector.Vector2f;
import com.darkmagician6.eventapi.events.Event;

public class LookEvent implements Event {
    private Vector2f rotation;

    public LookEvent(Vector2f rotation) {
        this.rotation = rotation;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
    }
}
