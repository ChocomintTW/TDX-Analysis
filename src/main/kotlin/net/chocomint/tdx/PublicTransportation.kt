package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.tra.TRALine
import net.chocomint.tdx.transportation.ShapeUnit
import net.chocomint.tdx.transportation.tra.TrainType
import net.chocomint.tdx.transportation.tra.TRAODFare
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.TrainTimetable
import net.chocomint.tdx.transportation.thsr.THSRODFare
import net.chocomint.tdx.transportation.metro.StationTimetable
import net.chocomint.tdx.transportation.metro.MetroLine
import net.chocomint.tdx.transportation.metro.MetroODFare
import net.chocomint.tdx.transportation.metro.TravelTime
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

object TaipeiMetro {
    @MergedData
    fun readStations(): List<Station> {
        return JsonParser.parseReader(resource("/tdx/TaipeiMetro/Station.json"))
            .asJsonArray
            .map { Station.fromJson(it.asJsonObject) }
    }

    @MergedData
    fun readLines(): List<MetroLine> {
        return JsonParser.parseReader(resource("/tdx/TaipeiMetro/Line.json"))
            .asJsonArray
            .map { MetroLine.fromJson(it.asJsonObject) }
    }

    @MergedData
    fun readTravelTimes(): List<TravelTime> {
        return JsonParser.parseReader(resource("/tdx/TaipeiMetro/S2STravelTime.json"))
            .asJsonArray
            .flatMap {
                it.asJsonObject["TravelTimes"].asJsonArray
                    .map { e -> TravelTime.fromJson(e.asJsonObject) }
            }
    }

    @NoNTMCData
    fun readShapes(): List<ShapeUnit> {
        return JsonParser.parseReader(resource("/tdx/TaipeiMetro/Shape.json"))
            .asJsonArray
            .map { ShapeUnit.fromJson(it.asJsonObject) }
    }

    @NoNTMCData
    fun readStationTimetables(): List<StationTimetable> {
        return JsonParser.parseReader(resource("/tdx/TaipeiMetro/StationTimeTable.json"))
            .asJsonArray
            .map { StationTimetable.fromJson(it.asJsonObject) }
    }

    @MergedData
    @ForMergingData
    fun readODFares(): List<MetroODFare> {
        return JsonParser.parseReader(resource("/tdx/TaipeiMetro/ODFare-TRTC.json"))
            .asJsonArray
            .map { MetroODFare.fromJson(it.asJsonObject) } +
                JsonParser.parseReader(resource("/tdx/TaipeiMetro/ODFare-NTMC.json"))
                    .asJsonArray
                    .map { MetroODFare.fromJson(it.asJsonObject) }
    }

    annotation class MergedData
    annotation class NoNTMCData
    annotation class ForMergingData
}