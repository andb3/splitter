package native.hotkey

import com.sun.jna.Library
import com.sun.jna.platform.win32.WinDef.*


internal interface HotkeyNativeAPI : Library {
    fun RegisterHotKey(
        hWnd: HWND?,
        id: Int,
        fsModifiers: UINT?,
        vk: UINT?
    ): Boolean

    fun UnregisterHotKey(
        hWnd: HWND?,
        id: Int
    ): Boolean
}