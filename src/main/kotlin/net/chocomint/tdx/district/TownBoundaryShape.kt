package net.chocomint.tdx.district

import com.google.gson.JsonObject
import org.locationtech.jts.geom.Polygonal
import org.locationtech.jts.io.WKTReader

data class TownBoundaryShape(
    val city: String,
    val cityName: String,
    val townCode: String,
    val townName: String,
    val boundary: Polygonal
) : BoundaryShape {

    override fun getBoundaryGeometry(): Polygonal {
        return boundary
    }

    companion object {
        fun fromJson(json: JsonObject): TownBoundaryShape {
            val city     = json["City"].asString
            val cityName = json["CityName"].asString
            val townCode = json["TownCode"].asString
            val townName = json["TownName"].asString
            val boundary = WKTReader().read(json["Geometry"].asString) as Polygonal

            return TownBoundaryShape(city, cityName, townCode, townName, boundary)
        }
    }
}