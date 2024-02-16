package net.chocomint.tdx.district

import com.google.gson.JsonObject
import org.locationtech.jts.geom.Polygonal
import org.locationtech.jts.io.WKTReader

data class CityBoundaryShape(
    val city: String,
    val cityName: String,
    val boundary: Polygonal
) : BoundaryShape {

    override fun getBoundaryGeometry(): Polygonal {
        return boundary
    }

    companion object {
        fun fromJson(json: JsonObject): CityBoundaryShape {
            val city     = json["City"].asString
            val cityName = json["CityName"].asString
            val boundary = WKTReader().read(json["Geometry"].asString) as Polygonal

            return CityBoundaryShape(city, cityName, boundary)
        }
    }
}