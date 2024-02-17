package net.chocomint.tdx.transportation.trtc

import com.google.gson.JsonObject
import net.chocomint.tdx.Name
import net.chocomint.tdx.Name.Companion.asName
import net.chocomint.tdx.utils.toColor
import java.awt.Color

data class TRTCLine(
    val id: String,
    val name: Name,
    val color: Color,
    val isBranch: Boolean
) {
    companion object {
        fun fromJson(json: JsonObject): TRTCLine {
            val id       = json["LineID"].asString
            val name     = json["LineName"].asName
            val color    = json["LineColor"].asString.toColor()
            val isBranch = json["IsBranch"].asBoolean

            return TRTCLine(id, name, color, isBranch)
        }
    }
}
