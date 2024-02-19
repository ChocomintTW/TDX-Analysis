package net.chocomint.tdx.transportation.metro.taipei

data class TaipeiMetroCode(private val id: String) {
    val line: String
    val stationNumber: String

    init {
        val des = Regex("([A-Z]+)(.*)").matchEntire(id)!!.destructured
        line          = des.component1()
        stationNumber = des.component2()
    }

    fun asPair(): Pair<String, String> = Pair(line, stationNumber)

    infix fun isBranchNeighborWith(other: TaipeiMetroCode): Boolean {
        return this != other && (id.startsWith(other.id) || other.id.startsWith(id))
    }

    override fun toString(): String {
        return id
    }
}