package net.chocomint.tdx.transportation.metro.taipei

import net.chocomint.tdx.utils.Dijkstra
import net.chocomint.tdx.utils.Name

data class TaipeiMetroStation(
    val name: Name,
    val code: List<TaipeiMetroCode>
): Dijkstra.Node {
    private val transferExceptions = listOf(
        TaipeiMetroCode("R22"),
        TaipeiMetroCode("G03"),
        TaipeiMetroCode("O12")
    )

    fun isTransfer(): Boolean {
        return code.size != 1 || transferExceptions.contains(code[0])
    }

    fun isBranchStation(): Boolean {
        return code.size == 1 && !code[0].stationNumber.last().isDigit()
    }

    infix fun isBranchNeighborWith(other: TaipeiMetroStation): Boolean {
        return if (this.code.size != 1 || other.code.size != 1)
            false
        else
            this.code[0] isBranchNeighborWith other.code[0]
    }

    infix fun isNextTo(other: TaipeiMetroStation): Boolean {
        if (isBranchNeighborWith(other))
            return true
        else if (isBranchStation() || other.isBranchStation())
            return false

        val exceptions = listOf(
            "O12" to "O50"
        )
            .map { Pair(TaipeiMetroCode(it.first), TaipeiMetroCode(it.second)) }

        for (ex in exceptions) {
            if (this.code[0] == ex.first && other.code[0] == ex.second)
                return true
        }


        val checkSameLine = this.code.flatMap { c1 ->
            other.code
                .filter { c2 ->
                    c1.line == c2.line
                }
                .map { c2 -> c1 to c2 }
        }

        if (checkSameLine.isEmpty())
            return false

        val pair = checkSameLine[0]
        return pair.first.stationNumber.toInt() - pair.second.stationNumber.toInt() == 1
    }

    infix fun isNeighborWith(other: TaipeiMetroStation): Boolean {
        return this isNextTo other || other isNextTo this
    }

    override fun toString(): String {
        return "$name ${code.joinToString("/")}"
    }
}