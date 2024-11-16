package cn.backday.module.impl.render

import cn.backday.Client
import cn.backday.event.impl.render.Render2DEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.value.impl.BoolValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11


object ArrayListMod : Module("Arraylist", "display modules", ModuleCategory.Render , true) {
    private val fontShadow = BoolValue("Font Shadow", true)

    override fun onInitialize() {
        toggled = true
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (mc.gameSettings.showDebugInfo) return

        val sr = ScaledResolution(mc)
        val mods = ArrayList<Module>()
        var arrayListY = 4f

        for (m in Client.moduleManager.getModules()) {
            if (!m.toggled) continue

            mods.add(m)
        }

        mods.sortWith { o1: Module, o2: Module ->
            font.misans20.getStringWidth(
                o2.moduleName
            ) - font.misans20.getStringWidth(
                o1.moduleName
            )
        }

        for (mod in mods) {
            GL11.glTranslated(0.0, -1.0, 0.0)
            GL11.glPushMatrix()

            if (!fontShadow.get()) {
                font.misans20.drawString(
                    mod.moduleName,
                    sr.scaledWidth - font.misans20.getStringWidth(mod.moduleName) - 7.2f,
                    arrayListY + 5,
                    -1
                )
            } else {
                font.misans20.drawStringWithShadow(
                    mod.moduleName,
                    sr.scaledWidth - font.misans20.getStringWidth(mod.moduleName) - 7.2f,
                    arrayListY + 5,
                    -1
                )
            }

            GL11.glPopMatrix()
            GL11.glTranslated(0.0, 1.0, 0.0)
            arrayListY += 12
        }
    }
}