package native.hotkey

import androidx.compose.ui.input.key.nativeKeyCode
import com.melloware.jintellitype.JIntellitype
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import data.KeyShortcut

class HotkeyAPI(val onHotkey: (shortcutID: Int) -> Unit) {
    private val intellitype: JIntellitype = JIntellitype.getInstance().also {
        it.addHotKeyListener(onHotkey)
    }
    fun registerHotkey(shortcut: KeyShortcut) {
        intellitype.registerHotKey(shortcut.hashCode(), shortcut.toIntellitypeFlag(), shortcut.key.nativeKeyCode)
    }

    fun unregisterHotkey(shortcut: KeyShortcut) {
        intellitype.unregisterHotKey(shortcut.hashCode())
    }

    fun unregisterAll() {
        intellitype.cleanUp()
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun KeyShortcut.toModifierFlag() = buildList {
    if (ctrl) add(WinUser.MOD_CONTROL)
    if (meta) add(WinUser.MOD_WIN)
    if (alt) add(WinUser.MOD_ALT)
    if (shift) add(WinUser.MOD_SHIFT)
}.fold(0) { acc, flag -> acc or flag }

@OptIn(ExperimentalStdlibApi::class)
private fun KeyShortcut.toIntellitypeFlag() = buildList {
    if (ctrl) add(JIntellitype.MOD_CONTROL)
    if (meta) add(JIntellitype.MOD_WIN)
    if (alt) add(JIntellitype.MOD_ALT)
    if (shift) add(JIntellitype.MOD_SHIFT)
}.fold(0) { acc, flag -> acc or flag }