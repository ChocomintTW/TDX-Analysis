package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import net.chocomint.tdx.utils.asLocalTime
import java.time.LocalTime

data class StopTime(
    val stationID: String,
    val arrivalTime: LocalTime?,
    val departureTime: LocalTime?
) {
    override fun toString(): String {
        return "$stationID[$arrivalTime-$departureTime]"
    }

    companion object {
        fun fromJson(json: JsonObject): StopTime {
            val stationID     = json["StationID"].asString
            val arrivalTime   = json["ArrivalTime"]?.asLocalTime
            val departureTime = json["DepartureTime"]?.asLocalTime

            return StopTime(stationID, arrivalTime, departureTime)
        }
    }
}
