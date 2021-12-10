package ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import data.*

@Composable
fun HotkeyItem(
    hotkey: Hotkey,
    modifier: Modifier = Modifier,
) {
    when(hotkey) {
        is Hotkey.Shortcut -> ShortcutItem(hotkey, modifier)
        Hotkey.Recording -> TODO()
        Hotkey.Unset -> UnsetItem(modifier)
    }
}

@Composable
private fun UnsetItem(modifier: Modifier = Modifier) = KeyContainer {
    Text(
        text = "Not Set",
        modifier = modifier.align(Alignment.Center),
        color = LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
    )
}

@Composable
private fun ShortcutItem(
    shortcut: Hotkey.Shortcut,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {


        when(shortcut.modifiers) {
            is HotkeyModifiers.Custom -> Text(
                shortcut.modifiers.modifiers.toString() + " + ",
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
            )
            HotkeyModifiers.Default -> Text(
                LocalDefaultModifier.current.toString() + " + ",
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
        }
        KeyItem(shortcut.key)
    }
}


@Composable
fun KeyItem(key: Key) = KeyContainer {
    Text(
        key.toString().removePrefix("Key: "),
        modifier = Modifier.align(Alignment.Center)
    )
}

@Composable
fun RecordingItem(key: Key) = KeyContainer {
    val animation = rememberInfiniteTransition()
    val alpha by animation.animateFloat(
        initialValue = 1f,
        targetValue = .1f,
        animationSpec = infiniteRepeatable(
            animation = tween(),
            repeatMode = RepeatMode.Reverse,
        )
    )
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = LocalContentColor.current.copy(alpha = alpha),
                shape = CircleShape
            )
            .padding(1.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = LocalContentColor.current,
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun KeyContainer(content: @Composable BoxScope.() -> Unit) {
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
        content()
    }
}