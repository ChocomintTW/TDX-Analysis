package net.chocomint.tdx.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class Name(val zh: String, val en: String?) {

    override fun toString(): String {
        return zh
    }

    infix fun equalString(string: String): Boolean {
        return zh == string || en == string
    }

    companion object {
        fun fromJson(json: JsonObject): Name {
            val zh = json["Zh_tw"].asString
            val en = json["En"]?.asString
            return Name(zh, en)
        }

        val JsonElement.asName get() = fromJson(asJsonObject)
    }
}