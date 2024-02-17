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

    @Test
    fun testTRA() {
        TRA.readTrainTypes()
        TRA.readLines()
        TRA.readShapes()
        TRA.readStations()
        TRA.readTimetables()
        TRA.readODFares()
    }

    @Test
    fun testTHSR() {
        THSR.readStations()
        THSR.readShape()
        THSR.readTimetables()
        THSR.readODFares()
    }

    @Test
    fun testMetro() {
        TaipeiMetro.readStations()
        TaipeiMetro.readLines()
        TaipeiMetro.readShapes()
        TaipeiMetro.readTravelTimes()
        TaipeiMetro.readStationTimetables()
        TaipeiMetro.readODFares()
    }
}