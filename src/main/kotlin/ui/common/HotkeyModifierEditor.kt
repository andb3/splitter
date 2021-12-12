package ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import data.HotkeyModifiers
import data.Modifiers
import java.lang.Error

private data class EnabledModifier(val modifierName: String, val isEnabled: Boolean)

@Composable
fun HotkeyModifierEditor(
    hotkeyModifiers: HotkeyModifiers.Custom,
    modifier: Modifier = Modifier,
    onEdit: (HotkeyModifiers.Custom) -> Unit) {
    val enabled = listOf(
        EnabledModifier("Ctrl", hotkeyModifiers.modifiers.ctrl),
        EnabledModifier("Meta", hotkeyModifiers.modifiers.meta),
        EnabledModifier("Alt", hotkeyModifiers.modifiers.alt),
        EnabledModifier("Shift", hotkeyModifiers.modifiers.shift),
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        ListWithSeparators(
            items = enabled,
            listItemContent = { enabledModifier ->
                KeyContainer(
                    modifier = modifier
                        .graphicsLayer(alpha = if (enabledModifier.isEnabled) 1f else ContentAlpha.disabled)
                        .clickable {
                            val newModifiers = hotkeyModifiers
                                .withKeyEnabled(enabledModifier.modifierName, !enabledModifier.isEnabled)
                            onEdit.invoke(newModifiers)
                        }
                ) {
                    Text(enabledModifier.modifierName, modifier = Modifier.align(Alignment.Center))
                }
            },
            separatorContent = { Text("+", color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)) }
        )
    }
}

private fun HotkeyModifiers.Custom.withKeyEnabled(key: String, enabled: Boolean): HotkeyModifiers.Custom {
    val newModifiers = this.modifiers.run {
        when (key) {
            "Ctrl" -> Modifiers(ctrl = enabled, meta = meta, alt = alt, shift = shift)
            "Meta" -> Modifiers(ctrl = ctrl, meta = enabled, alt = alt, shift = shift)
            "Alt" -> Modifiers(ctrl = ctrl, meta = meta, alt = enabled, shift = shift)
            "Shift" -> Modifiers(ctrl = ctrl, meta = meta, alt = alt, shift = enabled)
            else -> throw Error("Key must be 'Ctrl', 'Meta', 'Alt', or 'Shift'")
        }
    }
    return this.copy(modifiers = newModifiers)
}

@Composable
fun <T> ListWithSeparators(
    items: List<T>,
    listItemContent: @Composable (T) -> Unit,
    separatorContent: @Composable () -> Unit
) {
    items.forEachIndexed { index, t ->
        listItemContent(t)
        if (index != items.size - 1) separatorContent()
    }
}