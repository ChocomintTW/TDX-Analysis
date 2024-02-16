package net.chocomint.tdx.transportation.tra

import com.google.gson.JsonObject
import net.chocomint.tdx.Name

data class TRALine(
    val id: String,
    val name: Name,
    val sectionName: Name,
    val isBranch: Boolean
) {
    companion object {
        fun fromJson(json: JsonObject): TRALine {
            val id = json["LineID"].asString
            val name = Name.fromJson(json["LineName"].asJsonObject)
            val sectionName = Name.fromJson(json["LineSectionName"].asJsonObject)
            val isBranch = json["IsBranch"].asBoolean

            return TRALine(id, name, sectionName, isBranch)
        }
    }
}
