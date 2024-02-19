package net.chocomint.tdx.utils

import net.chocomint.tdx.transportation.Station
import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroCode
import net.chocomint.tdx.transportation.metro.taipei.TaipeiMetroStation

fun List<Station>.findByID(id: String): Station {
    return find { it.id == id }!!
}

fun List<TaipeiMetroStation>.findByName(name: String): TaipeiMetroStation {
    return find { it.name.zh == name || it.name.en == name }!!
}

fun List<TaipeiMetroStation>.findByCode(code: String): TaipeiMetroStation {
    return find { it.code.contains(TaipeiMetroCode(code)) }!!
}