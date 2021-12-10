package data

import androidx.compose.ui.input.key.Key

/**
 * Represents a key combination which should be pressed on a keyboard to trigger some action.
 */
class KeyShortcut(
    /**
     * Key that should be pressed to trigger an action
     */
    val key: Key,

    /**
     * true if Ctrl modifier key should be pressed to trigger an action
     */
    val ctrl: Boolean = false,

    /**
     * true if Meta modifier key should be pressed to trigger an action
     * (it is Command on macOs)
     */
    val meta: Boolean = false,

    /**
     * true if Alt modifier key should be pressed to trigger an action
     */
    val alt: Boolean = false,

    /**
     * true if Shift modifier key should be pressed to trigger an action
     */
    val shift: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeyShortcut

        if (key != other.key) return false
        if (ctrl != other.ctrl) return false
        if (meta != other.meta) return false
        if (alt != other.alt) return false
        if (shift != other.shift) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + ctrl.hashCode()
        result = 31 * result + meta.hashCode()
        result = 31 * result + alt.hashCode()
        result = 31 * result + shift.hashCode()
        return result
    }

    override fun toString() = buildString {
        if (ctrl) append("Ctrl+")
        if (meta) append("Meta+")
        if (alt) append("Alt+")
        if (shift) append("Shift+")
        append(key)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun KeyShortcut.modifiersToString() = buildList {
    if (ctrl) add("Ctrl")
    if (meta) add("Meta")
    if (alt) add("Alt")
    if (shift) add("Shift")
}.joinToString(" + ")
