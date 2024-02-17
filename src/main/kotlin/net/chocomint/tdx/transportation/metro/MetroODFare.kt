package net.chocomint.tdx.transportation.metro

import com.google.gson.JsonObject

data class MetroODFare(
    val originStationID: String,
    val destinationStationID: String,
    val travelDistance: Double?,
    val fares: List<MetroFare>
) {
    override fun toString(): String {
        return "$originStationID -> $destinationStationID | dist=$travelDistance"
    }

    companion object {
        fun fromJson(json: JsonObject): MetroODFare {
            val originStationID      = json["OriginStationID"].asString
            val destinationStationID = json["DestinationStationID"].asString
            val travelDistance       = json["TravelDistance"]?.asDouble
            val fares = json["Fares"].asJsonArray.map { MetroFare.fromJson(it.asJsonObject) }

            return MetroODFare(originStationID, destinationStationID, travelDistance, fares)
        }
    }
}
