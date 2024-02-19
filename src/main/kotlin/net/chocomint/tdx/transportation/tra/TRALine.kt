package net.chocomint.tdx.transportation.tra

import com.google.gson.JsonObject
import net.chocomint.tdx.utils.Name
import net.chocomint.tdx.utils.Name.Companion.asName

data class TRALine(
    val id: String,
    val name: Name,
    val sectionName: Name,
    val isBranch: Boolean
) {
    companion object {
        fun fromJson(json: JsonObject): TRALine {
            val id = json["LineID"].asString
            val name = json["LineName"].asName
            val sectionName = json["LineSectionName"].asName
            val isBranch = json["IsBranch"].asBoolean

            return TRALine(id, name, sectionName, isBranch)
        }
    }
}
