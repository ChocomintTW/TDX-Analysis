package net.chocomint.tdx.transportation.metro

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import net.chocomint.tdx.TaipeiMetro
import net.chocomint.tdx.resource
import net.chocomint.tdx.transportation.Station

fun calculateShortestGraph(stationList: List<Station> = TaipeiMetro.readStations()): ShortestDistanceGraph {
    val stationIndexMapping = stationList
        .associateBy { it.name.zh }
        .toList()
        .mapIndexed { i, pair -> pair.first to i }
        .associate { it }

    val n = stationIndexMapping.size

    val graph = Array(n) { i -> Array(n) { j -> DistanceKm(if (i == j) 0.0 else Double.POSITIVE_INFINITY) } }
    JsonParser.parseReader(resource("/tdx/TaipeiMetro/TaipeiMetroGraph.json"))
        .asJsonArray
        .flatMap {
            val json = it.asJsonObject
            val from = stationIndexMapping[json["from"].asString]!!
            val to   = stationIndexMapping[json["to"].asString]!!
            listOf(
                (from to to) to json["distance"].asDistanceKm,
                (to to from) to json["distance"].asDistanceKm
            )
        }
        .forEach { graph[it.first.first][it.first.second] = it.second }

    for (k in 0..<n) {
        for (i in 0..<n) {
            for (j in 0..<n) {
                if (graph[i][j] > graph[i][k] + graph[k][j])
                    graph[i][j] = graph[i][k] + graph[k][j]
            }
        }
    }

    return ShortestDistanceGraph(stationIndexMapping, graph)
}

@JvmInline
value class DistanceKm(val km: Double): Comparable<DistanceKm> {
    fun isImpossible(): Boolean {
        return km == Double.POSITIVE_INFINITY
    }

    fun toFare(): Int {
        return when(km) {
            in  0.0.. 5.0 -> 20
            in  5.0.. 8.0 -> 25
            in  8.0..11.0 -> 30
            in 11.0..14.0 -> 35
            in 14.0..17.0 -> 40
            in 17.0..20.0 -> 45
            in 20.0..23.0 -> 50
            in 23.0..27.0 -> 55
            in 27.0..31.0 -> 60
            in 31.0..Double.POSITIVE_INFINITY -> 65
            else -> -1
        }
    }

    operator fun plus(dist: DistanceKm): DistanceKm {
        return DistanceKm(km + dist.km)
    }

    override fun compareTo(other: DistanceKm): Int {
        return km.compareTo(other.km)
    }

    override fun toString(): String {
        return "$km km"
    }
}

val JsonElement.asDistanceKm: DistanceKm get() = DistanceKm(asDouble)

data class ShortestDistanceGraph(val mapping: Map<String, Int>, val graph: Array<Array<DistanceKm>>) {

    fun between(from: String, to: String): DistanceKm? {
        return from(from)?.to(to)
    }

    fun from(stationNameInZh: String): DistanceList? {
        return mapping[stationNameInZh]?.let { index -> DistanceList(mapping, graph[index]) }
    }

    // generate by IntelliJ
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShortestDistanceGraph

        if (mapping != other.mapping) return false
        if (!graph.contentDeepEquals(other.graph)) return false

        return true
    }

    // generate by IntelliJ
    override fun hashCode(): Int {
        var result = mapping.hashCode()
        result = 31 * result + graph.contentDeepHashCode()
        return result
    }
}

data class DistanceList(val mapping: Map<String, Int>, val arr: Array<DistanceKm>) {

    fun to(stationNameInZh: String): DistanceKm? {
        return mapping[stationNameInZh]?.let { index -> arr[index] }
    }

    // generate by IntelliJ
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DistanceList

        if (mapping != other.mapping) return false
        if (!arr.contentEquals(other.arr)) return false

        return true
    }

    // generate by IntelliJ
    override fun hashCode(): Int {
        var result = mapping.hashCode()
        result = 31 * result + arr.contentHashCode()
        return result
    }

}