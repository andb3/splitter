
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import native.hotkey.HotkeyAPI
import state.EditAction
import state.SplitterMachine
import ui.App
import java.awt.*
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess


fun main() {
    if (!SystemTray.isSupported()) return

    val splitterMachine = SplitterMachine()
    val windowManager = WindowManager()
    val hotkeyAPI = HotkeyAPI { code ->
        println("code = $code, allZones = ${splitterMachine.allZones()}")
        val zone = splitterMachine.allZones().first {
            when(val hotkey = it.hotkey) {
                is Hotkey.Shortcut -> hotkey.toKeyShortcut().hashCode() == code
                else -> false
            }
        }
        windowManager.handleAction(zone.moveAction())
    }

    SystemTray.getSystemTray().add(createTrayIcon(
        onLaunchSettings = { CoroutineScope(Dispatchers.Swing).launch {
            launchSettings(splitterMachine.zones) { action ->
                //splitterMachine.zones = splitterMachine.zones.value.find { action. }
            }
        } },
        onExit = {
            hotkeyAPI.unregisterAll()
            exitProcess(1)
        }
    ))

    splitterMachine.allZones().forEach { zone ->
        when(val hotkey = zone.hotkey) {
            Hotkey.Recording, Hotkey.Unset -> return@forEach
            is Hotkey.Shortcut -> {
                hotkeyAPI.registerHotkey(hotkey.toKeyShortcut())
            }
        }
    }
}

suspend fun launchSettings(zones: StateFlow<List<ZoneGroup>>, onEditAction: (EditAction) -> Unit) = awaitApplication {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Splitter",
        icon = painterResource("SplitterIcon.png")
    ) {
        val zoneGroups = zones.collectAsState()
        App(zoneGroups.value, onEditAction)
    }
}

fun createTrayIcon(onLaunchSettings: () -> Unit, onExit : () -> Unit) = TrayIcon(
    ImageIO.read(File("src/main/resources/SplitterTrayIcon.jpg")),
    "Splitter",
    PopupMenu().apply {
        add(MenuItem("Exit").apply {
            addActionListener { onExit.invoke() }
        })
        add(MenuItem("Settings").apply {
            addActionListener { onLaunchSettings.invoke() }
        })
    }
).apply {
    addActionListener { onLaunchSettings.invoke() }
}