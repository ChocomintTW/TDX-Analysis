package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.TrainTimetable
import net.chocomint.tdx.transportation.thsr.THSRODFare
import net.chocomint.tdx.utils.resource
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.io.WKTReader

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

    fun readTimetables(): List<TrainTimetable> {
        return JsonParser.parseReader(resource("/tdx/THSR/GeneralTimetable.json"))
            .asJsonArray
            .map { TrainTimetable.fromJson(it.asJsonObject["GeneralTimetable"].asJsonObject, "GeneralTrainInfo") }
    }

    fun readODFares(): List<THSRODFare> {
        return JsonParser.parseReader(resource("/tdx/THSR/ODFare.json"))
            .asJsonArray
            .map { THSRODFare.fromJson(it.asJsonObject) }
    }

    class Processor(
        val stations: List<Station> = readStations(),
        val shape: LineString = readShape(),
        private val timetables: List<TrainTimetable> = readTimetables(),
        private val odFares: List<THSRODFare> = readODFares()
    ) {
        fun timetableOf(trainNo: Int): TrainTimetable? {
            return timetables.find { it.trainNo == trainNo }
        }
    }
}