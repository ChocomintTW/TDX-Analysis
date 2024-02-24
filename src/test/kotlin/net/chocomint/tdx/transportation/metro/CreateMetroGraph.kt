package net.chocomint.tdx.transportation.metro

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.chocomint.tdx.TaipeiMetro
import java.io.File

fun main() {
    val exceptions = mapOf(
        "BL07" to 1.28,
        "Y16" to 1.46
    )

    val taipeiMetroODFares = TaipeiMetro.readODFares()
    val processor = TaipeiMetro.Processor()
    val neighborJson = taipeiMetroODFares
        .asSequence()
        .filter { processor.stationByCode(it.destinationStationID)!! isNextTo processor.stationByCode(it.originStationID)!! }
        .map {
            if (it.travelDistance == null) {
                MetroODFare(
                    it.originStationID,
                    it.destinationStationID,
                    exceptions[it.originStationID],
                    it.fares
                )
            } else
                it
        }
        .map {
            JsonObject().apply {
                addProperty("from", processor.stationByCode(it.originStationID)!!.name.zh)
                addProperty("to", processor.stationByCode(it.destinationStationID)!!.name.zh)
                addProperty("distance", it.travelDistance)
            }
        }
        .toSet().toList()
        .toMutableList()

    neighborJson += JsonObject().apply {
        addProperty("from", "新埔")
        addProperty("to", "新埔民生")
        addProperty("distance", 0.0)
    }

    File(System.getProperty("user.dir") + "/src/main/resources/tdx/metro/taipei/TaipeiMetroGraph.json")
        .writeText(GsonBuilder().setPrettyPrinting().create().toJson(neighborJson))
}