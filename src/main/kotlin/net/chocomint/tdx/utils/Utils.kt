package net.chocomint.tdx.utils

import com.google.gson.JsonElement
import java.awt.Color
import java.time.LocalTime

@OptIn(ExperimentalStdlibApi::class)
fun String.toColor(): Color {
    assert(startsWith("#") && length == 7)

    val r = substring(1, 3).hexToInt()
    val g = substring(3, 5).hexToInt()
    val b = substring(5, 7).hexToInt()

    return Color(r, g, b)
}

val JsonElement.asLocalTime: LocalTime get() = LocalTime.parse(asString)