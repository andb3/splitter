package ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import ui.RecordingIndicator
import java.awt.event.KeyEvent

@Composable
fun KeyContainer(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
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

@Composable
fun KeyItem(key: Key, modifier: Modifier = Modifier, onEdit: (Key) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val isFocused = interactionSource.collectIsFocusedAsState()
    KeyContainer(
        modifier = modifier
            .clickable { if (isFocused.value) focusManager.clearFocus() else focusRequester.requestFocus() }
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .onKeyEvent {
                when (isFocused.value && !it.key.isModifier()) {
                    true -> {
                        println("Pressed ${it.key}")
                        onEdit.invoke(it.key)
                        focusManager.clearFocus()
                        true
                    }
                    false -> false
                }
            }
    ) {
        when(isFocused.value) {
            true -> RecordingIndicator(Modifier.align(Alignment.Center))
            false -> Text(key.toString().removePrefix("Key: "), modifier = Modifier.align(Alignment.Center))
        }
    }
}

fun Key.isModifier() = when(this.nativeKeyCode) {
    KeyEvent.VK_CONTROL, KeyEvent.VK_WINDOWS, KeyEvent.VK_ALT, KeyEvent.VK_SHIFT -> true
    else -> false
}