package cn.backday.module

import cn.backday.module.impl.client.Target
import cn.backday.module.impl.combat.AimAssist
import cn.backday.module.impl.movement.Sprint
import cn.backday.module.impl.render.ArrayListMod
import cn.backday.module.impl.render.WaterMark
import cn.backday.utils.log.Logger
import com.darkmagician6.eventapi.EventManager
import org.apache.commons.collections4.MultiValuedMap
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap
import java.util.stream.Collectors

class ModuleManager {
    var keyBinds: MultiValuedMap<Int, Module> = ArrayListValuedHashMap()
    private val modules: ArrayList<Module> = ArrayList()
    private val moduleList: ArrayList<Module> = ArrayList()

    fun registerModules() {
        // Client
        moduleList.add(Target)

        // Combat
        moduleList.add(AimAssist)

        // Movement
        moduleList.add(Sprint)

        //Render
        moduleList.add(WaterMark)
        moduleList.add(ArrayListMod)

        // 根据moduleList中的模块列表，按照模块名称长度、模块名称首字母的Unicode编码、模块类别顺序进行排序，然后将排序后的列表注册到模块中
        registerModuleByList(
            moduleList.stream().sorted(Comparator.comparingInt { module -> module.moduleName.length })
                .sorted(Comparator.comparingInt { s -> s.moduleName[0].code })
                .sorted(Comparator.comparingInt { module -> module.moduleCategory.ordinal }).collect(
                    Collectors.toList()
                )
        )
    }

    fun registerModule(module: Module) {
        modules.add(module)
        if (module.keybind != 0) {
            keyBinds.put(module.keybind, module)
        }

        Logger.log("Initialize module: " + module.moduleName)
        module.onInitialize()
        Logger.log("Finished initialize module: " + module.moduleName)
    }

    fun registerModuleByList(moduleList: List<Module>) {
        for (module in moduleList) {
            registerModule(module)
        }
    }

    fun getModule(name: String): Module? {
        for (i in modules) {
            if (i.moduleName.equals(name, ignoreCase = true)) {
                return i
            }
        }
        return null
    }

    fun getModules(): ArrayList<Module> {
        return modules
    }

    fun setKeybind(module: Module, key: Int) {
        var hasIt = false
        val newMap: MultiValuedMap<Int, Module> = ArrayListValuedHashMap()
        for ((key, value) in keyBinds.entries()) {
            if (value == module) {
                newMap.put(key, value)
                hasIt = true
            } else {
                newMap.put(key, value)
            }
        }
        if (!hasIt) {
            newMap.put(key, module)
        }
        module.keybind = key
        keyBinds = newMap
    }

    fun eventRegister() {
        for (module in this.getModules()) {
            if (module.getToggled()) {
                EventManager.register(module)
                Logger.log("Event register module: " + module.moduleName)
            } else {
                EventManager.unregister(module)
            }
        }
    }
}