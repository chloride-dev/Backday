package cn.backday.event.impl.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class JumpEvent extends EventCancellable {
    private float jumpMotion;
    private float yaw;

    public JumpEvent(float jumpMotion, float yaw) {
        this.jumpMotion = jumpMotion;
        this.yaw = yaw;
    }

    public float getJumpMotion() {
        return jumpMotion;
    }

    public void setJumpMotion(float jumpMotion) {
        this.jumpMotion = jumpMotion;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
