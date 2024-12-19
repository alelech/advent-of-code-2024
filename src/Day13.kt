import java.util.*
import java.util.ArrayDeque

fun main() {
    val dataPrefix = "Day13"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small(expected 480)=${Day13.part1(inputSmall)}")
    println("part1(expected 38839)=${Day13.part1(input)}")
//
    println("part2_small(expected 875318608908)=${Day13.part2(inputSmall)}")
    println("part2(expected 75200131617108)=${Day13.part2(input)}")
}

object Day13 {

    data class Cfg(
        val xa: Long,
        val ya: Long,
        val xb: Long,
        val yb: Long,
        val x: Long,
        val y: Long,
    )

    fun List<String>.toCfg(adjustment: Long = 0): Cfg {
        require(size == 3)

        return Cfg(
            xa = get(0).substringAfter("X+").substringBefore(",").toLong(),
            ya = get(0).substringAfter("Y+").toLong(),
            xb = get(1).substringAfter("X+").substringBefore(",").toLong(),
            yb = get(1).substringAfter("Y+").toLong(),
            x = get(2).substringAfter("X=").substringBefore(",").toLong() + adjustment,
            y = get(2).substringAfter("Y=").toLong() + adjustment,
        )
    }

    fun Cfg.solveOrNull(): Pair<Long, Long>? {
        if (xa == ya) return null
        val a = (yb * x - xb * y) / (xa * yb - xb * ya)
        val rema = (yb * x - xb * y) % (xa * yb - xb * ya)
        val b = (ya * x - xa * y) / (xb * ya - xa * yb)
        val remb = (ya * x - xa * y) % (xb * ya - xa * yb)
        return (a to b).takeIf { rema == 0L && remb == 0L }
    }

    fun part1(input: List<String>): Long {
        val configurations = input.chunked(4).map { it.dropLastWhile { it.isBlank() } }.map { it.toCfg() }

        return configurations.mapNotNull { it.solveOrNull() }.sumOf { (a, b) -> a * 3 + b }
    }


    fun part2(input: List<String>): Long {
        val configurations = input.chunked(4).map { it.dropLastWhile { it.isBlank() } }.map { it.toCfg(adjustment = 10000000000000) }

        return configurations.mapNotNull { it.solveOrNull() }.sumOf { (a, b) -> a * 3 + b }
    }
}
