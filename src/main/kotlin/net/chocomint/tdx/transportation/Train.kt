package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import net.chocomint.tdx.THSR
import net.chocomint.tdx.TRA
import net.chocomint.tdx.utils.rgbBg
import java.awt.Color

data class Train(
    val trainNo: Int,
    val trainTypeID: String?,
    val startStationID: String,
    val endStationID: String,
    val stopTimeList: List<StopTime>,
    val serviceDay: ServiceDay
) {
    val terminusStationID: String get() = stopTimeList.last().stationID

    fun basicInfo(processor: TRA.Processor): String {
        val type = processor.simpleTypeOf(trainTypeID!!)
        val startName = processor.stationByID(startStationID)!!.name
        val endName = processor.stationByID(endStationID)!!.name

        return "$trainNo $type [$startName -> $endName]  ($serviceDay)"
    }

    fun basicInfo(processor: THSR.Processor): String {
        val startName = processor.stationByID(startStationID)!!.name
        val endName = processor.stationByID(endStationID)!!.name

        return "$trainNo [$startName -> $endName]  ($serviceDay)"
    }

    fun stopTimeByID(id: String): StopTime? {
        return stopTimeList.find { it.stationID == id }
    }

    fun subStopTimeListByID(fromID: String, toID: String): List<StopTime> {
        val fromIndex = stopTimeList.indexOfFirst { it.stationID == fromID }
        val toIndex   = stopTimeList.indexOfFirst { it.stationID == toID }

        return stopTimeList.subList(fromIndex, toIndex + 1)
    }

    fun subStopTimeList(from: String, to: String, processor: TRA.Processor): List<StopTime> {
        val fromStation = processor.stationByName(from)!!
        val toStation   = processor.stationByName(to)!!

        val fromIndex = stopTimeList.indexOfFirst { it.stationID == fromStation.id }
        val toIndex   = stopTimeList.indexOfFirst { it.stationID == toStation.id }

        return stopTimeList.subList(fromIndex, toIndex + 1)
    }

    fun toTRAString(processor: TRA.Processor, vararg highlightStations: String): String {
        val stops = stopTimeList
            .map {
                val station = processor.stationByID(it.stationID)!!.name
                val str = "$station(${it.departureTime})"

                if (highlightStations.contains(station.zh))
                    rgbBg(Color.darkGray, str)
                else
                    str
            }

        return "${basicInfo(processor)}\n" +
                "經由 ${stops.joinToString(", ")}"
    }

    companion object {
        fun fromJson(json: JsonObject, infoKey: String = "TrainInfo"): Train {
            val trainInfo = json[infoKey].asJsonObject

            val trainNo        = trainInfo["TrainNo"].asString.toInt()
            val trainTypeID    = trainInfo["TrainTypeID"]?.asString
            val startStationID = trainInfo["StartingStationID"].asString
            val endStationID   = trainInfo["EndingStationID"].asString
            val serviceDay     = ServiceDay.fromJson(json["ServiceDay"].asJsonObject, false)

            val stopTimeList   = json["StopTimes"].asJsonArray
                .map { StopTime.fromJson(it.asJsonObject) }

            return Train(trainNo, trainTypeID, startStationID, endStationID, stopTimeList, serviceDay)
        }
    }
}