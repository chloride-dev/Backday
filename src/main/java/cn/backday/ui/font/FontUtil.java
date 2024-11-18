package cn.backday.ui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class FontUtil {
    public final FontRenderer misans16;
    public final FontRenderer misans18;
    public final FontRenderer misans20;
    public final FontRenderer misans23;
    public final FontRenderer misans30;
    public final FontRenderer misans35;

    public final FontRenderer misans_semibold30;

    public FontUtil() {
        misans16 = new FontRenderer("MiSans-Bold", 16, Font.PLAIN, true, true);
        misans18 = new FontRenderer("MiSans-Bold", 18, Font.PLAIN, true, true);
        misans20 = new FontRenderer("MiSans-Bold", 20, Font.PLAIN, true, true);
        misans23 = new FontRenderer("MiSans-Bold", 23, Font.PLAIN, true, true);
        misans30 = new FontRenderer("MiSans-Bold", 30, Font.PLAIN, true, true);
        misans35 = new FontRenderer("MiSans-Bold", 35, Font.PLAIN, true, true);

        misans_semibold30 = new FontRenderer("MiSans-Semibold", 30, Font.PLAIN, true, true);
    }

    public static Font getFontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }
}
