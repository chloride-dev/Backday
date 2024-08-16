package cn.backday.module.impl.render

import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11
import cn.backday.api.event.impl.render.Render3DEvent
import org.lwjgl.input.Keyboard

object ESP : Module("ESP", "Draws outlines around entities", ModuleCategory.Render , Keyboard.KEY_I) {
    //private val mc: Minecraft = Minecraft.getMinecraft()

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (mc.gameSettings.showDebugInfo) return

        GlStateManager.pushMatrix()
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glLineWidth(2.5f) // Set line width for outline

        for (entity in mc.theWorld.loadedEntityList) {
            if (entity is Entity) {
                renderEntityOutline(entity)
            }
        }

        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.popMatrix()
    }

    private fun renderEntityOutline(entity: Entity) {
        val partialTicks = mc.timer.renderPartialTicks
        val posX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks
        val posY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks
        val posZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks

        val playerX = mc.thePlayer.posX
        val playerY = mc.thePlayer.posY
        val playerZ = mc.thePlayer.posZ

        val x = posX - playerX
        val y = posY - playerY
        val z = posZ - playerZ

        val scale = 0.5

        // Draw bounding box outline
        drawBoundingBox(x, y, z, scale)
    }

    private fun drawBoundingBox(x: Double, y: Double, z: Double, scale: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat(), y.toFloat(), z.toFloat())
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f) // Set color to white

        GL11.glLineWidth(2.5f) // Set line width for outline

        GL11.glBegin(GL11.GL_LINES)
        // Draw lines for bounding box
        GL11.glVertex3d(-scale, 0.0, -scale)
        GL11.glVertex3d(scale, 0.0, -scale)

        GL11.glVertex3d(scale, 0.0, -scale)
        GL11.glVertex3d(scale, 0.0, scale)

        GL11.glVertex3d(scale, 0.0, scale)
        GL11.glVertex3d(-scale, 0.0, scale)

        GL11.glVertex3d(-scale, 0.0, scale)
        GL11.glVertex3d(-scale, 0.0, -scale)

        GL11.glVertex3d(-scale, 0.0, -scale)
        GL11.glVertex3d(-scale, scale, -scale)

        GL11.glVertex3d(scale, 0.0, -scale)
        GL11.glVertex3d(scale, scale, -scale)

        GL11.glVertex3d(scale, 0.0, scale)
        GL11.glVertex3d(scale, scale, scale)

        GL11.glVertex3d(-scale, 0.0, scale)
        GL11.glVertex3d(-scale, scale, scale)

        GL11.glVertex3d(-scale, scale, -scale)
        GL11.glVertex3d(scale, scale, -scale)

        GL11.glVertex3d(scale, scale, -scale)
        GL11.glVertex3d(scale, scale, scale)

        GL11.glVertex3d(scale, scale, scale)
        GL11.glVertex3d(-scale, scale, scale)

        GL11.glVertex3d(-scale, scale, scale)
        GL11.glVertex3d(-scale, scale, -scale)
        GL11.glEnd()

        GlStateManager.popMatrix()
    }
}
