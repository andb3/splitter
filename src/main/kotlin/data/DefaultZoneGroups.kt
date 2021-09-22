package data

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key

@OptIn(ExperimentalComposeUiApi::class)
val verticalGroups = listOf(
    ZoneGroup.Vertical(
        name = "Full",
        zones = listOf(
            Zone.Vertical(
                name = "Full Height",
                startPercent = 0f,
                endPercent = 1f,
                shortcut = KeyShortcut(
                    key = Key.V,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            )
        )
    ),
    ZoneGroup.Vertical(
        name = "Halves",
        zones = listOf(
            Zone.Vertical(
                name = "Top Half",
                startPercent = 0f,
                endPercent = 0.5f,
                shortcut = KeyShortcut(
                    key = Key.DirectionUp,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
            Zone.Vertical(
                name = "Bottom Half",
                startPercent = 0.5f,
                endPercent = 1f,
                shortcut = KeyShortcut(
                    key = Key.DirectionDown,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
        )
    ),
)

@OptIn(ExperimentalComposeUiApi::class)
val horizontalGroups = listOf(
    ZoneGroup.Horizontal(
        name = "Full",
        zones = listOf(
            Zone.Horizontal(
                name = "Full Width",
                startPercent = 0f,
                endPercent = 1f,
                shortcut = KeyShortcut(
                    key = Key.H,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            )
        )
    ),
    ZoneGroup.Horizontal(
        name = "Halves",
        zones = listOf(
            Zone.Horizontal(
                name = "Left Half",
                startPercent = 0f,
                endPercent = 0.5f,
                shortcut = KeyShortcut(
                    key = Key.DirectionLeft,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
            Zone.Horizontal(
                name = "Right Half",
                startPercent = 0.5f,
                endPercent = 1f,
                shortcut = KeyShortcut(
                    key = Key.DirectionRight,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
        )
    ),

    ZoneGroup.Horizontal(
        name = "Thirds",
        zones = listOf(
            Zone.Horizontal(
                name = "Left Third",
                startPercent = 0f,
                endPercent = 1f/3,
                shortcut = KeyShortcut(
                    key = Key.A,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
            Zone.Horizontal(
                name = "Center Third",
                startPercent = 1f/3,
                endPercent = 2f/3,
                shortcut = KeyShortcut(
                    key = Key.S,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
            Zone.Horizontal(
                name = "Right Third",
                startPercent = 2f/3,
                endPercent = 1f,
                shortcut = KeyShortcut(
                    key = Key.D,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
            Zone.Horizontal(
                name = "Left Two Thirds",
                startPercent = 0f,
                endPercent = 2f/3,
                shortcut = KeyShortcut(
                    key = Key.Z,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
            Zone.Horizontal(
                name = "Right Two Thirds",
                startPercent = 1f/3,
                endPercent = 1f,
                shortcut = KeyShortcut(
                    key = Key.X,
                    ctrl = true,
                    meta = true,
                    shift = true
                )
            ),
        )
    ),
)