package net.chocomint.tdx.utils

import com.google.gson.JsonElement
import java.awt.Color
import java.time.LocalTime

@OptIn(ExperimentalStdlibApi::class)
fun String.toColor(): Color {
    assert(startsWith("#") && length == 7)

    val r = substring(1, 3).hexToInt()
    val g = substring(3, 5).hexToInt()
    val b = substring(5, 7).hexToInt()

    return Color(r, g, b)
}

val JsonElement.asLocalTime: LocalTime get() = LocalTime.parse(asString)

fun stationIsNeighbor(station1: String, station2: String): Boolean {
    val p1 = station1.toStationPair()
    val p2 = station2.toStationPair()

    if (p1.second == null || p2.second == null)
        return station1.startsWith(station2) || station2.startsWith(station1)

    val exceptions = listOf(
        NeighborStation("O", 12 to 50)
    )

    for (ex in exceptions) {
        if (p1.first == ex.line && p2.first == ex.line && setOf(p1.second, p2.second) == ex.asSet())
            return true
    }

    return p1.first == p2.first && p2.second!! - p1.second!! == 1
}

private data class NeighborStation(val line: String, val neighborNumber: Pair<Int, Int>) {
    fun asSet() = setOf(neighborNumber.first, neighborNumber.second)
}

fun String.toStationPair(): Pair<String?, Int?> {
    val des = Regex("([A-Z]+)(\\d+)").matchEntire(this)?.destructured
    val line = des?.component1()
    val n    = des?.component2()
    return Pair(line, n?.toInt())
}