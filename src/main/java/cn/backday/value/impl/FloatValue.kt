package cn.backday.value.impl

import cn.backday.value.Value
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

open class FloatValue(
    name: String, value: Float, minimum: Float, maximum: Float, suffix: String, displayable: () -> Boolean
) : Value<Float>(name, value, displayable) {
    val minimum: Float = minimum
    val maximum: Float = maximum
    val suffix: String = suffix

    constructor(name: String, value: Float, minimum: Float, maximum: Float, displayable: () -> Boolean) : this(
        name, value, minimum, maximum, "", displayable
    )

    constructor(name: String, value: Float, minimum: Float, maximum: Float, suffix: String) : this(name,
        value,
        minimum,
        maximum,
        suffix,
        { true })

    constructor(name: String, value: Float, minimum: Float, maximum: Float) : this(name,
        value,
        minimum,
        maximum,
        "",
        { true })

    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJson(): JsonElement {
        return JsonPrimitive(value)
    }

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) {
            value = element.asFloat
        }
    }
}
