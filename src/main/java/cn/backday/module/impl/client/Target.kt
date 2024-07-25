package cn.backday.module.impl.client

import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import cn.backday.value.impl.BoolValue

object Target : Module("Target", "choose target", ModuleCategory.Client) {
    val player = BoolValue("Players", true)
    val mobs = BoolValue("Mobs", false)
    val animals = BoolValue("Animals", false)
    val invisibles = BoolValue("Invisibles", false)
    val teams = BoolValue("Teams", false)

    override fun onEnable() {
        toggled = false
    }
}