package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ZoneGroup
import data.moveAction
import state.Action
import state.EditGroupAction

@Composable
fun ZoneGroupItem(
    group: ZoneGroup,
    modifier: Modifier = Modifier,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(group.name, style = MaterialTheme.typography.subtitle1, color = LocalContentColor.current.copy(alpha = ContentAlpha.medium))
        group.zones.forEach { zone ->
            ZoneItem(
                zone = zone,
                onMove = { onAction.invoke(zone.moveAction()) },
                onUpdateHotkey = { onAction.invoke(EditGroupAction.Zone.UpdateHotkey(group, zone, it)) }
            )
        }
    }
}