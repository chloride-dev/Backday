package cn.backday

import cn.backday.component.ComponentManager
import cn.backday.config.FileManager
import cn.backday.config.ModuleConfig
import cn.backday.manager.TargetManager
import cn.backday.module.ModuleManager
import com.mojang.authlib.*
import com.darkmagician6.eventapi.EventManager
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.Display
import sun.audio.AudioPlayer.player

object Client {
    val clientName = "Backday"
    val clientVersion = "cd25ec54"
   // val title = "$clientName ${clientVersion}"
   val playerId = Minecraft.getMinecraft().session.username
   val title = "$clientName $clientVersion - $playerId"
    val isDev = false
    // Manager
    val moduleManager = ModuleManager()
    val fileManager = FileManager()
    val targetManager = TargetManager()
    val componentManager = ComponentManager()

    val moduleConfig = ModuleConfig()

    fun startClient() {
        Display.setTitle(title)

        // init something
        targetManager.init()
        componentManager.init()

        EventManager.register(this)

        moduleManager.registerModules()
        moduleConfig.load(fileManager.getbConfigFile())
        moduleManager.eventRegister()
    }

    fun stopClient() {
        EventManager.unregister(this)

        moduleConfig.save(fileManager.getbConfigFile())
    }
}