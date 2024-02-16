package net.chocomint.tdx.transportation

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class Fare(
    val ticketType: TicketType,
    val fareClass: FareClass,
    val cabinClass: CabinClass,
    val price: Int
) {
    companion object {
        fun fromJson(json: JsonObject): Fare {
            val ticketType = json["TicketType"].asTicketType
            val fareClass  = json["FareClass"].asFareClass
            val cabinClass = json["CabinClass"].asCabinClass
            val price      = json["Price"].asInt

            return Fare(ticketType, fareClass, cabinClass, price)
        }

        fun fromJsonArray(array: JsonArray): Fare {
            val ticketType = array[0].asTicketType
            val fareClass  = array[1].asFareClass
            val cabinClass = array[2].asCabinClass
            val price      = array[3].asInt

            return Fare(ticketType, fareClass, cabinClass, price)
        }

        @JvmInline
        value class TicketType(val type: Int) {
            val name: String get() = when(type) {
                1 -> "一般票"
                2 -> "來回票"
                3 -> "電子票證"
                4 -> "回數票"
                5 -> "定期票(30天期)"
                6 -> "定期票(60天期)"
                7 -> "早鳥票"
                else -> ""
            }
        }

        @JvmInline
        value class FareClass(val clazz: Int) {
            val name: String get() = when(clazz) {
                1 -> "成人"
                2 -> "學生"
                3 -> "孩童" // 成人/2 四捨五入
                4 -> "敬老" // 成人/2 四捨五入
                5 -> "愛心" // 成人/2 四捨五入
                6 -> "愛心孩童" // 成人/4 四捨五入
                7 -> "愛心優待/愛心陪伴"
                8 -> "團體"
                9 -> "軍警"
                else -> ""
            }
        }

        @JvmInline
        value class CabinClass(val clazz: Int) {
            val name: String get() = when(clazz) {
                1 -> "標準座車廂"
                2 -> "商務座車廂"
                3 -> "自由座車廂"
                else -> ""
            }
        }

        private val JsonElement.asTicketType get() = TicketType(asInt)
        private val JsonElement.asFareClass get() = FareClass(asInt)
        private val JsonElement.asCabinClass get() = CabinClass(asInt)
    }
}
