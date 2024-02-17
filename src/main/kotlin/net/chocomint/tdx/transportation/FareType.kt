package net.chocomint.tdx.transportation

import com.google.gson.JsonElement

@JvmInline
value class TicketType(private val type: Int) {
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
value class FareClass(private val clazz: Int) {
    val name: String get() = when(clazz) {
        1 -> "成人"
        2 -> "學生"
        3 -> "孩童"
        4 -> "敬老"
        5 -> "愛心"
        6 -> "愛心孩童"
        7 -> "愛心優待/愛心陪伴"
        8 -> "團體"
        9 -> "軍警"
        else -> ""
    }
}

@JvmInline
value class CabinClass(private val clazz: Int) {
    val name: String get() = when(clazz) {
        1 -> "標準座車廂"
        2 -> "商務座車廂"
        3 -> "自由座車廂"
        else -> ""
    }
}

val JsonElement.asTicketType get() = TicketType(asInt)
val JsonElement.asFareClass get() = FareClass(asInt)
val JsonElement.asCabinClass get() = CabinClass(asInt)