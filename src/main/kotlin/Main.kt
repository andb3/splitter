
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import native.hotkey.HotkeyAPI
import state.Action
import state.EditGroupAction
import state.SplitterMachine
import state.UserAction
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
                when(action) {
                    is Action.MoveWindow -> windowManager.handleAction(action)
                    is EditGroupAction -> splitterMachine.zones.value = splitterMachine.zones.value.handleEditAction(action)
                }
            }
        } },
        onExit = {
            hotkeyAPI.unregisterAll()
            exitProcess(1)
        }
    ))

    runBlocking {
        splitterMachine.zones.fold(emptyList<ZoneGroup>()) { oldGroups, newGroups ->
            val toRemove = oldGroups.flatMap { it.zones } - newGroups.flatMap { it.zones }
            val toAdd = newGroups.flatMap { it.zones } - oldGroups.flatMap { it.zones }
            println("adding: $toAdd, removing: $toRemove")
            toRemove
                .filter { it.hotkey is Hotkey.Shortcut }
                .forEach { hotkeyAPI.unregisterHotkey((it.hotkey as Hotkey.Shortcut).toKeyShortcut()) }
            toAdd
                .filter { it.hotkey is Hotkey.Shortcut }
                .forEach { hotkeyAPI.registerHotkey((it.hotkey as Hotkey.Shortcut).toKeyShortcut()) }
            newGroups
        }
        Unit
    }
}

suspend fun launchSettings(zones: StateFlow<List<ZoneGroup>>, onAction: (Action) -> Unit) = awaitApplication {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Splitter",
        icon = painterResource("SplitterIcon.png")
    ) {
        val zoneGroups = zones.collectAsState()
        App(zoneGroups.value, onAction)
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

fun List<ZoneGroup>.handleEditAction(action: EditGroupAction): List<ZoneGroup> {
    val newGroup = when(action) {
        is EditGroupAction.Zone.UpdateHotkey -> when {
            this.flatMap { it.zones }.any { it.hotkey == action.hotkey } -> action.group
            else -> action.group.applyAction(action)
        }
        else -> action.group.applyAction(action)
    }
    return this.map { if (it == action.group) newGroup else it }
}