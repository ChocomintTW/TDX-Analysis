package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.transportation.tra.TRALine
import net.chocomint.tdx.transportation.tra.TRAShape
import net.chocomint.tdx.transportation.tra.TrainType
import net.chocomint.tdx.transportation.ODFare
import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.TrainTimetable
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

    fun readShapes(): List<TRAShape> {
        return JsonParser.parseReader(resource("/tdx/TRA/Shape.json"))
            .asJsonObject["Shapes"].asJsonArray
            .map { TRAShape.fromJson(it.asJsonObject) }
    }

    fun readTimetables(): List<TrainTimetable> {
        return JsonParser.parseReader(resource("/tdx/TRA/GeneralTrainTimetable.json"))
            .asJsonObject["TrainTimetables"].asJsonArray
            .map { TrainTimetable.fromJson(it.asJsonObject) }
    }

    fun readODFares(): List<ODFare> {
        return JsonParser.parseReader(resource("/tdx/TRA/ODFare-Simple.json"))
            .asJsonArray
            .map { ODFare.fromJsonSimple(it.asJsonObject) }
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

    fun readODFares(): List<ODFare> {
        return JsonParser.parseReader(resource("/tdx/THSR/ODFare.json"))
            .asJsonArray
            .map { ODFare.fromJson(it.asJsonObject) }
    }
}