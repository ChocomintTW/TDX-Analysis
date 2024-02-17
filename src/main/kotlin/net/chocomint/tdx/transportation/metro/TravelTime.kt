package net.chocomint.tdx.transportation.metro

import com.google.gson.JsonObject

data class TravelTime(
    val fromStationID: String,
    val toStationID: String,
    val runTime: Int,
    val stopTime: Int
) {
    companion object {
        fun fromJson(json: JsonObject): TravelTime {
            val fromStationID = json["FromStationID"].asString
            val toStationID   = json["ToStationID"].asString
            val runTime       = json["RunTime"].asInt
            val stopTime      = json["StopTime"].asInt

            return TravelTime(fromStationID, toStationID, runTime, stopTime)
        }
    }
}
