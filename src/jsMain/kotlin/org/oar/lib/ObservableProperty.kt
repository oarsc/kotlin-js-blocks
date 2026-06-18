package org.oar.lib

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObservableProperty<S, T> private constructor(
    initial: T, private val equality: (T, T) -> Boolean = { a, b -> a == b },
    private val onChange: (thisRef: S, old: T, new: T) -> Unit
): ReadWriteProperty<S, T> {
    private var value: T = initial

    override operator fun getValue(thisRef: S, property: KProperty<*>): T = value

    override operator fun setValue(thisRef: S, property: KProperty<*>, value: T) {
        if (!equality(value, this.value)) {
            val old = this.value

            this.value = value

            onChange(thisRef, old, this.value)
        }
    }

    companion object {
        fun <S, T> observable(initial: T, onChange: S.(old: T, new: T) ->Unit): ReadWriteProperty<S, T> = ObservableProperty(initial) { thisRef, old, new ->
            onChange(thisRef, old, new)
        }
    }
}