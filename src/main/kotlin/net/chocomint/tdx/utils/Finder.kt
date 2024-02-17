package net.chocomint.tdx.utils

import net.chocomint.tdx.transportation.Station

fun List<Station>.findByID(id: String): Station {
    return filter { it.id == id }[0]
}