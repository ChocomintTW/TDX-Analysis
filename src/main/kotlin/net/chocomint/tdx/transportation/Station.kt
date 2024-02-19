package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import net.chocomint.tdx.utils.Name
import net.chocomint.tdx.utils.Name.Companion.asName
import net.chocomint.tdx.utils.Position

data class Station(
    val uid: String,
    val id: String,
    val code: String?,
    val name: Name,
    val position: Position,
    val address: String,
    val phone: String?,
    val category: String?
) {
    companion object {
        fun fromJson(json: JsonObject): Station {
            val uid      = json["StationUID"].asString
            val id       = json["StationID"].asString
            val code     = json["StationCode"]?.asString
            val name     = json["StationName"].asName
            val position = Position.fromJson(json["StationPosition"].asJsonObject)
            val address  = json["StationAddress"].asString
            val phone    = json["StationPhone"]?.asString
            val category = json["StationClass"]?.asString

            return Station(uid, id, code, name, position, address, phone, category)
        }
    }
}
