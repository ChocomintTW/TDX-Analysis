package net.chocomint.tdx.transportation

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.time.DayOfWeek

data class ServiceDay(
    val week: List<Boolean>,
    val holiday: List<Boolean?>?,
    val typhoon: Boolean?
) {
    fun isAvailableOn(day: DayOfWeek): Boolean {
        return week[day.value - 1]
    }

    fun isEveryday(): Boolean {
        return week.all { it }
    }

    fun onlyWeekend(): Boolean {
        return week == "0000011".map { it == '1' }
    }

    override fun toString(): String {
        if (isEveryday())
            return "everyday"
        if (onlyWeekend())
            return "weekend"

        val serviceDays = week.mapIndexed { i, b -> if (b) weekNames[i].take(3) else null }.filterNotNull()
        return "srv. ${serviceDays.joinToString(", ")}"
    }

    companion object {
        private val weekNames = DayOfWeek.entries.map { it.name.lowercase().replaceFirstChar { char -> char.uppercaseChar() } }

        private val holidayNames = listOf("DayBeforeHoliday", "NationalHolidays", "DayAfterHoliday")

        fun fromJson(json: JsonObject, useBoolean: Boolean): ServiceDay {
            val week = weekNames.map { json[it].asGeneralBool(useBoolean) }

            val holiday = if (json.has("NationalHolidays"))
                holidayNames.map { json[it]?.asGeneralBool(useBoolean) }
            else null

            val typhoon = json["TyphoonDay"]?.asInt?.let { it == 1 }

            return ServiceDay(week, holiday, typhoon)
        }

        private fun JsonElement.asGeneralBool(useBoolean: Boolean): Boolean {
            return if (useBoolean) asBoolean else asInt == 1
        }
    }
}
