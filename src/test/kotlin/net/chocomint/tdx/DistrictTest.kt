package net.chocomint.tdx

import org.junit.jupiter.api.Test

class DistrictTest {

    @Test
    fun readCityBoundaryTest() {
        District.readCityBoundary()
    }

    @Test
    fun readTownBoundaryTest() {
        District.readTownBoundary()
    }

    @Test
    fun readVillageBoundaryTest() {
        District.readVillageBoundary()
    }
}