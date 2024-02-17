package net.chocomint.tdx.utils

import com.google.gson.JsonObject
import net.chocomint.tdx.district.BoundaryShape
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygonal
import org.locationtech.jts.io.WKTReader

data class Position(val longitude: Double, val latitude: Double) {

    fun toGeometryPoint(): Point {
        return WKTReader().read("POINT ($longitude $latitude)") as Point
    }

    fun isIn(polygon: Polygonal): Boolean {
        return (polygon as Geometry).contains(toGeometryPoint())
    }

    fun isIn(boundaryShape: BoundaryShape): Boolean {
        return (boundaryShape.getBoundaryGeometry() as Geometry).contains(toGeometryPoint())
    }

    override fun toString(): String {
        return "($longitude, $latitude)"
    }

    companion object {
        fun fromJson(json: JsonObject): Position {
            return Position(
                json["PositionLon"].asDouble,
                json["PositionLat"].asDouble
            )
        }
    }
}
