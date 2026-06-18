package org.oar.lib

import kotlinx.browser.window
import org.oar.lib.model.PathParam

object HashController {
    private val hashData: HashData = HashData()
    private lateinit var format: List<PathParam<*>>
    private lateinit var onPageChange: (Map<String, String>) -> Unit

    val path: MutableMap<String, String> get() = hashData.path
    val params: MutableMap<String, String> get() = hashData.params

    fun init(format: List<PathParam<*>>, onPageChange: (Map<String, String>) -> Unit) {
        this.format = format
        this.onPageChange = onPageChange
        sync()

        window.addEventListener("hashchange", {
            sync()
            onPageChange(hashData.path)
        })
    }

    fun sync() {
        hashData.path.clear()
        hashData.params.clear()

        val hash = window.location.hash.takeIf(String::isNotBlank)
            ?: run {
                format.filter { it.default != null }.forEach {
                    hashData.path[it.name] = it.defaultStr!!
                }
                return
            }

        val hashParts = hash.trimStart('#').split("?")
        val path = hashParts.firstOrNull()?.split("/") ?: emptyList()

        format.forEachIndexed { idx, it ->
            val urlParam = path.getOrNull(idx) ?: it.defaultStr
            if (urlParam != null) {
                hashData.path[it.name] = urlParam
            }
        }

        val params = hashParts.getOrNull(1)
            ?.split("&")
            ?.associate {
                val pair = it.split("=")
                pair.first() to pair.drop(1).joinToString("=").let(::decodeURIComponent)
            }
            .orEmpty().toMutableMap()

        hashData.params.putAll(params)
    }

    fun updateUrl(redirect: Boolean = true, pushHistory: Boolean = false) {
        val path = buildList {
            for (it in format) {
                val value = hashData.path[it.name] ?: break
                add(value)
            }
        }.joinToString("/")

        val params = hashData.params.entries.joinToString("&") {
            "${it.key}=${encodeURIComponent(it.value)}"
        }

        val hash =
            if (params.isNotBlank()) {
                "$path?$params"
            } else {
                path
            }
                .takeIf(String::isNotBlank)
                ?: return

        val baseUrl = window.location.href.replace(window.location.search, "").replace(window.location.hash, "")
        if (pushHistory) {
            window.history.pushState("", "", "$baseUrl#$hash")
        } else {
            window.history.replaceState("", "", "$baseUrl#$hash")
        }

        if (redirect) {
            onPageChange(hashData.path)
        }
    }

    class HashData internal constructor(
        val path: MutableMap<String, String> = mutableMapOf(),
        val params: MutableMap<String, String> = mutableMapOf()
    )
}

private external fun decodeURIComponent(encodedURI: String): String
private external fun encodeURIComponent(decodedURI: String): String