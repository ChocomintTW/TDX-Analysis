package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.tra.TRALine
import net.chocomint.tdx.transportation.ShapeUnit
import net.chocomint.tdx.transportation.tra.TrainType
import net.chocomint.tdx.transportation.tra.TRAODFare
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.TrainTimetable
import net.chocomint.tdx.transportation.thsr.THSRODFare
import net.chocomint.tdx.transportation.trtc.StationTimetable
import net.chocomint.tdx.transportation.trtc.TRTCLine
import net.chocomint.tdx.transportation.trtc.TRTCODFare
import net.chocomint.tdx.transportation.trtc.TravelTime
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.io.WKTReader
import java.io.InputStreamReader

fun resource(name: String): InputStreamReader? {
    return object {}.javaClass.getResourceAsStream(name)?.let { InputStreamReader(it) }
}

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
        return JsonParser.parseReader(resource("/tdx/TRA/GeneralTrainTimetable.json"))
            .asJsonObject["TrainTimetables"].asJsonArray
            .map { TrainTimetable.fromJson(it.asJsonObject) }
    }

    fun readODFares(): List<TRAODFare> {
        return JsonParser.parseReader(resource("/tdx/TRA/ODFare-Simple.json"))
            .asJsonArray
            .map { TRAODFare.fromJsonSimple(it.asJsonObject) }
    }
}

object THSR {
    fun readStations(): List<Station> {
        return JsonParser.parseReader(resource("/tdx/THSR/Station.json"))
            .asJsonArray
            .map { Station.fromJson(it.asJsonObject) }
    }

    fun readTimetables(): List<TrainTimetable> {
        return JsonParser.parseReader(resource("/tdx/THSR/GeneralTimetable.json"))
            .asJsonArray
            .map { TrainTimetable.fromJson(it.asJsonObject["GeneralTimetable"].asJsonObject, "GeneralTrainInfo") }
    }

    fun readShape(): LineString {
        return JsonParser.parseReader(resource("/tdx/THSR/Shape.json"))
            .asJsonArray[0].asJsonObject["Geometry"]
            .asString.let { WKTReader().read(it) as LineString }
    }

    fun readODFares(): List<THSRODFare> {
        return JsonParser.parseReader(resource("/tdx/THSR/ODFare.json"))
            .asJsonArray
            .map { THSRODFare.fromJson(it.asJsonObject) }
    }
}

object TRTC {
    fun readStations(): List<Station> {
        return JsonParser.parseReader(resource("/tdx/TRTC/Station.json"))
            .asJsonArray
            .map { Station.fromJson(it.asJsonObject) }
    }

    fun readLines(): List<TRTCLine> {
        return JsonParser.parseReader(resource("/tdx/TRTC/Line.json"))
            .asJsonArray
            .map { TRTCLine.fromJson(it.asJsonObject) }
    }

    fun readShapes(): List<ShapeUnit> {
        return JsonParser.parseReader(resource("/tdx/TRTC/Shape.json"))
            .asJsonArray
            .map { ShapeUnit.fromJson(it.asJsonObject) }
    }

    fun readTravelTimes(): List<TravelTime> {
        return JsonParser.parseReader(resource("/tdx/TRTC/S2STravelTime.json"))
            .asJsonArray
            .flatMap {
                it.asJsonObject["TravelTimes"].asJsonArray
                    .map { e -> TravelTime.fromJson(e.asJsonObject) }
            }
    }

    fun readStationTimetables(): List<StationTimetable> {
        return JsonParser.parseReader(resource("/tdx/TRTC/StationTimeTable.json"))
            .asJsonArray
            .map { StationTimetable.fromJson(it.asJsonObject) }
    }

    fun readODFares(): List<TRTCODFare> {
        return JsonParser.parseReader(resource("/tdx/TRTC/ODFare.json"))
            .asJsonArray
            .map { TRTCODFare.fromJson(it.asJsonObject) }
    }
}