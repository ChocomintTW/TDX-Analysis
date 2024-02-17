package net.chocomint.tdx.transportation.trtc

import com.google.gson.JsonObject

data class TRTCODFare(
    val originStationID: String,
    val destinationStationID: String,
    val travelDistance: Double?,
    val fares: List<TRTCFare>
) {
    companion object {
        fun fromJson(json: JsonObject): TRTCODFare {
            val originStationID      = json["OriginStationID"].asString
            val destinationStationID = json["DestinationStationID"].asString
            val travelDistance       = json["TravelDistance"]?.asDouble
            val fares = json["Fares"].asJsonArray.map { TRTCFare.fromJson(it.asJsonObject) }

            return TRTCODFare(originStationID, destinationStationID, travelDistance, fares)
        }
    }
}
