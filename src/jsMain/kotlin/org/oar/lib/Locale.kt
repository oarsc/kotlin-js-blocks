package org.oar.lib

import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object Locale {
    private var currentLanguage = ""
    private var locale = mapOf<String, String>()

    fun loadLanguage(language: String, callback: () -> Unit) {
        val loadLanguage = if (language in AVAILABLE_LANGUAGES) language else "en"

        if (currentLanguage == loadLanguage) {
            callback()
            return
        }

        currentLanguage = loadLanguage
        window.fetch("locale.$loadLanguage.json")
            .then { it.text() }
            .then {
                locale = Json.decodeFromString<JsonObject>(it)
                    .mapValues { (_, value) ->
                        when (value) {
                            is JsonPrimitive -> value.content
                            else -> value.toString()
                        }
                    }
                callback()
            }
    }

    val String.translate get() = locale[this] ?: this

    private val AVAILABLE_LANGUAGES = setOf("es", "en")
}