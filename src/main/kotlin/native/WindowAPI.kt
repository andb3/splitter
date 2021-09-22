package native

import androidx.compose.ui.geometry.Rect
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.MONITOR_DEFAULTTONEAREST
import com.sun.jna.platform.win32.WinUser.MONITOR_DEFAULTTOPRIMARY

data class Window(internal val pointer: WinDef.HWND)
data class Monitor(internal val pointer: WinUser.HMONITOR)
enum class MonitorChoicePreference { Nearest, Primary }

private const val InvisibleHorizontalBorderWidth = 7

class WindowAPI {
    private val windowNativeApi: WindowNativeAPI = Native.load("user32", WindowNativeAPI::class.java)
    fun getForegroundWindow(): Window = Window(windowNativeApi.GetForegroundWindow())
    fun Window.move(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        repaint: Boolean = true,
    ) {
        windowNativeApi.MoveWindow(this.pointer, x - InvisibleHorizontalBorderWidth, y, width + (InvisibleHorizontalBorderWidth * 2), height, repaint)
    }

    fun Window.getBounds() : Rect {
        val mutableRect = WinDef.RECT()
        val succeeded = windowNativeApi.GetWindowRect(this.pointer, mutableRect)
        return mutableRect.toRect().let {
            it.copy(left = it.left + InvisibleHorizontalBorderWidth, right = it.right - InvisibleHorizontalBorderWidth)
        }
    }

    fun Window.getCurrentMonitor(
        choicePreference: MonitorChoicePreference = MonitorChoicePreference.Nearest
    ): Monitor = Monitor(windowNativeApi.MonitorFromWindow(this.pointer, when(choicePreference) {
        MonitorChoicePreference.Nearest -> MONITOR_DEFAULTTONEAREST
        MonitorChoicePreference.Primary -> MONITOR_DEFAULTTOPRIMARY
    }))

    fun Monitor.getBounds() : Rect {
        val mutableMonitorInfo = WinUser.MONITORINFO()
        val succeeded = windowNativeApi.GetMonitorInfoA(this.pointer, mutableMonitorInfo)
        return mutableMonitorInfo.rcMonitor.toRect()
    }
}

fun withAPI(api: WindowAPI, block: WindowAPI.() -> Unit) {
    api.apply(block)
}

private fun WinDef.RECT.toRect(): Rect = Rect(this.left.toFloat(), this.top.toFloat(), this.right.toFloat(), this.bottom.toFloat())