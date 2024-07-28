package cn.backday.utils.math;

import java.util.Random;

public class CPSUtils {
    private static final Random random = new Random();

    public static double generateNoise(double min, double max) {
        double u1, u2, v1, v2, s;
        do {
            u1 = random.nextDouble() * 2 - 1;
            u2 = random.nextDouble() * 2 - 1;
            s = u1 * u1 + u2 * u2;
        } while (s >= 1 || s == 0);

        double multiplier = Math.sqrt(-2 * Math.log(s) / s);
        v1 = u1 * multiplier;
        v2 = u2 * multiplier;

        return (v1 + v2) / 2 * (max - min) / 4 + (max + min) / 2;
    }

    public static double generate(double cps, double range) {
        double noise = cps;
        for (int j = 0; j < 10; j++) {
            double newNoise = generateNoise(0, cps * 2);
            if (Math.abs(noise - newNoise) < range)
                noise = (noise + newNoise) / 2;
            else j--;
        }
        return noise;
    }
}
