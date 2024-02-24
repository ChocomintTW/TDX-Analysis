package net.chocomint.tdx

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalTime

class UtilTest {
    @Test
    fun testTimeRange() {
        val t1 = LocalTime.of(1, 0)
        val t2 = LocalTime.of(2, 0)
        val r = t1..t2
        assertEquals(r.start.hour, 1)
        assertEquals(r.endInclusive.hour, 2)
    }
}