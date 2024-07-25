package cn.backday.value.impl

import cn.backday.value.Value
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

open class BoolValue(name: String, value: Boolean, displayable: () -> Boolean) :
    Value<Boolean>(name, value, displayable) {

    constructor(name: String, value: Boolean) : this(name, value, { true })

    override fun toJson(): JsonElement {
        return JsonPrimitive(value)
    }

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) {
            value = element.asBoolean || element.asString.equals("true", ignoreCase = true)
        }
    }
}
