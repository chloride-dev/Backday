package cn.backday.utils.player;

import cn.backday.utils.MinecraftInterface;

public class MovementUtils implements MinecraftInterface {
    public static float getSpeed() {
        return (float) getSpeed(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static boolean isOnGround() {
        return mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically;
    }

    public static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }

    public static void strafe(final float speed) {
        if (!isMoving()) return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;
        if (mc.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0F) forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;

        if (mc.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static double speed() {
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static void resetMotion(boolean resetY) {
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
        if (resetY) mc.thePlayer.motionY = 0.0;
    }
}
