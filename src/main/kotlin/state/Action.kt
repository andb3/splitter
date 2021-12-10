package state

import data.Hotkey
import data.ZoneName

sealed class Action {
    sealed class MoveWindow : Action() {
        abstract val startPercent: Float
        abstract val endPercent: Float
        data class Vertical(override val startPercent: Float, override val endPercent: Float) : MoveWindow()
        data class Horizontal(override val startPercent: Float, override val endPercent: Float) : MoveWindow()
    }
}

sealed class EditAction : Action() {
    data class UpdateName(val name: String) : EditAction()
    sealed class Zone : EditAction() {
        abstract val zone: data.Zone
        data class UpdateHotkey(override val zone: data.Zone, val hotkey: Hotkey) : Zone()
        data class UpdateVisibility(override val zone: data.Zone, val visibility: Boolean) : Zone()
        sealed class Combination : Zone() {
            abstract override val zone: data.Zone.Combination
            data class UpdateName(override val zone: data.Zone.Combination, val name: ZoneName) : Combination()
        }
    }
    sealed class Section : EditAction() {
        abstract val section: data.Section
        data class UpdateName(override val section: data.Section, val name: String) : Section()
    }

    sealed class Divider : EditAction() {
        abstract val divider: data.Divider
        data class UpdateDivider(override val divider: data.Divider) : Divider()
        data class AddDivider(override val divider: data.Divider) : Divider()
        data class RemoveDivider(override val divider: data.Divider) : Divider()
    }
}