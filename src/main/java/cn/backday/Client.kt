package cn.backday

import cn.backday.component.ComponentManager
import cn.backday.config.FileManager
import cn.backday.config.ModuleConfig
import cn.backday.module.ModuleManager
import cn.backday.utils.log.Logger
import cn.backday.utils.misc.GitUtils.gitInfo
import com.darkmagician6.eventapi.EventManager
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.DefaultResourcePack
import org.lwjgl.opengl.Display

object Client {
    val clientName = "Backday"
    val clientCommit = gitInfo["git.commit.id.abbrev"] ?: "unknown"
    val clientBranch = gitInfo["git.branch"]?.toString() ?: "unknown"
    val playerId = Minecraft.getMinecraft().session.username
    val title = "$clientName (${clientCommit}/${clientBranch}) - $playerId"
    val isDev = false

    // Manager
    val moduleManager = ModuleManager()
    val fileManager = FileManager()
    val componentManager = ComponentManager()
    val moduleConfig = ModuleConfig()

    fun startClient() {

        // init something
        componentManager.init()

        EventManager.register(this)

        moduleManager.registerModules()
        moduleConfig.load(fileManager.getbConfigFile())
        moduleManager.eventRegister()
        Display.setTitle(title)
        Logger.log("Welcome to $clientName (${clientCommit}/${clientBranch})")
        //Display.setIcon()
    }

    fun stopClient() {
        EventManager.unregister(this)
        Logger.log("Shutting down $clientName")
        moduleConfig.save(fileManager.getbConfigFile())
    }
}