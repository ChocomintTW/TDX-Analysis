package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import net.chocomint.tdx.Name
import net.chocomint.tdx.Name.Companion.asName
import org.locationtech.jts.geom.Lineal
import org.locationtech.jts.io.WKTReader

data class ShapeUnit(
    val lineNo: String,
    val lineID: String,
    val name: Name,
    val geometry: Lineal
) {
    companion object {
        fun fromJson(json: JsonObject): ShapeUnit {
            val lineNo   = json["LineNo"].asString
            val lineID   = json["LineID"].asString
            val name     = json["LineName"].asName
            val geometry = WKTReader().read(json["Geometry"].asString) as Lineal

            return ShapeUnit(lineNo, lineID, name, geometry)
        }
    }
}
