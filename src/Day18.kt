import java.util.*
import java.util.ArrayDeque

fun main() {
    val dataPrefix = "Day18"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")

//    val inputSmall2 = reaInput("${dataPrefix}_small2")

//    println("part1_small(expected )=${Day18.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day18.part1(inputSmall2)}")
//    println("part1(expected )=${Day18.part1(input)}")
//
//    println("part2_small(expected )=${Day18.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day18.part2(inputSmall2)}")
    println("part2(expected )=${Day18.part2(input)}")
//    println("partX(expected )=${Day18.partX(input)}")
}

object Day18 {

    fun part1(input: List<String>): Int {
        //70x70
        //7x7
        val limit = 1024
        val mems = input.take(limit).map { it.split(",").let { it[0].toInt() to it[1].toInt() } }.toSet()

        val right = 70
        val bot = 70

        val start = 0 to 0
        val end = 70 to 70

        val map = (0..right).map { x ->
            (0..bot).joinToString("") { y ->
                if (x to y in mems) "#" else "."
            }
        }

        return dijkstra(map, start, end)
    }

    fun part2(input: List<String>): Int {
        //70x70
        //7x7
        val limit = 1024
        val memsAll = input
//            .take(limit)
            .map { it.split(",").let { it[0].toInt() to it[1].toInt() } }.toSet()
        val mems = memsAll.take(limit)
        val memsRem = memsAll.drop(limit)

        val right = 70
        val bot = 70

        val start = 0 to 0
        val end = 70 to 70

        var map = (0..right).map { x ->
            (0..bot).joinToString("") { y ->
                if (x to y in mems) "#" else "."
            }
        }

        memsRem.forEachIndexed { i, nextMem ->
            "$i/${memsRem.indices.last}".println()
            map = drop(map, nextMem)
            if (end !in dfs(map,start)) {
                nextMem.println()
                return 0
            }
        }

        return -1
    }

    fun drop(map: List<String>, point: Pair<Int, Int>) = map.mapIndexed { x, xStr ->
        xStr.mapIndexed { y, yCh ->
            if (point == x to y) "#" else yCh
        }.joinToString("")
    }

    fun dfs(input: List<String>, start: Pair<Int, Int>): Set<Pair<Int, Int>> = buildSet {
        val stack: Deque<Pair<Int, Int>> = ArrayDeque()
        stack.addLast(start)
        while (!stack.isEmpty()) {
            val last = stack.removeLast()
            if (last !in this) {
                add(last)
                last.neighbours(input).forEach { stack.addLast(it) }
            }
        }
    }

    fun dijkstra(
        input: List<String>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
    ): Int {
        val prevMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        val toProcess: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val distMap = input.flatMapIndexed { x, xStr ->
            xStr.mapIndexedNotNull { y, colCh ->
                if (colCh != '#') {
                    (x to y) to Int.MAX_VALUE
                } else {
                    null
                }
            }
        }.toMap().toMutableMap()
        distMap[start] = 0
        toProcess.add(start)

        while (toProcess.isNotEmpty()) {
            val lastMin = toProcess.minBy { distMap[it]!! }
            toProcess.remove(lastMin)

            val neighbours = lastMin.neighbours(input).filter { it in toProcess }

            neighbours.forEach { next ->
                val dist = distMap[lastMin]!! + 1
                if (dist < distMap[next]!!) {
                    distMap[next] = dist
                    prevMap[next] = lastMin
                    toProcess.add(next)
                }
            }
        }
        return distMap[end]!!
    }

    fun Pair<Int, Int>.neighbours(input: List<String>): List<Pair<Int, Int>> = listOf(
        first to second + 1,
        first - 1 to second,
        first to second - 1,
        first + 1 to second,
    )
        .filter { it.first in input.indices && it.second in input[it.first].indices }
        .filter { input[it.first][it.second] != '#' }


}
