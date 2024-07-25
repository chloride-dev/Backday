package cn.backday.value.impl

import cn.backday.value.Value
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

open class IntValue(
    name: String, value: Int, val minimum: Int, val maximum: Int, suffix: String, displayable: () -> Boolean
) : Value<Int>(name, value, displayable) {
    val suffix: String

    init {
        this.suffix = suffix
    }

    constructor(name: String, value: Int, minimum: Int, maximum: Int, suffix: String) : this(name,
        value,
        minimum,
        maximum,
        suffix,
        { true })

    constructor(name: String, value: Int, minimum: Int, maximum: Int, displayable: () -> Boolean) : this(
        name, value, minimum, maximum, "", displayable
    )

    constructor(name: String, value: Int, minimum: Int, maximum: Int) : this(name,
        value,
        minimum,
        maximum,
        "",
        { true })

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    override fun toJson(): JsonElement {
        return JsonPrimitive(value)
    }

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) {
            value = element.asInt
        }
    }
}