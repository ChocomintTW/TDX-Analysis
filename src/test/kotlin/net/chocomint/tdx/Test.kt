package net.chocomint.tdx

import net.chocomint.tdx.transportation.ServiceDay
import net.chocomint.tdx.utils.toColor
import org.locationtech.jts.geom.Lineal
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.WKTReader
import java.awt.Color

fun main() {
//    val vb = District.readVillageBoundary()
//    val point = Position(119.581176, 23.569208)
//    val village = vb.find { point.isIn(it) }

    val x = WKTReader().read("LINESTRING(1 2, 3 4, 5 6, 7 8)") as Lineal
    println(x)

    val polygon = WKTReader().read("POLYGON((1 2, 3 4, 5 6, 7 8, 1 2))") as Polygon

    val sd = ServiceDay(listOf(false, false, false, false, false, true, true), null, null)
    println(sd.onlyWeekend())

    println("#0a59ae".toColor())
}
