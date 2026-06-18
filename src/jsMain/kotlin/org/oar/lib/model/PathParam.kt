package org.oar.lib.model

open class PathParam<T : Any>(
    val name: String,
    val default: T?,
    val fromStr: (String) -> T,
    val toStr: (T) -> String = Any::toString
) {
    val defaultStr: String? = default?.let(toStr)
}

class IntPathParam(
    name: String,
    default: Int? = null
): PathParam<Int>(
    name = name,
    default = default,
    fromStr = String::toInt,
    toStr = Int::toString
)

class StrPathParam(
    name: String,
    default: String? = null
): PathParam<String>(
    name = name,
    default = default,
    fromStr = { it },
    toStr = { it }
)