package net.chocomint.tdx.transportation.tra

import com.google.gson.JsonObject
import org.locationtech.jts.geom.Lineal
import org.locationtech.jts.io.WKTReader

data class TRAShape(
    val lineNo: String,
    val lineID: String,
    val geometry: Lineal
) {
    companion object {
        fun fromJson(json: JsonObject): TRAShape {
            val lineNo     = json["LineNo"].asString
            val lineID     = json["LineID"].asString
            val geometry = WKTReader().read(json["Geometry"].asString) as Lineal

            return TRAShape(lineNo, lineID, geometry)
        }
    }
}
