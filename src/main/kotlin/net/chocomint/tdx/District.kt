package net.chocomint.tdx

import com.google.gson.JsonParser
import net.chocomint.tdx.district.CityBoundaryShape
import net.chocomint.tdx.district.TownBoundaryShape
import net.chocomint.tdx.district.VillageBoundaryShape
import net.chocomint.tdx.utils.resource

object District {
    fun readCityBoundary(): List<CityBoundaryShape> {
        return JsonParser.parseReader(resource("/tdx/district/shape/City.json"))
            .asJsonArray
            .map { CityBoundaryShape.fromJson(it.asJsonObject) }
    }

    fun readTownBoundary(): List<TownBoundaryShape> {
        return JsonParser.parseReader(resource("/tdx/district/shape/Town.json"))
            .asJsonArray
            .map { TownBoundaryShape.fromJson(it.asJsonObject) }
    }

    fun readVillageBoundary(): List<VillageBoundaryShape> {
        return JsonParser.parseReader(resource("/tdx/district/shape/Village.json"))
            .asJsonArray
            .map { VillageBoundaryShape.fromJson(it.asJsonObject) }
    }
}
