package net.chocomint.tdx.transportation.metro.taipei

import org.junit.jupiter.api.Test

class TaipeiMetroFareTest {

    @Test
    fun betweenStationTest() {
        val taipeiMetro = TaipeiMetroFare()
        val price = taipeiMetro.betweenStation("南港展覽館", "幸福")
        println(price)
    }
}