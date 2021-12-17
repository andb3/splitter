package ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import data.Hotkey
import data.HotkeyModifiers
import ui.common.HotkeyModifierEditor
import ui.common.KeyContainer
import ui.common.KeyItem

@Composable
fun HotkeyItem(
    hotkey: Hotkey,
    modifier: Modifier = Modifier,
    onEdit: (Hotkey.Shortcut) -> Unit
) {
    when(hotkey) {
        is Hotkey.Shortcut -> ShortcutItem(hotkey, modifier, onEdit)
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
    onEdit: (Hotkey.Shortcut) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when(shortcut.modifiers) {
            is HotkeyModifiers.Custom -> CustomHotkeyModifiersItem(shortcut.modifiers) {
                onEdit.invoke(shortcut.copy(modifiers = it))
            }
            HotkeyModifiers.Default -> DefaultHotkeyModifiersItem { onEdit.invoke(shortcut.copy(modifiers = it)) }
        }
        KeyItem(shortcut.key) { onEdit.invoke(shortcut.copy(key = it)) }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CustomHotkeyModifiersItem(
    customHotkeyModifiers: HotkeyModifiers.Custom,
    modifier: Modifier = Modifier,
    onEdit: (HotkeyModifiers) -> Unit,
) {
    val isHovering = remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .pointerMoveFilter(
                onEnter = { isHovering.value = true; false },
                onExit = { isHovering.value = false; false },
            )
            .padding(end = 12.dp)
    ) {
        when(isHovering.value) {
            false -> {
                KeyContainer {
                    Text(
                        text = customHotkeyModifiers.modifiers.toString(),
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            true -> {
                KeyButton(Icons.Default.Undo, "Reset to default") { onEdit.invoke(HotkeyModifiers.Default) }
                HotkeyModifierEditor(customHotkeyModifiers, onEdit = onEdit)
            }
        }
        Text(text = "+", color = LocalContentColor.current.copy(alpha = ContentAlpha.medium))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DefaultHotkeyModifiersItem(modifier: Modifier = Modifier, onEdit: (HotkeyModifiers) -> Unit) {
    val isHovering = remember { mutableStateOf(false) }
    val defaultModifier = LocalDefaultModifier.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .pointerMoveFilter(
                onEnter = { isHovering.value = true; false },
                onExit = { isHovering.value = false; false },
            )
            .height(24.dp)
    ) {
        if (isHovering.value) {
            KeyButton(Icons.Default.Edit, "Change shortcut modifiers") {
                onEdit.invoke(HotkeyModifiers.Custom(defaultModifier))
            }
        }
        Text(
            "$defaultModifier + ",
            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        )
    }
}

@Composable
private fun KeyButton(icon: ImageVector, contentDescription: String?, onClick: () -> Unit) {
    KeyContainer(
        modifier = Modifier
            .clickable(onClick = onClick)
            .size(24.dp)

    ) {
        Image(
            imageVector = icon,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(LocalContentColor.current.copy(alpha = ContentAlpha.medium)),
            modifier = Modifier
                .align(Alignment.Center)
                .requiredSize(16.dp)
        )
    }
}

@Composable
fun RecordingIndicator(modifier: Modifier = Modifier) {
    val animation = rememberInfiniteTransition()
    val alpha by animation.animateFloat(
        initialValue = 1f,
        targetValue = .1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse,
        )
    )
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = LocalContentColor.current.copy(alpha = alpha),
                shape = CircleShape
            )
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = LocalContentColor.current,
                    shape = CircleShape
                )
                .size(12.dp)
        )
    }
}

