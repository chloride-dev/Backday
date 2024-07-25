package cn.backday.value.impl

import cn.backday.value.Value
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

open class ListValue(name: String, values: Array<String>, value: String, displayable: () -> Boolean) :
    Value<String>(name, value, displayable) {
    var values: Array<String> = values
    var openList: Boolean = false

    constructor(name: String, values: Array<String>, value: String) : this(name, values, value, { true })

    fun containsValue(string: String): Boolean {
        return values.any { it.equals(string, ignoreCase = true) }
    }

    override fun changeValue(value: String) {
        for (element in values) {
            if (element.equals(value, ignoreCase = true)) {
                this.value = element
                break
            }
        }
    }

    override fun toJson(): JsonElement {
        return JsonPrimitive(value)
    }

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) {
            changeValue(element.asString)
        }
    }

    fun isMode(string: String): Boolean {
        return value.equals(string, ignoreCase = true)
    }

    fun indexOf(mode: String): Int {
        for (i in values.indices) {
            if (values[i].equals(mode, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    fun listContains(string: String): Boolean {
        return values.any { it.equals(string, ignoreCase = true) }
    }

    fun getModeListNumber(mode: String): Int {
        return values.indexOf(mode)
    }
}
