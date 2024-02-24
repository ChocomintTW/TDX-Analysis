package net.chocomint.tdx

import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroCode
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime

class ProcessorTest {

    @Test
    fun processTRA() {
        val processor = TRA.Processor()

        val mainLine = processor.roundIslandRail()
        val branches = processor.branches()

        val train = processor.trainOf(666)!!
        println(train.toTRAString(processor))
        println()

        val stationTimetable = processor.stationTimetable("臺南", DayOfWeek.TUESDAY)

        processor.search("臺南", "臺北")
            .filterOn(DayOfWeek.WEDNESDAY)
            .filterType("普悠瑪")
            .print()
    }

    @Test
    fun processTHSR() {
        val processor = THSR.Processor()

        val timetable = processor.trainOf(1546)!!
        println(timetable)

        processor.search("台北", "台南")
            .filterOn(DayOfWeek.SUNDAY)
            .filterTime(6..15)
            .print()
    }

    @Test
    fun processTaipeiMetro() {
        val processor = TaipeiMetro.Processor()

        val st1 = processor.stationByCode("G03")!!
        val st2 = processor.stationByCode("G03A")!!
        println(st2 isBranchNeighborWith st1)

        println(processor.stationByName("蘆洲"))
        println(processor.stationByName("Xinbeitou"))
        println(processor.stationByCode("G03A"))

        println(processor.priceBetween(TaipeiMetroCode("G19"), TaipeiMetroCode("Y19")))
        println(processor.priceBetween("新北產業園區", "西門"))

        val path = processor.shortestPath("幸福", "大安")
        path.print(processor.lines)
    }
}