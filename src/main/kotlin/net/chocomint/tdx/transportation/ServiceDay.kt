package net.chocomint.tdx.transportation

import com.google.gson.JsonObject
import java.time.DayOfWeek

data class ServiceDay(
    val week: List<Boolean>,
    val holiday: List<Boolean>?,
    val typhoon: Boolean?
) {
    fun isAvailableOn(day: DayOfWeek): Boolean {
        return week[day.value]
    }

    fun isEveryday(): Boolean {
        return week.all { it }
    }

    fun onlyWeekend(): Boolean {
        return week == "0000011".map { it == '1' }
    }

    companion object {
        private val weekNames = DayOfWeek.entries.map { it.name.lowercase().replaceFirstChar { char -> char.uppercaseChar() } }

        private val holidayNames = listOf("DayBeforeHoliday", "NationalHolidays", "DayAfterHoliday")

        fun fromJson(json: JsonObject): ServiceDay {
            val week = weekNames.map { json[it].asInt == 1 }

            val holiday = if (json.has("NationalHolidays"))
                holidayNames.map { json[it].asInt == 1 }
            else null

            val typhoon = json["TyphoonDay"]?.asInt?.let { it == 1 }

            return ServiceDay(week, holiday, typhoon)
        }
    }
}
