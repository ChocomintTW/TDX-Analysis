package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.ShapeUnit
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.metro.*
import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroCode
import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroStation
import net.chocomint.tdx.utils.*
import java.awt.Color

object TaipeiMetro {
    @MergedData
    fun readStations(): List<Station> {
        return JsonParser.parseReader(resource("/tdx/metro/taipei/Station.json"))
            .asJsonArray
            .map { Station.fromJson(it.asJsonObject) }
    }

    @MergedData
    fun readLines(): List<MetroLine> {
        return JsonParser.parseReader(resource("/tdx/metro/taipei/Line.json"))
            .asJsonArray
            .map { MetroLine.fromJson(it.asJsonObject) }
    }

    @MergedData
    fun readTravelTimes(): List<TravelTime> {
        return JsonParser.parseReader(resource("/tdx/metro/taipei/S2STravelTime.json"))
            .asJsonArray
            .flatMap {
                it.asJsonObject["TravelTimes"].asJsonArray
                    .map { e -> TravelTime.fromJson(e.asJsonObject) }
            }
    }

    @NoNTMCData
    fun readShapes(): List<ShapeUnit> {
        return JsonParser.parseReader(resource("/tdx/metro/taipei/Shape.json"))
            .asJsonArray
            .map { ShapeUnit.fromJson(it.asJsonObject) }
    }

    @NoNTMCData
    fun readStationTimetables(): List<MetroStationTimetable> {
        return JsonParser.parseReader(resource("/tdx/metro/taipei/StationTimeTable.json"))
            .asJsonArray
            .map { MetroStationTimetable.fromJson(it.asJsonObject) }
    }

    @MergedData
    @ForMergingData
    fun readODFares(): List<MetroODFare> {
        return JsonParser.parseReader(resource("/tdx/metro/taipei/ODFare-TRTC.json"))
            .asJsonArray
            .map { MetroODFare.fromJson(it.asJsonObject) } +
                JsonParser.parseReader(resource("/tdx/metro/taipei/ODFare-NTMC.json"))
                    .asJsonArray
                    .map { MetroODFare.fromJson(it.asJsonObject) }
    }

    annotation class MergedData
    annotation class NoNTMCData
    annotation class ForMergingData

    class Processor(
        private val stationsOriginal: List<Station> = readStations(),
        val lines: List<MetroLine> = readLines(),
        private val travelTimes: List<TravelTime> = readTravelTimes(),
        private val shapes: List<ShapeUnit> = readShapes(),
        private val metroStationTimeTables: List<MetroStationTimetable> = readStationTimetables()
    ) {
        private val shortestDistanceGraph: Array<DoubleArray>

        val stations: List<TaipeiMetroStation> = stationsOriginal
            .map { it.name }
            .toSet().toList()
            .map { name ->
                val codeList = stationsOriginal
                    .filter { it.name == name }
                    .map { it.id.asTaipeiMetroCode }
                TaipeiMetroStation(name, codeList)
            }

        private val metroGraph: List<Dijkstra.Edge> = JsonParser
            .parseReader(resource("/tdx/metro/taipei/TaipeiMetroGraph.json"))
            .asJsonArray
            .flatMap {
                val json = it.asJsonObject
                val from = stations.find { s -> s.name.zh == json["from"].asString }!!
                val to   = stations.find { s -> s.name.zh == json["to"].asString }!!
                val dist = json["distance"].asDouble
                listOf(
                    Dijkstra.Edge(from, to, dist),
                    Dijkstra.Edge(to, from, dist)
                )
            }

        private fun indexOf(node: Dijkstra.Node): Int {
            return stations.indexOf(node as TaipeiMetroStation)
        }

        init {
            val n = stations.size
            val graph = Array(n) { i -> DoubleArray(n) { j -> if (i == j) 0.0 else Double.POSITIVE_INFINITY } }

            metroGraph.forEach { graph[indexOf(it.node1)][indexOf(it.node2)] = it.distance }

            for (k in 0..<n) {
                for (i in 0..<n) {
                    for (j in 0..<n) {
                        if (graph[i][j] > graph[i][k] + graph[k][j])
                            graph[i][j] = graph[i][k] + graph[k][j]
                    }
                }
            }

            shortestDistanceGraph = graph
        }

        private val stationAmount: Int get() = stations.size

        fun stationByName(name: String): TaipeiMetroStation? {
            return stations.find { it.name equalString name }
        }

        fun stationByCode(code: String): TaipeiMetroStation? {
            return stations.find { it.code.contains(TaipeiMetroCode(code)) }
        }

        private fun distToPrice(dist: Double): Int {
            return when(dist) {
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

        fun priceBetween(station1: String, station2: String): Int {
            val index1 = stations.indexOf(stationByName(station1))
            val index2 = stations.indexOf(stationByName(station2))

            println(shortestDistanceGraph[index1][index2])

            return distToPrice(shortestDistanceGraph[index1][index2])
        }

        fun priceBetween(station1: TaipeiMetroCode, station2: TaipeiMetroCode): Int {
            val index1 = stations.indexOf(stationByCode(station1.code))
            val index2 = stations.indexOf(stationByCode(station2.code))

            return distToPrice(shortestDistanceGraph[index1][index2])
        }

        fun shortestPath(station1Name: String, station2Name: String): Path {
            val station1 = stationByName(station1Name)!!
            val station2 = stationByName(station2Name)!!

            return Dijkstra
                .findShortestPath(metroGraph, station1, station2)
                .shortestPath()
                .map { it as TaipeiMetroStation }
                .let { Path(it) }
        }
    }

    @JvmInline
    value class Path(private val stationList: List<TaipeiMetroStation>) {
        override fun toString(): String {
            return stationList.joinToString(" -> ")
        }

        fun print(lines: List<MetroLine> = readLines()) {
            val colors = stationList
                .subList(0, stationList.size - 1)
                .mapIndexed { index, station -> Pair(station, stationList[index + 1]) }
                .map {
                    val st1 = it.first
                    val st2 = it.second

                    st1.code
                        .flatMap { c1 ->
                            st2.code
                                .filter { c2 ->
                                    c1.line == c2.line
                                }
                                .map { c2 -> c1 to c2 }
                        }
                }
                .map {
                    if (it.isEmpty())
                        Color.WHITE
                    else
                        lines.find { line -> line.id == it[0].first.line }!!.color
                }

            for (i in 0..(stationList.size - 2)) {
                val st = stationList[i]
                if (i > 0 && colors[i - 1] != colors[i])
                    print(rgbBg(Color.darkGray, st.toString()))
                else
                    print(st)

                if (colors[i] == Color.WHITE)
                    print(rgbBg(Color.darkGray, " -[站外轉乘]- "))
                else
                    print(rgbText(colors[i], " -> "))
            }
            println(stationList.last())
        }
    }
}