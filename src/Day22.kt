import java.util.*
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sign

fun main() {
    val dataPrefix = "Day22"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")

//    val inputSmall2 = reaInput("${dataPrefix}_small2")

    println("part1_small(expected )=${Day22.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day22.part1(inputSmall2)}")
    println("part1(expected )=${Day22.part1(input)}")
//
    println("part2_small(expected )=${Day22.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day22.part2(inputSmall2)}")
    println("part2(expected )=${Day22.part2(input)}")
//    println("partX(expected )=${Day22.partX(input)}")
}


object Day22 {

    fun part1(input: List<String>): Long {

        return input.sumOf {
            secretSequence(it.toLong()).elementAt(2000)
        }

    }

    fun part2(input: List<String>): Long {

        val chunksCache = input.map { traderSecret ->
            secretSequence(traderSecret.toLong())
                .map { it % 10 }
                .zipWithNext()
                .map {
                    it.second to it.second - it.first
                }
                .take(2001)
                .allFullChunks()
                .groupBy({ it.map { it.second } })
                .mapValues {
                    it.value.map { it.last().first }
                }
                .mapValues { it.value.first() }
                .filterValues { it > 0  }
        }
        val distinctChunks = chunksCache.flatMap { it.keys }.distinct()
        val best = distinctChunks.maxBy { valueAcrossTraders(it, chunksCache) }
        best.println()
//        valueAcrossTraders(listOf(-2, 1, -1, 3), chunksCache).println()
        return valueAcrossTraders(best, chunksCache)
    }

    fun <T> Sequence<T>.allFullChunks():List<List<T>> {
        return toList().let {
            (0..<it.size-4).map { start->
                listOf(
                    it[start],
                    it[start+1],
                    it[start+2],
                    it[start+3],
                )
            }.distinct()
        }
    }

    fun valueAcrossTraders(chunk: List<Long>, chunksCache: List<Map<List<Long>, Long>>): Long = chunksCache.sumOf {
        it[chunk] ?: 0
    }

    fun secretSequence(secret: Long) = generateSequence(secret) {
        ((it xor it * 64) % 16777216).let {
            (it xor it / 32) % 16777216
        }.let {
            (it xor it * 2048) % 16777216
        }
    }

}