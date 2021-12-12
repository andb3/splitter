package state

import data.Hotkey
import data.Zone
import data.ZoneGroup
import data.ZoneName

sealed class Action {
    sealed class MoveWindow : Action() {
        abstract val startPercent: Float
        abstract val endPercent: Float

        data class Vertical(override val startPercent: Float, override val endPercent: Float) : MoveWindow()
        data class Horizontal(override val startPercent: Float, override val endPercent: Float) : MoveWindow()
    }
}

sealed class EditGroupAction : Action() {
    abstract val group: ZoneGroup

    data class UpdateName(override val group: ZoneGroup, val name: String) : EditGroupAction()
    sealed class Zone : EditGroupAction() {
        abstract val zone: data.Zone

        data class UpdateHotkey(
            override val group: ZoneGroup,
            override val zone: data.Zone,
            val hotkey: Hotkey
        ) : Zone()

        data class UpdateVisibility(
            override val group: ZoneGroup,
            override val zone: data.Zone,
            val visibility: Boolean
        ) : Zone()

        sealed class Combination : Zone() {
            abstract override val zone: data.Zone.Combination

            data class UpdateName(
                override val group: ZoneGroup,
                override val zone: data.Zone.Combination,
                val name: ZoneName
            ) : Combination()
        }
    }

    sealed class Section : EditGroupAction() {
        abstract val section: data.Section

        data class UpdateName(
            override val group: ZoneGroup,
            override val section: data.Section,
            val name: String
        ) : Section()
    }

    sealed class Divider : EditGroupAction() {
        abstract val divider: data.Divider

        data class UpdateDivider(override val group: ZoneGroup, override val divider: data.Divider) : Divider()
        data class AddDivider(override val group: ZoneGroup, override val divider: data.Divider) : Divider()
        data class RemoveDivider(override val group: ZoneGroup, override val divider: data.Divider) : Divider()
    }
}

sealed class UserAction : Action() {
    sealed class ZoneGroup : UserAction() {
        abstract val zoneGroup: data.ZoneGroup

        data class EditZoneGroup(override val zoneGroup: data.ZoneGroup) : ZoneGroup()
    }
}