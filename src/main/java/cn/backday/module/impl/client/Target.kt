package cn.backday.module.impl.client

import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.value.impl.BoolValue

object Target : Module("Target", "choose target", ModuleCategory.Client) {
    val player = BoolValue("Players", true)
    val mobs = BoolValue("Mobs", true)
    val animals = BoolValue("Animals", true)
    val invisibles = BoolValue("Invisibles", false)
    val deaths = BoolValue("Deaths", false)
    val teams = BoolValue("Teams", true)


    override fun onEnable() {
        toggled = false
    }
}