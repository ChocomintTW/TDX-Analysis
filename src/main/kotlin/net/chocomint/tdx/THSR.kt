package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.Train
import net.chocomint.tdx.transportation.thsr.THSRODFare
import net.chocomint.tdx.utils.resource
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.io.WKTReader
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object THSR {
    fun readStations(): List<Station> {
        return JsonParser.parseReader(resource("/tdx/THSR/Station.json"))
            .asJsonArray
            .map { Station.fromJson(it.asJsonObject) }
    }

    fun readShape(): LineString {
        return JsonParser.parseReader(resource("/tdx/THSR/Shape.json"))
            .asJsonArray[0].asJsonObject["Geometry"]
            .asString.let { WKTReader().read(it) as LineString }
    }

    fun readTimetables(): List<Train> {
        return JsonParser.parseReader(resource("/tdx/THSR/GeneralTimetable.json"))
            .asJsonArray
            .map { Train.fromJson(it.asJsonObject["GeneralTimetable"].asJsonObject, "GeneralTrainInfo") }
    }

    fun readODFares(): List<THSRODFare> {
        return JsonParser.parseReader(resource("/tdx/THSR/ODFare.json"))
            .asJsonArray
            .map { THSRODFare.fromJson(it.asJsonObject) }
    }

    class Processor(
        private val stations: List<Station> = readStations(),
        private val shape: LineString = readShape(),
        private val trainList: List<Train> = readTimetables(),
        private val odFares: List<THSRODFare> = readODFares()
    ) {
        fun stationByName(name: String): Station? {
            return stations.find { it.name equalString name }
        }

        fun stationByID(id: String): Station? {
            return stations.find { it.id == id }
        }

        fun stationByCode(code: String): Station? {
            return stations.find { it.code!! == code }
        }

        fun trainOf(trainNo: Int): Train? {
            return trainList.find { it.trainNo == trainNo }
        }

        fun search(from: String, to: String): TrainSearchResult {
            val fromStation = stationByName(from)!!
            val toStation   = stationByName(to)!!

            val north = toStation.id.toInt() < fromStation.id.toInt()

            return TrainSearchResult(
                this,
                fromStation,
                toStation,
                trainList
                    .filter { north == (it.trainNo % 2 == 0) } // 北上列車為偶數號，反之亦然
                    .filter { train -> train.stopTimeList.any { it.stationID == fromStation.id } }
                    .filter { train -> train.stopTimeList.any { it.stationID == toStation.id } }
            )
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
            trainList
                .sortedBy { it.stopTimeByID(fromStation.id)!!.departureTime }
                .forEach { train: Train ->
                    println(train.basicInfo(processor))

                    // time
                    val depTime = train.stopTimeByID(fromStation.id)!!.departureTime!!
                    val arrTime = train.stopTimeByID(toStation.id)!!.arrivalTime!!
                    var min = depTime.until(arrTime, ChronoUnit.MINUTES)
                    if (min < 0) min += 24 * 60
                    print("[${min / 60}h ${(min % 60).toString().padStart(2, '0')}m] ")

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