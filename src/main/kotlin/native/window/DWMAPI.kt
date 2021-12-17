package native.window

import com.sun.jna.Library
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT

const val DWMWA_EXTENDED_FRAME_BOUNDS = 0x00000009L //from https://docs.microsoft.com/en-us/windows/win32/api/dwmapi/ne-dwmapi-dwmwindowattribute

internal interface DWMAPI : Library {
    fun DwmGetWindowAttribute(
        hWnd: WinDef.HWND,
        dwAttribute: WinDef.DWORD,
        pvAttribute: WinDef.PVOID,
        cbAttribute: WinDef.DWORD,
    ): WinNT.HRESULT
}