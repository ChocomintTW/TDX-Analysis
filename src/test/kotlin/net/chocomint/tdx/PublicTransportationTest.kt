package net.chocomint.tdx

import org.junit.jupiter.api.Test

class PublicTransportationTest {
    @Test
    fun checkTRA() {
        val odFares = TRA.readODFares()
        val map = odFares.map { it.originStationID to it.destinationStationID }
            .groupingBy { it }.eachCount()
        map.entries.toList().take(20).forEach {
            println("${it.key.first} | ${it.key.second} -> ${it.value}")
        }

        val list = odFares.groupBy { Triple(it.originStationID, it.destinationStationID, it.trainTypeCode) }
            .map { it.value.minBy { fare -> fare.price!! } }
        list.forEach { println("${it.originStationID} | ${it.destinationStationID} -> ${it.price}") }
    }

    @Test
    fun checkMetro() {
        val x = TaipeiMetro.readStations()
        println(x.size)
    }
}