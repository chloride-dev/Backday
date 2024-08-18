package cn.backday.component.impl

import cn.backday.Client
import cn.backday.component.Component
import cn.backday.event.impl.key.KeyEvent
import com.darkmagician6.eventapi.EventTarget

class KeybindComponent : Component() {
    @EventTarget
    fun onKey(key: KeyEvent) {
        if (mc.currentScreen != null) {
            return
        }

        if (Client.moduleManager.keyBinds.containsKey(key.key)) {
            for ((k, v) in Client.moduleManager.keyBinds.entries()) {
                if (k == key.key) {
                    v.toggle()
                }
            }
        }
    }
}