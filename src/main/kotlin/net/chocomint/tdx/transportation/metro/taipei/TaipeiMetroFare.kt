package net.chocomint.tdx.transportation.metro.taipei

import net.chocomint.tdx.transportation.metro.calculateShortestGraph

class TaipeiMetroFare {
    private val mapping: Map<String, Int>
    private val fareGraph: Array<Array<Int>>

    init {
        val graph = calculateShortestGraph()
        mapping = graph.mapping
        fareGraph = graph.graph.map { it.map { km -> km.toFare() }.toTypedArray() }.toTypedArray()
    }

    fun betweenStation(from: String, to: String): Int {
        if (!mapping.containsKey(from) || !mapping.containsKey(to))
            throw InvalidStationError()

        return fareGraph[mapping[from]!!][mapping[to]!!]
    }

    class InvalidStationError : Error()
}