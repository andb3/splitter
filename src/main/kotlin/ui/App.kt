package ui

import WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DefaultModifiers
import data.ZoneGroup
import data.horizontalGroups
import data.verticalGroups
import state.EditAction

@Composable
fun App(zoneGroups: List<ZoneGroup>, onEditAction: (EditAction) -> Unit) {
    val windowManager = remember { WindowManager() }
    MaterialTheme {
        CompositionLocalProvider(LocalDefaultModifier provides DefaultModifiers.modifiers) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                Text("Vertical", style = MaterialTheme.typography.h4)
                zoneGroups.filter { it is ZoneGroup.Vertical }.forEach {
                    ZoneGroupItem(it) { windowManager.handleAction(it) }
                }
                Text("Horizontal", style = MaterialTheme.typography.h4)
                zoneGroups.filter { it is ZoneGroup.Horizontal }.forEach {
                    ZoneGroupItem(it) { windowManager.handleAction(it) }
                }
            }
        }
    }
}