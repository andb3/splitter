package fluent.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import fluent.theme.FluentTheme

@Composable
fun NavigationListItemExpanded(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(width = 48.dp, height = 40.dp)
    ) {

    }
}

@Composable
fun NavigationListItemCollapsed(
    selected: Boolean,
    icon: @Composable BoxScope.() -> Unit,
    disabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(width = 48.dp, height = 40.dp)
    ) {
        val backgroundColor = mutableStateOf(if (selected) FluentTheme.colors.fill.subtle.secondary else FluentTheme.colors.fill.subtle.transparent)
        Box(
            modifier = Modifier
                .pointerInput(selected, disabled) {

                }
                .pointerMoveFilter(
                    onEnter = { backgroundColor.value = if (selected) FluentTheme.colors.fill.subtle.tertiary else FluentTheme.colors.fill.subtle.tertiary }
                )
                .background(FluentTheme.colors.fill.subtle.secondary)
                .size(width = 40.dp, height = 36.dp)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(FluentTheme.colors.fill.accent.default)
                    .size(3.dp, 16.dp)
            )
            icon()
        }
    }
}

@Composable
fun MouseStyle(content: @Composable () -> Unit) {
    
}