package cn.backday.value

import cn.backday.utils.log.Logger
import com.google.gson.JsonElement

abstract class Value<T> {
    val name: String
    var value: T
    private val displayable: () -> Boolean
    private val defaultVal: T

    constructor(name: String, value: T, displayable: () -> Boolean) {
        this.name = name
        this.value = value
        this.displayable = displayable
        this.defaultVal = value
    }

    abstract fun toJson(): JsonElement

    abstract fun fromJson(element: JsonElement)

    fun get(): T {
        return value
    }

    fun set(newValue: T) {
        if (newValue == value) {
            return
        }

        val oldValue = get()

        try {
            onChange(oldValue, newValue)
            changeValue(newValue)
            onChanged(oldValue, newValue)
            onChanging()
            // Save
        } catch (e: Exception) {
            Logger.error("[ValueSystem ($name)]: ${e.javaClass.name} (${e.message}) [$oldValue >> $newValue]")
        }
    }

    fun setDefault() {
        value = defaultVal
    }

    open fun onChange(oldValue: T, newValue: T) {
    }

    open fun onChanged(oldValue: T, newValue: T) {
    }

    open fun onChanging() {
    }

    open fun changeValue(value: T) {
        this.value = value
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (value is String && other is String) {
            return (value as String).equals(other, true)
        }
        return value != null && value == other
    }

    fun contains(text: String): Boolean {
        return if (value is String) {
            (value as String).contains(text)
        } else {
            false
        }
    }

    fun isDisplayable(): Boolean {
        return displayable()
    }
}