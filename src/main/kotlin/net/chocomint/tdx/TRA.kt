package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.ShapeUnit
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.TrainTimetable
import net.chocomint.tdx.transportation.tra.TRALine
import net.chocomint.tdx.transportation.tra.TRAODFare
import net.chocomint.tdx.transportation.tra.TrainType
import net.chocomint.tdx.utils.Name
import net.chocomint.tdx.utils.removeAddition
import net.chocomint.tdx.utils.resource

object TRA {
    fun readTrainTypes(): List<TrainType> {
        return JsonParser.parseReader(resource("/tdx/TRA/TrainType.json"))
            .asJsonObject["TrainTypes"].asJsonArray
            .map { TrainType.fromJson(it.asJsonObject) }
    }

    fun readStations(): List<Station> {
        return JsonParser.parseReader(resource("/tdx/TRA/Station.json"))
            .asJsonObject["Stations"].asJsonArray
            .map { Station.fromJson(it.asJsonObject) }
    }

    fun readLines(): List<TRALine> {
        return JsonParser.parseReader(resource("/tdx/TRA/Line.json"))
            .asJsonObject["Lines"].asJsonArray
            .map { TRALine.fromJson(it.asJsonObject) }
    }

    fun readShapes(): List<ShapeUnit> {
        return JsonParser.parseReader(resource("/tdx/TRA/Shape.json"))
            .asJsonObject["Shapes"].asJsonArray
            .map { ShapeUnit.fromJson(it.asJsonObject) }
    }

    fun readTimetables(): List<TrainTimetable> {
        // TODO 無環島列車 #1 #2
        return JsonParser.parseReader(resource("/tdx/TRA/GeneralTrainTimetable.json"))
            .asJsonObject["TrainTimetables"].asJsonArray
            .map { TrainTimetable.fromJson(it.asJsonObject) }
    }

    fun readODFares(): List<TRAODFare> {
        return JsonParser.parseReader(resource("/tdx/TRA/ODFare-Simple.json"))
            .asJsonArray
            .map { TRAODFare.fromJsonSimple(it.asJsonObject) }
    }

    class Processor(
        private val trainTypes: List<TrainType> = readTrainTypes(),
        val stations: List<Station> = readStations(),
        private val lines: List<TRALine> = readLines(),
        private val shapes: List<ShapeUnit> = readShapes(),
        private val timetables: List<TrainTimetable> = readTimetables(),
        private val odFares: List<TRAODFare> = readODFares()
    ) {
        fun simpleTrainTypeMap(): Map<Int, Name> {
            return trainTypes.associate { it.code to Name(it.name.zh.removeAddition(), it.name.en) }
        }

        fun trainTypeMapByID(): Map<String, TrainType> {
            return trainTypes.associateBy { it.id }
        }

        fun roundIslandRail(): List<TRALine> {
            return lines.filter { !it.isBranch }
        }

        fun branches(): List<TRALine> {
            return lines.filter { it.isBranch }
        }

        fun timetableOf(trainNo: Int): TrainTimetable? {
            return timetables.find { it.trainNo == trainNo }
        }
    }
}