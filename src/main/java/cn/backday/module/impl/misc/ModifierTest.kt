package cn.backday.module.impl.misc

import cn.backday.event.impl.client.ChatReceivedEvent
import cn.backday.event.impl.client.TabListUpdateEvent
import cn.backday.event.impl.client.ScoreboardUpdateEvent
import cn.backday.module.Module
import cn.backday.module.ModuleCategory
import com.darkmagician6.eventapi.EventTarget

object ModifierTest : Module("Modifier", "Modifies specific words and links in chat, TAB lists, and scoreboards", ModuleCategory.Misc,true) {
    private val linkReplacements = mapOf(
        "mc.163mc.cn" to "quickmarco.com",
        "KKCraft" to "quickmarco.com",
        "DCJ" to "quickmarco.com",
        "www.hypixel.net" to "quickmarco.com",
        "mc.hypixel.net" to "quickmarco.com",
        "BlocksMC.com" to "quickmarco.com"
    )

    private val wordReplacements = mapOf(
        "KKCraft" to "Backday",
        "Hypixel" to "Backday" ,
        "DCJ" to "Backday",

        "BlocksMC" to "Backday"
    )

    @EventTarget
    fun onChatReceived(event: ChatReceivedEvent) {
        event.message = replaceText(event.message)
    }

    @EventTarget
    fun onTabListUpdate(event: TabListUpdateEvent) {
        event.tabListEntries = event.tabListEntries.map { replaceText(it) }
    }

    @EventTarget
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        event.scoreboardLines = event.scoreboardLines.map { replaceText(it) }
    }

    private fun replaceText(text: String): String {
        var newText = text

        // Replace links
        for ((key, value) in linkReplacements) {
            newText = newText.replace(key, value)
        }

        // Replace single words
        val words = newText.split(" ").toMutableList()
        for (i in words.indices) {
            val word = words[i]
            if (wordReplacements.containsKey(word)) {
                words[i] = wordReplacements[word] ?: word
            }
        }
        newText = words.joinToString(" ")

        return newText
    }
}
