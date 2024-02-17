package net.chocomint.tdx.transportation.thsr

import com.google.gson.JsonObject

data class THSRODFare(
    val originStationID: String,
    val destinationStationID: String,
    val fares: List<THSRFare>
) {
    companion object {
        fun fromJson(json: JsonObject): THSRODFare {
            val originStationID      = json["OriginStationID"].asString
            val destinationStationID = json["DestinationStationID"].asString
            val fares = json["Fares"].asJsonArray.map { THSRFare.fromJson(it.asJsonObject) }

            return THSRODFare(originStationID, destinationStationID, fares)
        }
    }
}
