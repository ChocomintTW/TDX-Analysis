package net.chocomint.tdx

import com.google.gson.JsonObject

data class Name(val zh: String, val en: String) {

    override fun toString(): String {
        return zh
    }

    companion object {
        fun fromJson(json: JsonObject): Name {
            val zh = json["Zh_tw"].asString
            val en = json["En"].asString
            return Name(zh, en)
        }
    }
}