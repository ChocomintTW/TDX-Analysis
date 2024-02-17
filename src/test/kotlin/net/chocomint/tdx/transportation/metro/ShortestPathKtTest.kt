package net.chocomint.tdx.transportation.metro

import org.junit.jupiter.api.Test

class ShortestPathKtTest {

    @Test
    fun calculateShortestTest() {
        val distances = calculateShortestGraph()

        distances.graph.forEach { arr -> println(arr.joinToString(", ") {
                if (it.isImpossible())
                    "∞".padStart(5)
                else
                    String.format("%.2f", it.km).padStart(5)
            } )
        }
    }

    @Test
    fun shortestGetTest() {
        val distances = calculateShortestGraph()
        val dist = distances.between("南港軟體園區", "幸福")!!
        println(dist)
        println("$${dist.toFare()}")
    }
}