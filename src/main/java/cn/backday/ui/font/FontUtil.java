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
    public final FontRenderer mc16;

    public final FontRenderer mc18;
    public final FontRenderer mc20;
    public final FontRenderer mc23;
    public final FontRenderer mc30;
    public final FontRenderer mc35;
    public final FontRenderer misans_semibold30;

    public FontUtil() {
        mc16 = new FontRenderer("mc" , 16 ,Font.PLAIN,true , true);
        mc18 = new FontRenderer("mc" , 18 , Font.PLAIN, true , true);
        mc20 = new FontRenderer("mc" , 20 , Font.PLAIN, true , true);
        mc23 = new FontRenderer("mc" , 23 , Font.PLAIN, true , true);
        mc30 = new FontRenderer("mc" , 30 , Font.PLAIN, true , true);
        mc35 = new FontRenderer("mc" , 35 , Font.PLAIN, true , true);
        misans16 = new FontRenderer("MiSans-Regular", 16, Font.PLAIN, true, true);
        misans18 = new FontRenderer("MiSans-Regular", 18, Font.PLAIN, true, true);
        misans20 = new FontRenderer("MiSans-Regular", 20, Font.PLAIN, true, true);
        misans23 = new FontRenderer("MiSans-Regular", 23, Font.PLAIN, true, true);
        misans30 = new FontRenderer("MiSans-Regular", 30, Font.PLAIN, true, true);
        misans35 = new FontRenderer("MiSans-Regular", 35, Font.PLAIN, true, true);

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
