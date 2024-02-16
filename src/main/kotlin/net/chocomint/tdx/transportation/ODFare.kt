package net.chocomint.tdx.transportation

import com.google.gson.JsonObject

data class ODFare(
    val originStationID: String,
    val destinationStationID: String,
    val trainTypeCode: Int?,
    val travelDistance: Double?,
    val fares: List<Fare>?,
    val price: Int?
) {
    companion object {
        fun fromJson(json: JsonObject): ODFare {
            val originStationID      = json["OriginStationID"].asString
            val destinationStationID = json["DestinationStationID"].asString
            val travelDistance       = json["TravelDistance"]?.asDouble
            val fares = json["Fares"].asJsonArray.map { Fare.fromJson(it.asJsonObject) }

            return ODFare(originStationID, destinationStationID, null, travelDistance, fares, null)
        }

        fun fromJsonSimple(json: JsonObject): ODFare {
            val originStationID      = json["originID"].asString
            val destinationStationID = json["destID"].asString
            val travelDistance       = json["dist"].asDouble
            val trainTypeCode        = json["trainType"].asInt
            val price                = json["price"].asInt

            return ODFare(originStationID, destinationStationID, trainTypeCode, travelDistance, null, price)
        }
    }
}
