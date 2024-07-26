package cn.backday.module

import cn.backday.Client
import cn.backday.ui.font.FontUtil
import cn.backday.value.Value
import com.darkmagician6.eventapi.EventManager
import net.minecraft.client.Minecraft

open class Module {
    val mc: Minecraft = Minecraft.getMinecraft()
    var moduleName: String
    var description: String
    var moduleCategory: ModuleCategory
    var keybind = 0
    val font = FontUtil()

    @JvmField
    var toggled = false

    constructor(moduleName: String, description: String, moduleCategory: ModuleCategory) {
        this.moduleName = moduleName
        this.description = description
        this.moduleCategory = moduleCategory
        this.toggled = false
    }

    constructor(moduleName: String, description: String, moduleCategory: ModuleCategory, keybind: Int) {
        this.moduleName = moduleName
        this.description = description
        this.moduleCategory = moduleCategory
        this.toggled = false
        this.keybind = keybind
    }

    constructor(moduleName: String, description: String, moduleCategory: ModuleCategory, toggled: Boolean) {
        this.moduleName = moduleName
        this.description = description
        this.moduleCategory = moduleCategory
        this.toggled = toggled
    }

    constructor(
        moduleName: String,
        description: String,
        moduleCategory: ModuleCategory,
        keybind: Int,
        toggled: Boolean
    ) {
        this.moduleName = moduleName
        this.description = description
        this.moduleCategory = moduleCategory
        this.toggled = toggled
        this.keybind = keybind
    }

    open fun onInitialize() {}

    open fun onEnable() {}

    open fun onDisable() {}

    fun toggle() {
        try {
            if (toggled) {
                toggled = false
                EventManager.unregister(this)
                onDisable()
            } else {
                toggled = true
                EventManager.register(this)
                onEnable()
            }
            mc.thePlayer.playSound("random.click", 0.5F, 1F)
        } catch (exception: Exception) {
            // 如果客户端处于开发模式，则打印异常堆栈信息
            if (Client.isDev) exception.printStackTrace()
        }
    }

    fun getValues(): List<Value<*>> {
        val values = mutableListOf<Value<*>>()
        val fields = javaClass.declaredFields
        for (field in fields) {
            if (Value::class.java.isAssignableFrom(field.type)) {
                try {
                    field.isAccessible = true
                    val value = field.get(this) as Value<*>
                    values.add(value)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
        return values
    }

    fun getValueByName(name: String): Value<*>? {
        return getValues().stream()
            .filter { value -> value.name == name }
            .findFirst()
            .orElse(null)
    }

    fun getToggled(): Boolean {
        return this.toggled
    }
}