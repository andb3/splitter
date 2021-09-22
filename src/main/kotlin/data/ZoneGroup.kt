package data

sealed class ZoneGroup {
    abstract val name: String
    abstract val zones: List<Zone>
    data class Vertical(
        override val name: String,
        override val zones: List<Zone.Vertical>
    ) : ZoneGroup()
    data class Horizontal(
        override val name: String,
        override val zones: List<Zone.Horizontal>
    ) : ZoneGroup()
}
sealed class Zone {
    abstract val name: String
    abstract val startPercent: Float
    abstract val endPercent: Float
    abstract val shortcut: KeyShortcut
    data class Horizontal(override val name: String, override val startPercent: Float, override val endPercent: Float, override val shortcut: KeyShortcut) : Zone()
    data class Vertical(override val name: String, override val startPercent: Float, override val endPercent: Float, override val shortcut: KeyShortcut) : Zone()
}

val Zone.percentSize get() = endPercent - startPercent