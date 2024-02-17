package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import net.chocomint.tdx.utils.asLocalTime
import java.time.LocalTime

data class ArrivalDeparture(
    val arrival: LocalTime,
    val departure: LocalTime
) {
    companion object {
        fun fromJson(json: JsonObject): ArrivalDeparture {
            return ArrivalDeparture(
                json["ArrivalTime"].asLocalTime,
                json["DepartureTime"].asLocalTime
            )
        }
    }
}
