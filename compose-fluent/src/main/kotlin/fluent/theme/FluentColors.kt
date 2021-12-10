package fluent.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class FluentColors(
    val fill: Fill,
    val elevation: Elevation,
) {
    data class Fill(
        val text: Text,
        val accentText: Text,
        val textOnAccent: TextOnAccent,
        val control: Control,
        val controlStrong: ControlStrong,
        val subtle: Subtle,
        val controlSolid: ControlSolid,
        val controlAlt: ControlAlt,
        val accent: Accent,
        val system: System,
        val controlOnImage: ControlOnImage,
    ) {
        data class Text(
            val primary: Color,
            val secondary: Color,
            val tertiary: Color,
            val disabled: Color,
        )

        data class TextOnAccent(
            val primary: Color,
            val secondary: Color,
            val disabled: Color,
            val selectedText: Color,
        )

        data class Control(
            val transparent: Color,
            val default: Color,
            val secondary: Color,
            val tertiary: Color,
            val inputActive: Color,
            val disabled: Color,
        )

        data class ControlStrong(
            val default: Color,
            val disabled: Color,
        )

        data class Subtle(
            val transparent: Color,
            val secondary: Color,
            val tertiary: Color,
            val disabled: Color,
        )

        data class ControlSolid(
            val default: Color,
        )

        data class ControlAlt(
            val transparent: Color,
            val secondary: Color,
            val tertiary: Color,
            val quaternary: Color,
            val disabled: Color,
        )

        data class Accent(
            val default: Color,
            val secondary: Color,
            val tertiary: Color,
            val disabled: Color,
            val selectedTextBackground: Color,
        )

        data class System(
            val critical: Color,
            val success: Color,
            val attention: Color,
            val caution: Color,
            val attentionBackground: Color,
            val successBackground: Color,
            val cautionBackground: Color,
            val criticalBackground: Color,
            val neutral: Color,
            val neutralBackground: Color,
            val solidNeutral: Color,
            val solidAttentionBackground: Color,
            val solidNeutralBackground: Color,
        )

        data class ControlOnImage(
            val default: Color,
            val secondary: Color,
            val tertiary: Color,
            val disabled: Color,
        )
    }

    data class Elevation(
        val control: Control,
        val circle: Circle,
        val textControl: TextControl,
        val accentControl: AccentControl,
    ) {
        data class Control(
            val border: Brush,
        )

        data class Circle(
            val border: Brush,
        )

        data class TextControl(
            val border: Brush,
            val borderFocused: Brush,
        )
        data class AccentControl(
            val border: Brush,
        )
    }

    data class StrokeColor(
        val controlStroke: ControlStroke,
    ) {
        data class ControlStroke(
            val default: Color,
            val secondary: Color,
            val onAccentDefault: Color,
            val onAccentSecondary: Color,
            val onAccentTertiary: Color,
            val onAccentDisabled: Color,
            val forStrongFillWhenOnImage: Color,
        )
    }
}

fun lightColors(): FluentColors = FluentColors(

)

/**
 * Updates the internal values of the given [FluentColors] with values from the [other] [FluentColors]. This
 * allows efficiently updating a subset of [FluentColors], without recomposing every composable that
 * consumes values from [LocalColors].
 *
 * Because [FluentColors] is very wide-reaching, and used by many expensive composables in the
 * hierarchy, providing a new value to [LocalColors] causes every composable consuming
 * [LocalColors] to recompose, which is prohibitively expensive in cases such as animating one
 * color in the theme. Instead, [FluentColors] is internally backed by [mutableStateOf], and this
 * function mutates the internal state of [this] to match values in [other]. This means that any
 * changes will mutate the internal state of [this], and only cause composables that are reading
 * the specific changed value to recompose.
 */
internal fun FluentColors.updateColorsFrom(other: FluentColors) {
    primary = other.primary
    primaryVariant = other.primaryVariant
    secondary = other.secondary
    secondaryVariant = other.secondaryVariant
    background = other.background
    surface = other.surface
    error = other.error
    onPrimary = other.onPrimary
    onSecondary = other.onSecondary
    onBackground = other.onBackground
    onSurface = other.onSurface
    onError = other.onError
    isLight = other.isLight
}

/**
 * CompositionLocal used to pass [FluentColors] down the tree.
 *
 * Setting the value here is typically done as part of [FluentTheme], which will
 * automatically handle efficiently updating any changed colors without causing unnecessary
 * recompositions, using [FluentColors.updateColorsFrom].
 * To retrieve the current value of this CompositionLocal, use [FluentTheme.colors].
 */
internal val LocalColors = staticCompositionLocalOf { lightColors() }