package ui

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import data.Modifiers

val LocalDefaultModifier: ProvidableCompositionLocal<Modifiers> = compositionLocalOf { Modifiers() }
