package fluent.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Immutable
class FluentTypography(
    val display: TextStyle,
    val titleLarge: TextStyle,
    val title: TextStyle,
    val subtitle: TextStyle,
    val bodyLarge: TextStyle,
    val bodyStrong: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
) {
    constructor(
        defaultFontFamily: FontFamily = FontFamily.Default,
        display: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 68.sp,
            lineHeight = 92.sp
        ),
        titleLarge: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            lineHeight = 52.sp
        ),
        title: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        subtitle: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        bodyLarge: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 24.sp
        ),
        bodyStrong: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        body: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        caption: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    ) : this(
        display.withDefaultFontFamily(defaultFontFamily),
        titleLarge,
        title,
        subtitle,
        bodyLarge,
        bodyStrong,
        body,
        caption
    )
}

/**
 * @return [this] if there is a [FontFamily] defined, otherwise copies [this] with [default] as
 * the [FontFamily].
 */
private fun TextStyle.withDefaultFontFamily(default: FontFamily): TextStyle {
    return if (fontFamily != null) this else copy(fontFamily = default)
}

/**
 * This CompositionLocal holds on to the current definition of typography for this application as
 * described by the Fluent spec. You can read the values in it when creating custom components
 * that want to use Fluent types, as well as override the values when you want to re-style a
 * part of your hierarchy. Material components related to text such as [Button] will use this
 * CompositionLocal to set values with which to style children text components.
 *
 * To access values within this CompositionLocal, use [FluentTheme.typography].
 */
internal val LocalTypography = staticCompositionLocalOf { FluentTypography() }
