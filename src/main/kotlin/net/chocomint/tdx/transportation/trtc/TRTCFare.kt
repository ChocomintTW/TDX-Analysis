package net.chocomint.tdx.transportation.trtc

import com.google.gson.JsonObject
import net.chocomint.tdx.transportation.*

data class TRTCFare(
    val ticketType: TicketType,
    val fareClass: FareClass,
    val citizenCode: String,
    val price: Int
) {
    companion object {
        fun fromJson(json: JsonObject): TRTCFare {
            val ticketType = json["TicketType"].asTicketType
            val fareClass  = json["FareClass"].asFareClass
            val cabinClass = json["CitizenCode"].asString
            val price      = json["Price"].asInt

            return TRTCFare(ticketType, fareClass, cabinClass, price)
        }
    }
}
