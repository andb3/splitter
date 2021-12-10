package fluent.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

/**
 * A FluentTheme defines the styling principles from the Fluent design specification.
 *
 * Fluent components such as [Button] and [Checkbox] use values provided here when retrieving
 * default values.
 *
 * It defines colors as specified in the [Material Color theme creation spec](https://material.io/design/color/the-color-system.html#color-theme-creation),
 * typography defined in the [Material Type Scale spec](https://material.io/design/typography/the-type-system.html#type-scale),
 * and shapes defined in the [Shape scheme](https://material.io/design/shape/applying-shape-to-ui.html#shape-scheme).
 *
 * All values may be set by providing this component with the [colors][FluentColors],
 * [typography][FluentTypography], and [shapes][FluentShapes] attributes. Use this to configure the overall
 * theme of elements within this FluentTheme.
 *
 * Any values that are not set will inherit the current value from the theme, falling back to the
 * defaults if there is no parent FluentTheme. This allows using a FluentTheme at the top
 * of your application, and then separate FluentTheme(s) for different screens / parts of your
 * UI, overriding only the parts of the theme definition that need to change.
 *
 *
 * @param colors A complete definition of the Fluent Color theme for this hierarchy
 * @param typography A set of text styles to be used as this hierarchy's typography system
 * @param shapes A set of shapes to be used by the components in this hierarchy
 */
@Composable
fun FluentTheme(
    colors: FluentColors = FluentTheme.colors,
    typography: FluentTypography = FluentTheme.typography,
    shapes: FluentShapes = FluentTheme.shapes,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }.apply { updateColorsFrom(colors) }
    val rippleIndication = rememberRipple()
    val selectionColors = rememberTextSelectionColors(rememberedColors)
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalIndication provides rippleIndication,
        LocalShapes provides shapes,
        LocalTextSelectionColors provides selectionColors,
        LocalTypography provides typography
    ) {
        ProvideTextStyle(value = typography.body, content = content)
    }
}

/**
 * Contains functions to access the current theme values provided at the call site's position in
 * the hierarchy.
 */
object FluentTheme {
    /**
     * Retrieves the current [FluentColors] at the call site's position in the hierarchy.
     */
    val colors: FluentColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    /**
     * Retrieves the current [FluentTypography] at the call site's position in the hierarchy.
     */
    val typography: FluentTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    /**
     * Retrieves the current [FluentShapes] at the call site's position in the hierarchy.
     */
    val shapes: FluentShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}
