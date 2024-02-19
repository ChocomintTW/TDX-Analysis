package net.chocomint.tdx

import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroCode
import net.chocomint.tdx.utils.printAll
import org.junit.jupiter.api.Test

class ProcessorTest {

    @Test
    fun processTRA() {
        val processor = TRA.Processor()

        val simple = processor.simpleTrainTypeMap()
        simple.printAll()
        val typeMap = processor.trainTypeMapByID()

        val mainLine = processor.roundIslandRail()
        mainLine.printAll("[Main]")
        val branches = processor.branches()
        branches.printAll("[Branches]")

        val timetable = processor.timetableOf(146)!!
        println(timetable)
        println(typeMap[timetable.trainTypeID!!])
    }

    @Test
    fun processTHSR() {
        val processor = THSR.Processor()

        val timetable = processor.timetableOf(1546)!!
        println(timetable)
    }

    @Test
    fun processTaipeiMetro() {
        val processor = TaipeiMetro.Processor()

        val st1 = processor.stationByCode("G03")
        val st2 = processor.stationByCode("G03A")
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