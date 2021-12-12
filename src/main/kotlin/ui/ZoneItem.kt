package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import data.Hotkey
import data.Zone
import data.readableName
import state.EditGroupAction

@Composable
fun ZoneItem(
    zone: Zone,
    modifier: Modifier = Modifier,
    onMove: () -> Unit,
    onUpdateHotkey: (Hotkey.Shortcut) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onMove)
        ) {
            ZoneIcon(zone)
            Text(zone.readableName(), style = MaterialTheme.typography.subtitle1)
        }
        HotkeyItem(
            hotkey = zone.hotkey,
            onEdit = { onUpdateHotkey.invoke(it) }
        )
    }
}