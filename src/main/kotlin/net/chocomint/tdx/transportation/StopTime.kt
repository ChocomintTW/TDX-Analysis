package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import java.time.LocalTime

data class StopTime(
    val stationID: String,
    val arrivalTime: LocalTime?,
    val departureTime: LocalTime?
) {
    companion object {
        fun fromJson(json: JsonObject): StopTime {
            val stationID     = json["StationID"].asString
            val arrivalTime   = json["ArrivalTime"]?.asString?.let { LocalTime.parse(it) }
            val departureTime = json["DepartureTime"]?.asString?.let { LocalTime.parse(it) }

            return StopTime(stationID, arrivalTime, departureTime)
        }
    }
}
