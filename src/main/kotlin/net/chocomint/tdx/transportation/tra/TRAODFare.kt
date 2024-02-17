package net.chocomint.tdx.transportation.tra

import com.google.gson.JsonObject

data class TRAODFare(
    val originStationID: String,
    val destinationStationID: String,
    val trainTypeCode: Int,
    val travelDistance: Double,
    val price: Int?
) {
    companion object {
        fun fromJsonSimple(json: JsonObject): TRAODFare {
            val originStationID      = json["originID"].asString
            val destinationStationID = json["destID"].asString
            val travelDistance       = json["dist"].asDouble
            val trainTypeCode        = json["trainType"].asInt
            val price                = json["price"].asInt

            return TRAODFare(originStationID, destinationStationID, trainTypeCode, travelDistance, price)
        }
    }
}
