package org.oar.lib.extensions

import org.w3c.dom.EventInit
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

object EventExtension {
    @Suppress("UNCHECKED_CAST")
    fun HTMLElement.onClick(callback: (MouseEvent) -> Unit) =
        addEventListener("click", callback as (Event) -> Unit)

    @Suppress("UNCHECKED_CAST")
    fun HTMLElement.offClick(callback: (MouseEvent) -> Unit) =
        removeEventListener("click", callback as (Event) -> Unit)

    fun HTMLElement.onChange(callback: (Event) -> Unit) =
        addEventListener("change", callback)

    fun HTMLElement.offChange(callback: (Event) -> Unit) =
        removeEventListener("change", callback)

    fun HTMLElement.dispatchChange() =
        dispatchEvent(Event("change", EventInit(bubbles = true)))
}