package net.chocomint.tdx.transportation.tra

import com.google.gson.JsonObject
import net.chocomint.tdx.Name
import net.chocomint.tdx.Name.Companion.asName

data class TrainType(
    val id: String,
    val code: Int,
    val name: Name
) {
    companion object {
        fun fromJson(json: JsonObject): TrainType {
            val id   = json["TrainTypeID"].asString
            val code = json["TrainTypeCode"].asString.toInt()
            val name = json["TrainTypeName"].asName
            return TrainType(id, code, name)
        }
    }
}