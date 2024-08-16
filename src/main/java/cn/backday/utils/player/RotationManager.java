package cn.backday.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

public class RotationManager {
    public static boolean active = false;
    public static Vector2f rotations = new Vector2f(0, 0);
    private static double rotationSpeed = 1.0;

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setRotations(Vector2f targetRotations, double speed) {
        rotations = targetRotations;
        rotationSpeed = speed;
        active = true;
    }

    public static void onUpdate() {
        if (active) {
            smooth(rotations, rotationSpeed);
            // Example of stopping rotation when very close to the target
            if (Math.abs(rotations.x - mc.thePlayer.rotationYaw) < 1 && Math.abs(rotations.y - mc.thePlayer.rotationPitch) < 1) {
                stop();
            }
        }
    }

    public static void stop() {
        active = false;
    }

    public static void smooth(Vector2f targetRotations, double speed) {
        if (speed == 0) return;

        float yaw = targetRotations.x;
        float pitch = targetRotations.y;

        float deltaYaw = wrapAngleTo180(yaw - mc.thePlayer.rotationYaw);
        float deltaPitch = pitch - mc.thePlayer.rotationPitch;

        float maxYaw = (float) (speed * Math.abs(deltaYaw));
        float maxPitch = (float) (speed * Math.abs(deltaPitch));

        yaw = mc.thePlayer.rotationYaw + clamp(deltaYaw, -maxYaw, maxYaw);
        pitch = mc.thePlayer.rotationPitch + clamp(deltaPitch, -maxPitch, maxPitch);

        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    public static float wrapAngleTo180(float value) {
        value = value % 360.0F;
        if (value >= 180.0F) {
            value -= 360.0F;
        }
        if (value < -180.0F) {
            value += 360.0F;
        }
        return value;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static double[] getRotationsNeeded(Vec3 targetPos) {
        double x = targetPos.xCoord - mc.thePlayer.posX;
        double z = targetPos.zCoord - mc.thePlayer.posZ;
        double y = targetPos.yCoord - mc.thePlayer.posY;
        double distance = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(y, distance) * 180.0D / Math.PI));
        return new double[]{yaw, pitch};
    }
}
