package ui

import WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.horizontalGroups
import data.verticalGroups

@Composable
fun App() {
    val windowManager = remember { WindowManager() }
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            verticalGroups.forEach {
                ZoneGroupItem(it) { windowManager.handleAction(it) }
            }
            horizontalGroups.forEach {
                ZoneGroupItem(it) { windowManager.handleAction(it) }
            }
        }
    }
}