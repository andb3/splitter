package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import data.KeyShortcut
import data.modifiersToString

@Composable
fun KeyShortcutItem(
    shortcut: KeyShortcut,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            shortcut.modifiersToString() + " + ",
            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
        )
        KeyItem(
            shortcut.key
        )
    }
}

@Composable
fun KeyItem(key: Key) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                shape = RoundedCornerShape(4.dp),
            )
            .height(24.dp)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            key.toString().removePrefix("Key: "),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}