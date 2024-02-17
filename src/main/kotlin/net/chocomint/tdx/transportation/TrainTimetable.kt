package net.chocomint.tdx.transportation

import com.google.gson.JsonObject

data class TrainTimetable(
    val trainNo: String,
    val trainTypeID: String?,
    val startStationID: String,
    val endStationID: String,
    val stopTimeList: List<StopTime>,
    val serviceDay: ServiceDay
) {
    companion object {
        fun fromJson(json: JsonObject, infoKey: String = "TrainInfo"): TrainTimetable {
            val trainInfo = json[infoKey].asJsonObject

            val trainNo        = trainInfo["TrainNo"].asString
            val trainTypeID    = trainInfo["TrainTypeID"]?.asString
            val startStationID = trainInfo["StartingStationID"].asString
            val endStationID   = trainInfo["EndingStationID"].asString
            val serviceDay     = ServiceDay.fromJson(json["ServiceDay"].asJsonObject, false)

            val stopTimeList   = json["StopTimes"].asJsonArray
                .map { StopTime.fromJson(it.asJsonObject) }

            return TrainTimetable(trainNo, trainTypeID, startStationID, endStationID, stopTimeList, serviceDay)
        }
    }
}