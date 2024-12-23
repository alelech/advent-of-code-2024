import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sign

fun main() {
    val dataPrefix = "Day23"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")

//    val inputSmall2 = reaInput("${dataPrefix}_small2")

    println("part1_small(expected )=${Day23.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day23.part1(inputSmall2)}")
    println("part1(expected )=${Day23.part1(input)}")
//
    println("part2_small(expected )=${Day23.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day23.part2(inputSmall2)}")
    println("part2(expected )=${Day23.part2(input)}")
//    println("partX(expected )=${Day23.partX(input)}")
}


object Day23 {

    fun part1(input: List<String>): Int {
        val adjList = adjList(input)
        return adjList.keys.flatMap {
            triples(it,adjList)
        }.filter {
            it.any { it.startsWith('t') }
        }.distinct().count()

    }

    fun triples(from: String, adjList: Map<String, Set<String>>) = buildSet {
        adjList[from]?.forEach { second ->
            adjList[second]?.forEach { third ->
                if (adjList[third]?.contains(from) == true) {
                    add(setOf(from, second, third))
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val adjList = adjList(input)
        connectedGroups(adjList.keys.toList(),adjList).maxBy { it.size }.sorted().joinToString(",").println()
        return 0
    }

    fun connectedGroups(nodes:List<String>, adjList: Map<String, Set<String>>): Set<Set<String>> {
        val stack = ArrayDeque<List<String>>()
        val visited = mutableSetOf<Set<String>>()
        nodes.forEach { stack.addLast(listOf(it)) }
        val result = mutableSetOf<Set<String>>()
        while (stack.isNotEmpty()) {
            val lastList = stack.removeLast()
//            lastList.println()
            if(lastList.toSet() !in visited) {
                val last = lastList.last()
                val neighbours = adjList[last] ?: emptySet()
                val candidates = neighbours
                    .filter { it !in lastList }
                    .filter { n -> lastList.all { adjList[it]?.contains(n) ?: false } }
                if (candidates.isEmpty()) {
                    result.add(lastList.toSet())
                } else {
                    candidates.forEach { stack.addLast(lastList.plus(it)) }
                }
                visited.add(lastList.toSet())
            }
        }
        return result
    }

    fun adjList(input: List<String>): Map<String, Set<String>> = buildMap<String, MutableSet<String>> {
        input.map {
            it.split('-').let {
                it[0] to it[1]
            }
        }.forEach {
            computeIfAbsent(it.first) { mutableSetOf() }.add(it.second)
            computeIfAbsent(it.second) { mutableSetOf() }.add(it.first)
        }
    }


}