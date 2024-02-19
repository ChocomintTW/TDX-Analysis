package net.chocomint.tdx.transportation.metro

import com.google.gson.JsonObject
import net.chocomint.tdx.utils.Name
import net.chocomint.tdx.utils.Name.Companion.asName
import net.chocomint.tdx.utils.toColor
import java.awt.Color

data class MetroLine(
    val id: String,
    val name: Name,
    val color: Color,
    val isBranch: Boolean
) {
    companion object {
        fun fromJson(json: JsonObject): MetroLine {
            val id       = json["LineID"].asString
            val name     = json["LineName"].asName
            val color    = json["LineColor"].asString.toColor()
            val isBranch = json["IsBranch"].asBoolean

            return MetroLine(id, name, color, isBranch)
        }
    }
}
