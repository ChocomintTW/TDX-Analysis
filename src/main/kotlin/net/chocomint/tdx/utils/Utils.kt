package net.chocomint.tdx.utils

import com.google.gson.JsonElement
import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroCode
import java.awt.Color
import java.io.InputStreamReader
import java.time.LocalTime

fun resource(name: String): InputStreamReader? {
    return object {}.javaClass.getResourceAsStream(name)?.let { InputStreamReader(it) }
}

@OptIn(ExperimentalStdlibApi::class)
fun String.toColor(): Color {
    assert(startsWith("#") && length == 7)

    val r = substring(1, 3).hexToInt()
    val g = substring(3, 5).hexToInt()
    val b = substring(5, 7).hexToInt()

    return Color(r, g, b)
}

const val RC = "\u001b[0m"
fun rgbText(color: Color, text: String): String = "\u001b[38;2;${color.red};${color.green};${color.blue}m$text$RC"
fun rgbBg(color: Color, text: String): String = "\u001b[48;2;${color.red};${color.green};${color.blue}m$text$RC"

val JsonElement.asLocalTime: LocalTime get() = LocalTime.parse(asString)

val String.asTaipeiMetroCode: TaipeiMetroCode get() = TaipeiMetroCode(this)

fun <T> Iterable<T>.printAll() {
    forEach(::println)
}

fun <T> Iterable<T>.printAll(title: String) {
    println(title)
    forEach(::println)
}

fun <K, V> Map<out K, V>.printAll() {
    forEach(::println)
}

inline fun <K, V, R : Comparable<R>> Map<out K, V>.printSorted(crossinline selector: (Pair<K, V>) -> R?) {
    toList().sortedBy(selector).printAll()
}

fun String.removeAddition(): String {
    val additionStartIndex = indexOfLast { it == '(' }
    return substring(0, if (additionStartIndex == -1) length else additionStartIndex)
}