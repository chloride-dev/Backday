package cn.backday.event.impl.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class MotionEvent extends EventCancellable {
    private double posX, posY, posZ;
    private float yaw, pitch;
    private boolean onGround;
    private final EventState state;

    public MotionEvent(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround, EventState state) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.state = state;
    }

    public MotionEvent(EventState state) {
        this.state = state;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isPre() {
        return this.state == EventState.Pre;
    }

    public boolean isPost() {
        return this.state == EventState.Post;
    }

    public enum EventState {
        Pre, Post
    }
}
