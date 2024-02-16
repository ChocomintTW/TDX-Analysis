package net.chocomint.tdx.transportation.tra

import com.google.gson.JsonObject
import net.chocomint.tdx.Name

data class TrainType(
    val id: String,
    val code: Int,
    val name: Name
) {
    companion object {
        fun fromJson(json: JsonObject): TrainType {
            val id   = json["TrainTypeID"].asString
            val code = json["TrainTypeCode"].asString.toInt()
            val name = Name.fromJson(json["TrainTypeName"].asJsonObject)
            return TrainType(id, code, name)
        }
    }
}