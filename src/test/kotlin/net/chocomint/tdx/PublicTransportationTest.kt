package net.chocomint.tdx

import org.junit.jupiter.api.Test

class PublicTransportationTest {
    @Test
    fun readTest() {
        // TRA check
        val odFares = TRA.readODFares()
        val map = odFares.map { it.originStationID to it.destinationStationID }
            .groupingBy { it }.eachCount()
        map.entries.toList().take(20).forEach {
            println("${it.key.first} | ${it.key.second} -> ${it.value}")
        }

        val list = odFares.groupBy { Triple(it.originStationID, it.destinationStationID, it.trainTypeCode) }
            .map { it.value.minBy { fare -> fare.price!! } }
        list.forEach { println("${it.originStationID} | ${it.destinationStationID} -> ${it.price}") }

        // TRTC check
        val x = TRTC.readODFares()
        x.filter { it.travelDistance == null }.forEach { println(it.originStationID) }
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
    fun testTRTC() {
        TRTC.readStations()
        TRTC.readLines()
        TRTC.readShapes()
        TRTC.readTravelTimes()
        TRTC.readStationTimetables()
        TRTC.readODFares()
    }
}