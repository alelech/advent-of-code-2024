import java.util.ArrayDeque
import kotlin.time.measureTime

fun main() {
    val dataPrefix = "Day19"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")

//    val inputSmall2 = reaInput("${dataPrefix}_small2")

//    println("part1_small(expected )=${Day19.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day19.part1(inputSmall2)}")
    println("part1(expected )=${Day19.part1(input)}")
//
//    println("part2_small(expected )=${Day19.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day19.part2(inputSmall2)}")
    println("part2(expected )=${Day19.part2(input)}")
//    println("partX(expected )=${Day19.partX(input)}")
}

object Day19 {

    fun part1(input: List<String>): Int {
        val patterns = input.first().split(",").map { it.trim() }.sortedByDescending { it.length }

        val targets = input.drop(2)

        measureTime { targets.count {
            it.possibilitiesBottomUp(patterns)>0
        } }.inWholeMilliseconds.println()
        return targets.count {
            it.possibilitiesBottomUp(patterns)>0
        }
    }

    fun part2(input: List<String>): Long {
        val patterns = input.first().split(",").map { it.trim() }.sortedByDescending { it.length }
        val targets = input.drop(2)
        return targets.sumOf { it.possibilitiesBottomUp(patterns) }
    }

    private fun String.isPossible(patterns: List<String>): Boolean {
        val stack = ArrayDeque<String>()
        stack.addLast(this)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            patterns.forEach {
                val next = current.removePrefix(it)
                if (next.isEmpty()) return true
                if (next != current && patterns.any { it.length <= next.length }) {
                    stack.addLast(next)
                }
            }
        }
        return false
    }

    private fun String.possibilities(patterns: List<String>, cache: MutableMap<String, Long> = mutableMapOf()): Long {
        if (isEmpty()) return 1L
        return cache[this] ?: patterns.filter { startsWith(it) }.map { removePrefix(it) }.sumOf {
                it.possibilities(patterns, cache)
            }.also {
                cache[this] = it
            }
    }

    private fun String.possibilitiesBottomUp(patterns: List<String>): Long {
        val possible = (1..length).map { 0L }.toMutableList()
        indices.reversed().forEach { lastIdx ->
            val fitPatterns = patterns.filter { it.length <= length - lastIdx }
            val okPatterns = fitPatterns.filter {
                subSequence(lastIdx, lastIdx + it.length) == it
            }
            possible[lastIdx] = okPatterns.sumOf {
                val beforeIdx = lastIdx + it.length
                if (beforeIdx in possible.indices) possible[beforeIdx] else 1
            }
        }
        return possible[0]
    }
}
