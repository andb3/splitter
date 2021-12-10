package data

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import randomID

@OptIn(ExperimentalComposeUiApi::class)
val verticalGroups: List<ZoneGroup.Vertical> = listOf(
    verticalGroup("Full", emptyList())
        .withVerticalInformation(
            sectionInfo = listOf("Full height" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.T)),
            zoneInfo = emptyList()
        ),
    verticalGroup("Halves", listOf(Divider(randomID(), .5f)))
        .withVerticalInformation(
            sectionInfo = listOf(
                "Top half" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.G),
                "Bottom half" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.B)
            ),
            zoneInfo = emptyList()
        ),
)

@OptIn(ExperimentalComposeUiApi::class)
val horizontalGroups: List<ZoneGroup.Horizontal> = listOf(
    horizontalGroup("Full", emptyList())
        .withHorizontalInformation(
            sectionInfo = listOf("Full width" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.H)),
            zoneInfo = emptyList()
        ),
    horizontalGroup("Halves", listOf(Divider(randomID(), .5f)))
        .withHorizontalInformation(
            sectionInfo = listOf(
                "Left half" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.DirectionLeft),
                "Right half" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.DirectionRight),
            ),
            zoneInfo = emptyList()
        ),
    horizontalGroup("Thirds", listOf(Divider(randomID(), 1f / 3), Divider(randomID(), 2f / 3)))
        .withHorizontalInformation(
            sectionInfo = listOf(
                "Left third" to Hotkey.Unset,
                "Center third" to Hotkey.Unset,
                "Right third" to Hotkey.Unset,
            ),
            zoneInfo = listOf(
                ZoneName.Custom("Left two thirds") to Hotkey.Unset,
                ZoneName.Custom("Right two thirds") to Hotkey.Unset
            )
        ),
    horizontalGroup("Priority", listOf(Divider(randomID(), 1f / 4), Divider(randomID(), 3f / 4)))
        .withHorizontalInformation(
            sectionInfo = listOf(
                "Left" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.A),
                "Priority" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.S),
                "Right" to Hotkey.Shortcut(HotkeyModifiers.Default, Key.D),
            ),
            zoneInfo = listOf(
                ZoneName.Default to Hotkey.Shortcut(HotkeyModifiers.Default, Key.Z),
                ZoneName.Default to Hotkey.Shortcut(HotkeyModifiers.Default, Key.C)
            )
        ),
)

private fun verticalGroup(
    name: String,
    dividers: List<Divider>
): ZoneGroup.Vertical {
    val sections = dividers.toVerticalSections(emptyList())
    return ZoneGroup.Vertical(
        name = name,
        dividers = dividers,
        sections = sections,
        zones = sections.toZones(emptyList()),
    )
}

private fun horizontalGroup(
    name: String,
    dividers: List<Divider>
): ZoneGroup.Horizontal {
    val sections = dividers.toHorizontalSections(emptyList())
    return ZoneGroup.Horizontal(
        name = name,
        dividers = dividers,
        sections = sections,
        zones = sections.toZones(emptyList()),
    )
}

private fun ZoneGroup.withHorizontalInformation(
    sectionInfo: List<Pair<String, Hotkey>>,
    zoneInfo: List<Pair<ZoneName, Hotkey>>,
): ZoneGroup.Horizontal = withInformation(sectionInfo, zoneInfo) as ZoneGroup.Horizontal

private fun ZoneGroup.withVerticalInformation(
    sectionInfo: List<Pair<String, Hotkey>>,
    zoneInfo: List<Pair<ZoneName, Hotkey>>,
): ZoneGroup.Vertical = withInformation(sectionInfo, zoneInfo) as ZoneGroup.Vertical

private fun ZoneGroup.withInformation(
    sectionInfo: List<Pair<String, Hotkey>>,
    zoneInfo: List<Pair<ZoneName, Hotkey>>,
): ZoneGroup {
    val sectionNames = sectionInfo.map { it.first }
    val sectionHotkeys = sectionInfo.map { it.second }
    val withSectionsFilled = this
        .withSections(
            this.sections
                .zip(sectionNames)
                .map { (section, name) -> section.withName(name) }
        )
    val filledSectionZones = withSectionsFilled.zones.filterIsInstance<Zone.Section>()
        .zip(sectionHotkeys)
        .map { (zone, hotkey) -> zone.withHotkey(hotkey) }
    val filledCombinationZones = withSectionsFilled.zones.filterIsInstance<Zone.Combination>()
        .zip(zoneInfo)
        .map { (zone, info) -> zone.withName(info.first).withHotkey(info.second) }
    return withSectionsFilled.withZones(filledSectionZones + filledCombinationZones)
}
