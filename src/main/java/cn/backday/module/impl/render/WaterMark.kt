package cn.backday.module.impl.render

import cn.backday.Client
import cn.backday.event.impl.render.Render2DEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.utils.render.RoundedUtil
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import java.awt.Color

object WaterMark : Module("WaterMark", "water mark", ModuleCategory.Render) {
    private val logo = ResourceLocation("Backday/icons/icon_100.png")

    override fun onInitialize() {
        toggled = true
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (mc.gameSettings.showDebugInfo) return

        RoundedUtil.drawRound(8f, 8f, 20f, 20f, 5f, Color(0, 0, 0, 185))

        GlStateManager.enableBlend()
        GlStateManager.color(1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(logo)
        Gui.drawModalRectWithCustomSizedTexture(10, 10, 0f, 0f, 16, 16, 16f, 16f)
        GlStateManager.disableBlend()

        RoundedUtil.drawRound(32f, 8f, 73f, 20f, 5f, Color(0, 0, 0, 185))
        font.misans_semibold30.drawString(Client.clientName, 36f, 10f, -1)
    }
}