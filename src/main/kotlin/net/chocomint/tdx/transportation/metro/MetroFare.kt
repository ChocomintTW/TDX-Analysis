package net.chocomint.tdx.transportation.metro

import com.google.gson.JsonObject
import net.chocomint.tdx.transportation.*

data class MetroFare(
    val ticketType: TicketType,
    val fareClass: FareClass,
    val citizenCode: String,
    val price: Int
) {
    companion object {
        fun fromJson(json: JsonObject): MetroFare {
            val ticketType = json["TicketType"].asTicketType
            val fareClass  = json["FareClass"].asFareClass
            val cabinClass = json["CitizenCode"].asString
            val price      = json["Price"].asInt

            return MetroFare(ticketType, fareClass, cabinClass, price)
        }
    }
}
