package data

import state.Action
import state.EditAction

sealed class ZoneGroup {
    abstract val name: String
    abstract val dividers: List<Divider>
    abstract val sections: List<Section>
    abstract val zones: List<Zone>

    data class Vertical(
        override val name: String,
        override val dividers: List<Divider>,
        override val sections: List<Section.Vertical>,
        override val zones: List<Zone>,
    ) : ZoneGroup()

    data class Horizontal(
        override val name: String,
        override val dividers: List<Divider>,
        override val sections: List<Section.Horizontal>,
        override val zones: List<Zone>,
    ) : ZoneGroup()

    fun withName(name: String): ZoneGroup = with(name = name)

    fun withDividers(newDividers: List<Divider>): ZoneGroup = with(dividers = newDividers).withSections(
        when (sections.first()) {
            is Section.Horizontal -> newDividers.toHorizontalSections(sections as List<Section.Horizontal>)
            is Section.Vertical -> newDividers.toVerticalSections(sections as List<Section.Vertical>)
        }
    )

    fun withSections(newSections: List<Section>) = with(sections = newSections).withZones(newSections.toZones(zones))

    fun withZones(newZones: List<Zone>) = with(zones = newZones)

    private fun with(
        name: String = this.name,
        dividers: List<Divider> = this.dividers,
        sections: List<Section> = this.sections,
        zones: List<Zone> = this.zones
    ) = when (this) {
        is Horizontal -> this.copy(name, dividers, sections as List<Section.Horizontal>, zones)
        is Vertical -> this.copy(name, dividers, sections as List<Section.Vertical>, zones)
    }
}

fun ZoneGroup.applyAction(action: EditAction): ZoneGroup = when (action) {
    is EditAction.UpdateName -> this.withName(name = action.name)
    is EditAction.Divider.AddDivider -> this.withDividers(this.dividers + action.divider)
    is EditAction.Divider.UpdateDivider -> this.withDividers(
        newDividers = this.dividers.map { if (it.id == action.divider.id) action.divider else it }
    )
    is EditAction.Divider.RemoveDivider -> this.withDividers(this.dividers.filter { it.id != action.divider.id })
    is EditAction.Section.UpdateName -> this.withSections(
        newSections = this.sections.map { if (it.id == action.section.id) it.withName(action.name) else it }
    )
    is EditAction.Zone.Combination.UpdateName -> this.withZones(
        newZones = this.zones.map { if (it is Zone.Combination && it.id == action.zone.id) it.withName(action.name) else it }
    )
    is EditAction.Zone.UpdateHotkey -> this.withZones(
        newZones = this.zones.map { if (it.id == action.zone.id) it.withHotkey(action.hotkey) else it }
    )
    is EditAction.Zone.UpdateVisibility -> this.withZones(
        newZones = this.zones.map { if (it.id == action.zone.id) it.withVisibility(action.visibility) else it }
    )
}

fun Zone.moveAction(): Action.MoveWindow = when (this) {
    is Zone.Vertical -> Action.MoveWindow.Vertical(this.startPercent, this.endPercent)
    is Zone.Horizontal -> Action.MoveWindow.Horizontal(this.startPercent, this.endPercent)
}

data class Divider(val id: String, val percent: Float)

sealed class Section {
    val id get() = Pair(startDivider.id, endDivider.id)
    abstract val name: String
    abstract val startDivider: Divider
    abstract val endDivider: Divider

    data class Horizontal(
        //override val id: String,
        override val name: String,
        override val startDivider: Divider,
        override val endDivider: Divider,
    ) : Section()

    data class Vertical(
        //override val id: String,
        override val name: String,
        override val startDivider: Divider,
        override val endDivider: Divider,
    ) : Section()

    fun withName(newName: String) = when (this) {
        is Horizontal -> this.copy(name = newName)
        is Vertical -> this.copy(name = newName)
    }
}

fun List<Divider>.toHorizontalSections(
    oldSections: List<Section.Horizontal>
): List<Section.Horizontal> = (listOf(Divider("Start", 0f)) + this + Divider("End", 1f)).windowed(2)
    .mapIndexed { index, dividers ->
        val startDivider = dividers.first()
        val endDivider = dividers.last()
        val oldSection = oldSections.find { it.startDivider.id == startDivider.id }
        Section.Horizontal(
            //id = oldSection?.id ?: randomID(),
            name = oldSection?.name ?: "Zone ${index + 1}",
            startDivider = startDivider,
            endDivider = endDivider
        )
    }

fun List<Divider>.toVerticalSections(
    oldSections: List<Section.Vertical>
): List<Section.Vertical> = (listOf(Divider("Start", 0f)) + this + Divider("End", 1f)).windowed(2)
    .mapIndexed { index, dividers ->
        val startDivider = dividers.first()
        val endDivider = dividers.last()
        val oldSection = oldSections.find { it.startDivider.id == startDivider.id }
        Section.Vertical(
            //id = oldSection?.id ?: randomID(),
            name = oldSection?.name ?: "Zone ${index + 1}",
            startDivider = startDivider,
            endDivider = endDivider
        )
    }

fun List<Section>.toZones(oldZones: List<Zone>): List<Zone> = this.map { section ->
    val oldZone: Zone.Section? = oldZones
        .filterIsInstance<Zone.Section>()
        .find { it.section.id == section.id }
    when (section) {
        is Section.Horizontal -> Zone.Section.Horizontal(
            section = section,
            isVisible = oldZone?.isVisible ?: true,
            hotkey = oldZone?.hotkey ?: Hotkey.Unset
        )
        is Section.Vertical -> Zone.Section.Vertical(
            section = section,
            isVisible = oldZone?.isVisible ?: true,
            hotkey = oldZone?.hotkey ?: Hotkey.Unset
        )
    }
} + (2 until size).flatMap { windowSize ->
    this.windowed(windowSize).map { sections ->
        val oldZone: Zone.Combination? = oldZones
            .filterIsInstance<Zone.Combination>()
            .find { oldZone ->
                oldZone.sections.zip(sections).all { (old, new) ->
                    old.id == new.id
                }
            }
        when (sections.first()) {
            is Section.Horizontal -> Zone.Combination.Horizontal(
                sections = sections,
                name = oldZone?.name ?: ZoneName.Default,
                isVisible = oldZone?.isVisible ?: true,
                hotkey = oldZone?.hotkey ?: Hotkey.Unset
            )
            is Section.Vertical -> Zone.Combination.Vertical(
                sections = sections,
                name = oldZone?.name ?: ZoneName.Default,
                isVisible = oldZone?.isVisible ?: true,
                hotkey = oldZone?.hotkey ?: Hotkey.Unset
            )
        }
    }
}


sealed class Zone {
    val id get() = Pair(startDivider.id, endDivider.id)
    abstract val name: ZoneName
    abstract val isVisible: Boolean
    abstract val hotkey: Hotkey
    abstract val startDivider: Divider
    abstract val endDivider: Divider
    abstract val startPercent: Float
    abstract val endPercent: Float
    val percentSize get() = endPercent - startPercent

    sealed interface Horizontal
    sealed interface Vertical

    sealed class Section : Zone() {
        abstract val section: data.Section
        override val name: ZoneName = ZoneName.Default
        override val startDivider get() = section.startDivider
        override val endDivider get() = section.endDivider
        override val startPercent get() = section.startDivider.percent
        override val endPercent get() = section.endDivider.percent

        data class Horizontal(
            override val section: data.Section,
            override val isVisible: Boolean,
            override val hotkey: Hotkey,
        ) : Section(), Zone.Horizontal

        data class Vertical(
            override val section: data.Section,
            override val isVisible: Boolean,
            override val hotkey: Hotkey,
        ) : Section(), Zone.Vertical
    }

    sealed class Combination : Zone() {
        abstract val sections: List<data.Section>
        override val startDivider get() = sections.minByOrNull { it.startDivider.percent }!!.startDivider
        override val endDivider get() = sections.minByOrNull { it.endDivider.percent }!!.endDivider
        override val startPercent get() = sections.minOf { it.startDivider.percent }
        override val endPercent get() = sections.maxOf { it.endDivider.percent }

        data class Horizontal(
            override val sections: List<data.Section>,
            override val name: ZoneName,
            override val isVisible: Boolean,
            override val hotkey: Hotkey,
        ) : Combination(), Zone.Horizontal

        data class Vertical(
            override val sections: List<data.Section>,
            override val name: ZoneName,
            override val isVisible: Boolean,
            override val hotkey: Hotkey,
        ) : Combination(), Zone.Vertical

        fun withName(newName: ZoneName) = when(this) {
            is Horizontal -> this.copy(name = newName)
            is Vertical -> this.copy(name = newName)
        }
    }

    fun withHotkey(hotkey: Hotkey) = when(this) {
        is Combination.Horizontal -> this.copy(hotkey = hotkey)
        is Combination.Vertical -> this.copy(hotkey = hotkey)
        is Section.Horizontal -> this.copy(hotkey = hotkey)
        is Section.Vertical -> this.copy(hotkey = hotkey)
    }

    fun withVisibility(isVisible: Boolean) = when(this) {
        is Combination.Horizontal -> this.copy(isVisible = isVisible)
        is Combination.Vertical -> this.copy(isVisible = isVisible)
        is Section.Horizontal -> this.copy(isVisible = isVisible)
        is Section.Vertical -> this.copy(isVisible = isVisible)
    }
}

fun Zone.readableName() = when (val name = this.name) {
    is ZoneName.Custom -> name.name
    ZoneName.Default -> when (this) {
        is Zone.Section -> section.name
        is Zone.Combination -> sections.joinToString(" + ") { it.name }
    }
}

sealed class ZoneName {
    object Default : ZoneName()
    data class Custom(val name: String) : ZoneName()
}