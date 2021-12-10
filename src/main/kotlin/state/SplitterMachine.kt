package state

import data.Zone
import data.ZoneGroup
import data.horizontalGroups
import data.verticalGroups
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplitterMachine {
    val zones: MutableStateFlow<List<ZoneGroup>> = MutableStateFlow(verticalGroups + horizontalGroups)
    fun allZones() = zones.value.flatMap { it.zones }
}