package net.chocomint.tdx.transportation.metro

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.chocomint.tdx.TaipeiMetro
import net.chocomint.tdx.utils.findByID
import net.chocomint.tdx.utils.stationIsNeighbor
import java.io.File

fun main() {
    val exceptions = mapOf(
        "BL07" to 1.28,
        "Y16" to 1.46
    )

    val taipeiMetroODFares = TaipeiMetro.readODFares()
    val stations = TaipeiMetro.readStations()
    val neighborJson = taipeiMetroODFares
        .filter { stationIsNeighbor(it.originStationID, it.destinationStationID) }
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
                addProperty("from", stations.findByID(it.originStationID).name.zh)
                addProperty("to", stations.findByID(it.destinationStationID).name.zh)
                addProperty("distance", it.travelDistance)
            }
        }

    File(System.getProperty("user.dir") + "/src/main/resources/tdx/TaipeiMetro/TaipeiMetroGraph.json")
        .writeText(GsonBuilder().setPrettyPrinting().create().toJson(neighborJson))
}