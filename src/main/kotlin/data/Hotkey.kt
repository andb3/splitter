package data

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut

sealed class Hotkey {
    object Unset : Hotkey()
    object Recording : Hotkey()
    data class Shortcut(val modifiers: HotkeyModifiers, val key: Key) : Hotkey()
}

sealed class HotkeyModifiers {
    object Default : HotkeyModifiers()
    data class Custom(val modifiers: Modifiers) : HotkeyModifiers()
}

class Modifiers(
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

        other as Modifiers

        if (ctrl != other.ctrl) return false
        if (meta != other.meta) return false
        if (alt != other.alt) return false
        if (shift != other.shift) return false

        return true
    }


    @OptIn(ExperimentalStdlibApi::class)
    override fun toString() = buildList {
        if (ctrl) add("Ctrl")
        if (meta) add("Meta")
        if (alt) add("Alt")
        if (shift) add("Shift")
    }.joinToString(" + ")
}

fun Hotkey.Shortcut.toKeyShortcut() = when(this.modifiers) {
    is HotkeyModifiers.Custom -> data.KeyShortcut(
        key = this.key,
        ctrl = this.modifiers.modifiers.ctrl,
        meta = this.modifiers.modifiers.meta,
        alt = this.modifiers.modifiers.alt,
        shift = this.modifiers.modifiers.shift,
    )
    is HotkeyModifiers.Default -> data.KeyShortcut(
        key = this.key,
        ctrl = DefaultModifiers.modifiers.ctrl,
        meta = DefaultModifiers.modifiers.meta,
        alt = DefaultModifiers.modifiers.alt,
        shift = DefaultModifiers.modifiers.shift,
    )
}