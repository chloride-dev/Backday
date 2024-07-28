package cn.backday

import cn.backday.component.ComponentManager
import cn.backday.config.FileManager
import cn.backday.config.ModuleConfig
import cn.backday.manager.TargetManager
import cn.backday.module.ModuleManager
import cn.backday.utils.misc.GitUtils.gitInfo
import com.darkmagician6.eventapi.EventManager
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.Display

object Client {
    val clientName = "Backday"
    val clientCommit = gitInfo["git.commit.id.abbrev"] ?: "unknown"
    val clientBranch = gitInfo["git.branch"]?.toString() ?: "unknown"
    val playerId = Minecraft.getMinecraft().session.username
    val title = "$clientName (${clientCommit}/${clientBranch}) - $playerId"
    val isDev = true

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