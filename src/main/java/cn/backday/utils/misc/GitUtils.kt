package cn.backday.utils.misc

import cn.backday.Client
import java.util.*

object GitUtils {
    val gitInfo = Properties().also {
        val inputStream = Client::class.java.classLoader.getResourceAsStream("git.properties")

        if (inputStream != null) {
            it.load(inputStream)
        } else {
            it["git.build.version"] = "unofficial"
        }
    }
}