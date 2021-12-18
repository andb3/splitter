import androidx.compose.ui.geometry.Rect
import native.window.WindowAPI
import native.window.withAPI
import state.Action

class WindowManager {
    private val windowAPI = WindowAPI()
    fun handleAction(action: Action.MoveWindow) {
        when (action) {
            is Action.MoveWindow.Vertical -> moveVertically(action.startPercent, action.endPercent)
            is Action.MoveWindow.Horizontal -> moveHorizontally(action.startPercent, action.endPercent)
        }
    }

    private fun moveHorizontally(startPercent: Float, endPercent: Float) = withAPI(windowAPI) {
        val currentWindow = getForegroundWindow()
        val currentWindowBounds = currentWindow.getBounds()
        val currentMonitorBounds = currentWindow.getCurrentMonitor().getBounds()

        val newLeft = currentMonitorBounds.horizontalPercent(startPercent)
        val newWidth = currentMonitorBounds.horizontalPercent(endPercent) - newLeft
        currentWindow.move(
            x = newLeft.toInt(),
            y = currentWindowBounds.top.toInt(),
            width = newWidth.toInt(),
            height = currentWindowBounds.height.toInt()
        )
    }

    private fun moveVertically(startPercent: Float, endPercent: Float) = withAPI(windowAPI) {
        val currentWindow = getForegroundWindow()
        val currentWindowBounds = currentWindow.getBounds()
        val currentMonitorBounds = currentWindow.getCurrentMonitor().getBounds()

        val newTop = currentMonitorBounds.verticalPercent(startPercent)
        val newHeight = currentMonitorBounds.verticalPercent(endPercent) - newTop
        currentWindow.move(
            x = currentWindowBounds.left.toInt(),
            y = newTop.toInt(),
            width = currentWindowBounds.width.toInt(),
            height = newHeight.toInt()
        )
    }
}


fun Rect.verticalPercent(percent: Float): Float = top + (height * percent)
fun Rect.horizontalPercent(percent: Float): Float = left + (width * percent)