package native.window

import androidx.compose.runtime.snapshots.SnapshotApplyResult.Success.succeeded
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.key.Key.Companion.X
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.MONITOR_DEFAULTTONEAREST
import com.sun.jna.platform.win32.WinUser.MONITOR_DEFAULTTOPRIMARY
import kotlin.math.absoluteValue

data class Window(internal val pointer: WinDef.HWND)
data class Monitor(internal val pointer: WinUser.HMONITOR)
enum class MonitorChoicePreference { Nearest, Primary }

private const val InvisibleBorderWidth = 7

class WindowAPI {
    private val windowNativeApi: WindowNativeAPI = Native.load("user32", WindowNativeAPI::class.java)
    private val dwmapi: DWMAPI = Native.load("dwmapi", DWMAPI::class.java)
    fun getForegroundWindow(): Window = Window(windowNativeApi.GetForegroundWindow())
    fun Window.move(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        repaint: Boolean = true,
    ) {
        val invisibleBorder = this.getInvisibleBorder().absoluteValue
        windowNativeApi.MoveWindow(
            hWnd = this.pointer,
            X = x - invisibleBorder.left.toInt(),
            Y = y - invisibleBorder.top.toInt(),
            nWidth = width + invisibleBorder.left.toInt() + invisibleBorder.right.toInt(),
            nHeight = height + invisibleBorder.top.toInt() + invisibleBorder.bottom.toInt(),
            bRepaint = repaint)
    }

    fun Window.getBounds(): Rect {
        val mutableRect = WinDef.RECT()
        val succeeded = windowNativeApi.GetWindowRect(this.pointer, mutableRect)
        val invisibleBorder = this.getInvisibleBorder().absoluteValue
        return mutableRect.toRect().let {
            it.copy(
                left = it.left + invisibleBorder.left,
                top = it.top + invisibleBorder.top,
                right = it.right - invisibleBorder.right,
                bottom = it.bottom - invisibleBorder.bottom,
            )
        }
    }

    fun Window.getCurrentMonitor(
        choicePreference: MonitorChoicePreference = MonitorChoicePreference.Nearest
    ): Monitor = Monitor(windowNativeApi.MonitorFromWindow(this.pointer, when (choicePreference) {
        MonitorChoicePreference.Nearest -> MONITOR_DEFAULTTONEAREST
        MonitorChoicePreference.Primary -> MONITOR_DEFAULTTOPRIMARY
    }))

    fun Window.getInvisibleBorder(): Rect {
        val excludeShadow = WinDef.RECT()
        val includeShadow = WinDef.RECT()

        excludeShadow.write()
        val succeeded = dwmapi.DwmGetWindowAttribute(
            hWnd = this.pointer,
            dwAttribute = WinDef.DWORD(DWMWA_EXTENDED_FRAME_BOUNDS),
            pvAttribute = WinDef.PVOID(excludeShadow.pointer),
            cbAttribute = WinDef.DWORD(excludeShadow.size().toLong())
        )
        excludeShadow.read()
        println("dwmapi succeeded = ${succeeded.toLong().toULong().toString(16)}")

        windowNativeApi.GetWindowRect(this.pointer, includeShadow)

        println("excludeShadow (inner) = ${excludeShadow}")
        println("includeShadow (outer) = ${includeShadow}")

        val border = excludeShadow.toRect() - includeShadow.toRect()
        println("border = $border")
        return border
    }

    fun Monitor.getBounds(): Rect {
        val mutableMonitorInfo = WinUser.MONITORINFO()
        val succeeded = windowNativeApi.GetMonitorInfoA(this.pointer, mutableMonitorInfo)
        return mutableMonitorInfo.rcWork.toRect()
    }
}

fun withAPI(api: WindowAPI, block: WindowAPI.() -> Unit) {
    api.apply(block)
}

private fun WinDef.RECT.toRect(): Rect = Rect(
    left = this.left.toFloat(),
    top = this.top.toFloat(),
    right = this.right.toFloat(),
    bottom = this.bottom.toFloat(),
)

operator fun Rect.minus(other: Rect): Rect = Rect(
    left = this.left - other.left,
    top = this.top - other.top,
    right = this.right - other.right,
    bottom = this.bottom - other.bottom,
)

val Rect.absoluteValue get() = Rect(
    left = this.left.absoluteValue,
    top = this.top.absoluteValue,
    right = this.right.absoluteValue,
    bottom = this.bottom.absoluteValue,
)