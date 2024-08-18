package cn.backday.module.impl.render

import cn.backday.Client
import cn.backday.event.impl.render.Render2DEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.nio.FloatBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard

object Nametags : Module("Nametags", "Displays player nametags", ModuleCategory.Render, Keyboard.KEY_I) {
    //private val mc: Minecraft = Minecraft.getMinecraft()

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (mc.gameSettings.showDebugInfo) return

        for (entity in mc.theWorld.loadedEntityList) {
            if (entity is EntityPlayer) {
                renderNametag(entity)
            }
        }
    }

    private fun renderNametag(player: EntityPlayer) {
        val posX = player.prevPosX + (player.posX - player.prevPosX) * mc.timer.renderPartialTicks
        val posY = player.prevPosY + (player.posY - player.prevPosY) * mc.timer.renderPartialTicks
        val posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * mc.timer.renderPartialTicks

        val x = posX - mc.renderManager.viewerPosX
        val y = posY - mc.renderManager.viewerPosY
        val z = posZ - mc.renderManager.viewerPosZ

        val screenPos = projectToScreen(x, y + player.height + 0.5, z)

        if (screenPos != null) {
            val name = player.displayName.formattedText

            GlStateManager.pushMatrix()
            GlStateManager.translate(screenPos.x.toFloat(), screenPos.y.toFloat(), 0f)
            GlStateManager.scale(-0.5f, -0.5f, 0.5f)
            GlStateManager.disableDepth()
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            drawString(name, 0f, 0f, 0xFFFFFF) // ????

            GlStateManager.disableBlend()
            GlStateManager.enableDepth()
            GlStateManager.popMatrix()
        }
    }

    private fun projectToScreen(x: Double, y: Double, z: Double): Vec2? {
        val projectionMatrix = BufferUtils.createFloatBuffer(16)
        val modelViewMatrix = BufferUtils.createFloatBuffer(16)

        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix)
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix)

        val clipCoords = FloatArray(4)
        val ndcCoords = FloatArray(4)

        // ??????
        clipCoords[0] = (x * modelViewMatrix.get(0) + y * modelViewMatrix.get(4) + z * modelViewMatrix.get(8) + modelViewMatrix.get(12)).toFloat()
        clipCoords[1] = (x * modelViewMatrix.get(1) + y * modelViewMatrix.get(5) + z * modelViewMatrix.get(9) + modelViewMatrix.get(13)).toFloat()
        clipCoords[2] = (x * modelViewMatrix.get(2) + y * modelViewMatrix.get(6) + z * modelViewMatrix.get(10) + modelViewMatrix.get(14)).toFloat()
        clipCoords[3] = (x * modelViewMatrix.get(3) + y * modelViewMatrix.get(7) + z * modelViewMatrix.get(11) + modelViewMatrix.get(15)).toFloat()

        // ?????????
        if (clipCoords[3] != 0f) {
            ndcCoords[0] = clipCoords[0] / clipCoords[3]
            ndcCoords[1] = clipCoords[1] / clipCoords[3]
            ndcCoords[2] = clipCoords[2] / clipCoords[3]
            ndcCoords[3] = clipCoords[3] / clipCoords[3]
        } else {
            return null
        }

        // ???????
        val width = mc.displayWidth.toDouble()
        val height = mc.displayHeight.toDouble()

        val xPos = (width / 2.0 + width / 2.0 * ndcCoords[0]).toInt()
        val yPos = (height / 2.0 - height / 2.0 * ndcCoords[1]).toInt()

        return Vec2(xPos.toDouble(), yPos.toDouble())
    }

    private fun drawString(text: String, x: Float, y: Float, color: Int) {
        val fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRendererObj
        fontRenderer.drawStringWithShadow(text, x, y, color)
    }

    data class Vec2(val x: Double, val y: Double)
}
