package native

import com.sun.jna.Library
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinUser



internal interface WindowNativeAPI : Library {
    fun GetForegroundWindow(): WinDef.HWND

    fun MoveWindow(
        hWnd: WinDef.HWND,
        X: Int,
        Y: Int,
        nWidth: Int,
        nHeight: Int,
        bRepaint: Boolean
    ): Boolean

    fun GetWindowRect(
        hWnd: WinDef.HWND,
        lpRect: WinDef.RECT
    ): Boolean

    fun GetWindowRectangle(
        hWnd: WinDef.HWND,
        lpRect: WinDef.RECT
    ): Boolean

    fun MonitorFromWindow(
        hwnd: WinDef.HWND,
        dwFlags: Int
    ): WinUser.HMONITOR

    fun GetMonitorInfoA(
        hMonitor: WinUser.HMONITOR,
        lpmi: WinUser.MONITORINFO
    ): Boolean
}