package net.chocomint.tdx.transportation.thsr

import com.google.gson.JsonObject
import net.chocomint.tdx.transportation.*

data class THSRFare(
    val ticketType: TicketType,
    val fareClass: FareClass,
    val cabinClass: CabinClass,
    val price: Int
) {
    companion object {
        fun fromJson(json: JsonObject): THSRFare {
            val ticketType = json["TicketType"].asTicketType
            val fareClass  = json["FareClass"].asFareClass
            val cabinClass = json["CabinClass"].asCabinClass
            val price      = json["Price"].asInt

            return THSRFare(ticketType, fareClass, cabinClass, price)
        }
    }
}
