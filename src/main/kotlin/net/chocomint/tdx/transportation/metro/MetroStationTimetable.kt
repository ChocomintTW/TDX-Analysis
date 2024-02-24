package net.chocomint.tdx.transportation.metro

import com.google.gson.JsonObject
import net.chocomint.tdx.transportation.ArrivalDeparture
import net.chocomint.tdx.transportation.ServiceDay

data class MetroStationTimetable(
    val stationID: String,
    val destinationStationID: String,
    val timetable: List<ArrivalDeparture>,
    val serviceDay: ServiceDay
) {
    companion object {
        fun fromJson(json: JsonObject): MetroStationTimetable {
            val stationID            = json["StationID"].asString
            val destinationStationID = json["DestinationStaionID"].asString
            val serviceDay           = ServiceDay.fromJson(json["ServiceDay"].asJsonObject, true)

            val timetable = json["Timetables"].asJsonArray
                .map { ArrivalDeparture.fromJson(it.asJsonObject) }

            return MetroStationTimetable(stationID, destinationStationID, timetable, serviceDay)
        }
    }
}
