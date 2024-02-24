package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.ShapeUnit
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.Train
import net.chocomint.tdx.transportation.tra.TRALine
import net.chocomint.tdx.transportation.tra.TRAODFare
import net.chocomint.tdx.transportation.tra.TrainType
import net.chocomint.tdx.utils.Name
import net.chocomint.tdx.utils.removeAddition
import net.chocomint.tdx.utils.resource
import java.time.DayOfWeek
import java.time.LocalTime

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

    fun readTrainTimetables(): List<Train> {
        // TODO 無環島列車 #1 #2
        return JsonParser.parseReader(resource("/tdx/TRA/GeneralTrainTimetable.json"))
            .asJsonObject["TrainTimetables"].asJsonArray
            .map { Train.fromJson(it.asJsonObject) }
    }

    fun readODFares(): List<TRAODFare> {
        return JsonParser.parseReader(resource("/tdx/TRA/ODFare-Simple.json"))
            .asJsonArray
            .map { TRAODFare.fromJsonSimple(it.asJsonObject) }
    }

    class Processor(
        private val trainTypes: List<TrainType> = readTrainTypes(),
        private val stations: List<Station> = readStations(),
        private val lines: List<TRALine> = readLines(),
        private val shapes: List<ShapeUnit> = readShapes(),
        private val trains: List<Train> = readTrainTimetables(),
        private val odFares: List<TRAODFare> = readODFares()
    ) {
        fun simpleTypeOf(typeID: String): Name? {
            return trainTypes.find { it.id == typeID }?.let { Name(it.name.zh.removeAddition(), it.name.en) }
        }

        fun stationByName(name: String): Station? {
            return stations.find { it.name equalString name }
        }

        fun stationByID(id: String): Station? {
            return stations.find { it.id == id }
        }

        fun roundIslandRail(): List<TRALine> {
            return lines.filter { !it.isBranch }
        }

        fun branches(): List<TRALine> {
            return lines.filter { it.isBranch }
        }

        fun trainOf(trainNo: Int): Train? {
            return trains.find { it.trainNo == trainNo }
        }

        fun stationTimetable(stationName: String): List<TrainAtStation> {
            val station = stations.find { it.name equalString stationName }!!

            return trains
                .map { t -> t.stopTimeList.find { it.stationID == station.id } to t }
                .filter { it.first != null &&
                        it.second.stopTimeList.last().stationID != station.id }
                .sortedBy { it.first!!.departureTime }
                .map {
                    TrainAtStation(
                        it.first!!.departureTime!!,
                        it.second,
                        simpleTypeOf(it.second.trainTypeID!!)!!,
                        stationByID(it.second.terminusStationID)!!.name
                    )
                }
        }

        fun stationTimetable(stationName: String, dayOfWeek: DayOfWeek): List<TrainAtStation> {
            return stationTimetable(stationName).filter { it.train.serviceDay.isAvailableOn(dayOfWeek) }
        }

        fun search(from: String, to: String): TrainSearchResult {
            val fromStation = stationByName(from)!!
            val toStation   = stationByName(to)!!

            return TrainSearchResult(
                this,
                fromStation,
                toStation,
                stationTimetable(from)
                    .filter {
                        val list = it.train.stopTimeList
                        val index = list.indexOfFirst { stopTime -> stopTime.stationID == fromStation.id }

                        list.subList(index, list.size).any { stopTime -> stopTime.stationID == toStation.id }
                    }
                    .map { it.train }
            )
        }
    }

    data class TrainAtStation(
        val departureTime: LocalTime,
        val train: Train,
        val typeName: Name,
        val terminusName: Name
    ) {
        override fun toString(): String {
            return "$departureTime  ${train.trainNo.toString().padEnd(4)} ${typeName.toString().padEnd(7)}  [往 $terminusName]"
        }
    }

    data class TrainSearchResult(
        val processor: Processor,
        val fromStation: Station,
        val toStation: Station,
        val trainList: List<Train>
    ) {
        private fun copy(newTrainList: List<Train>): TrainSearchResult {
            return TrainSearchResult(processor, fromStation, toStation, newTrainList)
        }

        fun filterOn(dayOfWeek: DayOfWeek): TrainSearchResult {
            return copy(trainList.filter { it.serviceDay.isAvailableOn(dayOfWeek) })
        }

        fun filterType(typeName: String): TrainSearchResult {
            return copy(trainList.filter { processor.simpleTypeOf(it.trainTypeID!!)!! equalString typeName })
        }

        fun filterNotType(typeName: String): TrainSearchResult {
            return copy(trainList.filterNot { processor.simpleTypeOf(it.trainTypeID!!)!! equalString typeName })
        }

        fun filterTime(range: ClosedRange<LocalTime>): TrainSearchResult {
            return copy(trainList.filter { train ->
                val time = train.stopTimeList.first { it.stationID == fromStation.id }.departureTime!!
                time in range
            })
        }

        fun filterTime(rangeInHour: IntRange): TrainSearchResult {
            return filterTime(LocalTime.of(rangeInHour.first, 0)..LocalTime.of(rangeInHour.last, 0))
        }

        fun print() {
            trainList.forEach { train: Train ->
                println(train.basicInfo(processor))

                val subStopStr = train.subStopTimeListByID(fromStation.id, toStation.id)
                    .map { "${processor.stationByID(it.stationID)!!.name}(${it.departureTime})" }
                val firstID     = train.stopTimeList.first().stationID
                val terminusID  = train.stopTimeList.last().stationID
                val first       = processor.stationByID(firstID)!!.name
                val terminus    = processor.stationByID(terminusID)!!.name
                val firstStr    = if (firstID == fromStation.id) "" else "$first ... "
                val terminusStr = if (terminusID == toStation.id) "" else " ... $terminus"
                println("$firstStr${subStopStr.joinToString(", ")}$terminusStr")
                println()
            }
        }
    }
}