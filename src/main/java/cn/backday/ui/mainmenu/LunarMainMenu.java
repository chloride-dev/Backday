package cn.backday.ui.mainmenu;

import cn.backday.Client;
import cn.backday.ui.mainmenu.buttons.ImageButton;
import cn.backday.ui.mainmenu.buttons.MainButton;
import cn.backday.ui.mainmenu.buttons.QuitButton;
import cn.backday.ui.mainmenu.font.FontUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.io.IOException;

public class LunarMainMenu extends GuiScreen {
    private ResourceLocation logo;

    private MainButton btnSingleplayer;
    private MainButton btnMultiplayer;

    private ImageButton btnLunarOptions;
    private ImageButton btnCosmetics;
    private ImageButton btnMinecraftOptions;
    private ImageButton btnLanguage;

    private QuitButton btnQuit;

    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("Backday/mainmenu/panorama/panorama_0.png"), new ResourceLocation("Backday/mainmenu/panorama/panorama_1.png"), new ResourceLocation("Backday/mainmenu/panorama/panorama_2.png"), new ResourceLocation("Backday/mainmenu/panorama/panorama_3.png"), new ResourceLocation("Backday/mainmenu/panorama/panorama_4.png"), new ResourceLocation("Backday/mainmenu/panorama/panorama_5.png")};

    private static int panoramaTimer;
    private ResourceLocation backgroundTexture;

    @Override
    public void initGui() {
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", new DynamicTexture(256, 256));

        this.logo = new ResourceLocation("Backday/icons/icon_100.png");

        this.btnSingleplayer = new MainButton("S I N G L E P L A Y E R", this.width / 2 - 66, this.height / 2);
        this.btnMultiplayer = new MainButton("M U L T I P L A Y E R", this.width / 2 - 66, this.height / 2 + 15);

        int yPos = this.height - 20;
        this.btnLunarOptions = new ImageButton("LUNAR SETTINGS", new ResourceLocation("Backday/mainmenu/icons/lunar.png"), this.width / 2 - 30, yPos);
        this.btnCosmetics = new ImageButton("LUNAR COSMETICS", new ResourceLocation("Backday/mainmenu/icons/cosmetics.png"), this.width / 2 - 15, yPos);
        this.btnMinecraftOptions = new ImageButton("MINECRAFT SETTINGS", new ResourceLocation("Backday/mainmenu/icons/cog.png"), this.width / 2, yPos);
        this.btnLanguage = new ImageButton("LANGUAGE", new ResourceLocation("Backday/mainmenu/icons/globe.png"), this.width / 2 + 15, yPos);

        this.btnQuit = new QuitButton(this.width - 17, 7);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();

        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(logo);
        Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 25, this.height / 2 - 68, 0, 0, 49, 49, 49, 49);

        FontUtil.TITLE.getFont().drawCenteredString(Client.INSTANCE.getClientName().toUpperCase() + " CLIENT", (float) this.width / 2 - 0.25F, (float) this.height / 2 - 18, new Color(30, 30, 30, 70).getRGB());
        FontUtil.TITLE.getFont().drawCenteredString(Client.INSTANCE.getClientName().toUpperCase() + " CLIENT", (float) this.width / 2, (float) this.height / 2 - 19, -1);

        this.btnSingleplayer.drawButton(mouseX, mouseY);
        this.btnMultiplayer.drawButton(mouseX, mouseY);

        this.btnLunarOptions.drawButton(mouseX, mouseY);
        this.btnCosmetics.drawButton(mouseX, mouseY);
        this.btnMinecraftOptions.drawButton(mouseX, mouseY);
        this.btnLanguage.drawButton(mouseX, mouseY);

        this.btnQuit.drawButton(mouseX, mouseY);

        String s = "Copyright Mojang Studios. Do not distribute!";
        FontUtil.TEXT.getFont().drawString(Client.INSTANCE.getClientName() + " Client 1.8.9 (" + Client.INSTANCE.getClientCommit().substring(4) + "/" + Client.INSTANCE.getClientBranch() + ")", 7, this.height - 11, new Color(255, 255, 255, 100).getRGB());
        FontUtil.TEXT.getFont().drawString(s, this.width - FontUtil.TEXT.getFont().getWidth(s) - 6, this.height - 11, new Color(255, 255, 255, 100).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        ++panoramaTimer;
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int x = mouseX, y = mouseY;
            int yPos = this.height - 20;

            if ((x <= this.width / 2 - 66 + 132 && x >= this.width / 2 - 66) && (y >= this.height / 2 && y <= this.height / 2 + 11)) {
                mc.displayGuiScreen(new GuiSelectWorld(this));
            } else if ((x <= this.width / 2 - 66 + 132 && x >= this.width / 2 - 66) && (y >= this.height / 2 + 15 && y <= this.height / 2 + 11 + 15)) {
                mc.displayGuiScreen(new GuiMultiplayer(this));
            } else if ((x <= this.width / 2 + 12 && x >= this.width / 2) && (y >= yPos && y <= yPos + 12)) {
                mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            } else if ((x <= this.width / 2 + 15 + 12 && x >= this.width / 2 + 15) && (y >= yPos && y <= yPos + 12)) {
                mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
            } else if ((x <= this.width - 17 + 12 && x >= this.width - 17) && (y >= 7 && y <= 7 + 12)) {
                mc.shutdown();
            }
        }
    }

    //   Code from GuiMainMenu.java   //

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
            float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();

                if (k == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float) (j - i / 2) / 256.0F;
            worldrenderer.pos(k, l, this.zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(k, 0.0D, this.zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, l, this.zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float f1 = (float) this.height * f / 256.0F;
        float f2 = (float) this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, j, this.zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }
}
