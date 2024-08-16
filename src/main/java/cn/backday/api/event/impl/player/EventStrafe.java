package cn.backday.api.event.impl.player;

import cn.backday.api.event.impl.CancellableEvent;
import net.minecraft.client.Minecraft;

public class EventStrafe extends CancellableEvent {
    private float strafe;
    private float up;
    private float forward;
    private float friction;
    private float yaw;

    private static final Minecraft mc = Minecraft.getMinecraft();

    public EventStrafe(float strafe, float up, float forward, float friction, float yaw) {
        this.strafe = strafe;
        this.up = up;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getUp() {
        return up;
    }

    public void setUp(float up) {
        this.up = up;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setSpeed(double speed, double motionMultiplier) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        mc.thePlayer.motionX *= motionMultiplier;
        mc.thePlayer.motionZ *= motionMultiplier;
    }

    public void slow(float v) {
        forward *= v;
        strafe *= v;
    }
}
