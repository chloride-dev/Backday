package cn.backday

import cn.backday.component.ComponentManager
import cn.backday.config.FileManager
import cn.backday.config.ModuleConfig
import cn.backday.manager.TargetManager
import cn.backday.module.ModuleManager
import com.darkmagician6.eventapi.EventManager
import org.lwjgl.opengl.Display

object Client {
    val clientName = "Backday"
    val clientVersion = "1.0.0"
    val title = "$clientName v${clientVersion} | 让世界回到那一天"
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