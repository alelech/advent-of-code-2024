import java.util.ArrayDeque
import java.util.Comparator
import java.util.Deque
import java.util.PriorityQueue
import java.util.Queue
import kotlin.math.abs
import kotlin.math.hypot

fun main() {
    val dataPrefix = "Day16"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
    val inputSmall2 = readInput("${dataPrefix}_small2")

//    println("part1_small(expected 7036)=${Day16.part1(inputSmall)}")
//    println("part1_small2(expected 11048)=${Day16.part1(inputSmall2)}")
//    println("part1(expected 135512)=${Day16.part1(input)}")
//
    println("part2_small(expected 45)=${Day16.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day16.part2(inputSmall2)}")
//    println("part2(expected 541)=${Day16.part2(input)}")
}

object Day16 {

    data class Node(
        val row: Int,
        val col: Int,
    )

    fun part1(input: List<String>): Int {
        val start = input.findStart()
        val end = input.findEnd()
        "start=$start".println()
        "ends=$end".println()

        return dijkstra2(input, start, end)
    }

    fun part2(input: List<String>): Int {
        val start = input.findStart()
        val end = input.findEnd()
        "start=$start".println()
        "ends=$end".println()

        return dijkstra2(input, start, end, returnCount = true)
    }

    fun List<String>.findStart() = findNodeWithValue('S')

    fun List<String>.findEnd() = findNodeWithValue('E')

    private fun List<String>.findNodeWithValue(ch: Char): Node {
        forEachIndexed { row, rowStr ->
            rowStr.forEachIndexed { col, colCh ->
                if (colCh == ch) return Node(row, col)
            }
        }
        throw IllegalArgumentException()
    }

    private fun List<String>.findPointWithValue(ch: Char): Pair<Int,Int> {
        forEachIndexed { row, rowStr ->
            rowStr.forEachIndexed { col, colCh ->
                if (colCh == ch) return row to col
            }
        }
        throw IllegalArgumentException()
    }

    fun dijkstra2(
        input: List<String>,
        start: Node,
        end: Node,
        returnCount:Boolean=false,
    ): Int {
        val prevMap = mutableMapOf<Node, Node>()
        val unvisited = mutableSetOf <Node>()
        val distMap = input.flatMapIndexed { row, rowStr ->
            rowStr.mapIndexedNotNull { col, colCh ->
                if (colCh != '#') {
                    Node(row, col).also { unvisited.add(it) } to Int.MAX_VALUE
                } else {
                    null
                }
            }
        }.toMap().toMutableMap()

        val queue: Queue<Node> = PriorityQueue(Comparator.comparing({distMap[it]!!}))
        distMap[start] = 0
        queue.add(start)
        unvisited.remove(start)

        val shortestPaths = mutableSetOf<Node>()
        while (queue.isNotEmpty()) {
            val lastMin = queue.remove()
            val neighbours = lastMin.neighbours(input)

            neighbours.forEach { next ->
                val dist = dist(lastMin, next, distMap, prevMap)
                if (dist <= distMap[next]!!) {
                    distMap[next] = dist
                    prevMap[next] = lastMin
                    queue.add(next)
                    if(next==end){
                        buildPath(end,prevMap).forEach { shortestPaths.add(it) }
                    }
                }
            }
        }
        return if (returnCount) shortestPaths.size else distMap[end]!!
    }

    fun dijkstra(
        input: List<String>,
        start: Node,
    ): Pair<Map<Node, Node>, Map<Node, Int>> {
        val prevMap = mutableMapOf<Node, Node>()
        prevMap[start] = Node(start.row, start.col - 1)//start facing east
        val toProcess: MutableSet<Node> = mutableSetOf()
        val distMap = input.flatMapIndexed { row, rowStr ->
            rowStr.mapIndexedNotNull { col, colCh ->
                if (colCh != '#') {
                    Node(row, col).also { toProcess.add(it) } to Int.MAX_VALUE
                } else {
                    null
                }
            }
        }.toMap().toMutableMap()
        distMap[start] = 0

        while (toProcess.isNotEmpty()) {
            val lastMin = toProcess.minBy { distMap[it]!! }
            toProcess.remove(lastMin)

            val neighbours = lastMin.neighbours(input).filter { it in toProcess }

            neighbours.forEach { next ->
                val dist = dist(lastMin, next, distMap, prevMap)
                if (dist < distMap[next]!!) {
                    distMap[next] = dist
                    prevMap[next] = lastMin
                    toProcess.add(next)
                }
            }
        }
        return prevMap to distMap
    }

    private fun dist(
        prev: Node,
        next: Node,
        distMap: Map<Node, Int>,
        prevMap: Map<Node, Node>
    ) = distMap[prev]!! + 1 + (wasRotated(prevMap[prev] ?: prev.copy(col = prev.col - 1), prev, next) * 1000)

    fun Map<Pair<Int, Int>, Int>.toCsv(input: List<String>) = input.mapIndexed { row, rowStr ->
        rowStr.mapIndexed { col, colCh ->
            get(row to col) ?: Int.MAX_VALUE
        }
    }.joinToString(separator = "\r\n")

    private fun wasRotated(prev: Pair<Int, Int>, current: Pair<Int, Int>, next: Pair<Int, Int>): Int {
        val sameRow = prev.first == current.first && current.first == next.first
        val sameCol = prev.second == current.second && current.second == next.second
        return if (sameRow || sameCol) 0 else 1
    }

    private fun wasRotated(prev: Node, current: Node, next: Node): Int {
        val sameRow = prev.row == current.row && current.row == next.row
        val sameCol = prev.col == current.col && current.col == next.col
        return if (sameRow || sameCol) 0 else 1
    }

    fun Node.neighbours(input: List<String>): List<Node> = (row to col).neighbours(input).map { nei ->
        Node(nei.first, nei.second)
    }

    fun Pair<Int, Int>.neighbours(input: List<String>): List<Pair<Int, Int>> = listOf(
        first to second + 1,
        first - 1 to second,
        first to second - 1,
        first + 1 to second,
    )
        .filter { it.first in input.indices && it.second in input[it.first].indices }
        .filter { input[it.first][it.second] != '#' }

    fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>): Double = b.sub(a).let {
        hypot(it.first.toDouble(), it.second.toDouble())
    }

    fun distN(a: Node, b: Node): Int = abs(b.row-a.row)+abs(b.col-a.col)


    fun buildPath(end: Pair<Int, Int>, prevMap: Map<Pair<Int, Int>, Pair<Int, Int>>) = buildList<Pair<Int, Int>> {
        addFirst(end)
        var current = end
        while (prevMap.containsKey(current)) {
            addFirst(prevMap[current]!!)
            current = prevMap[current]!!
        }
        removeFirst()
    }


    fun buildPath(end: Node, prevMap: Map<Node, Node>) = buildList<Node> {
        addFirst(end)
        var current = end
        while (prevMap.containsKey(current)) {
            addFirst(prevMap[current]!!)
            current = prevMap[current]!!
        }
    }


//    fun List<Node>.pathLen2(): Int {
//        return zipWithNext().sumOf { (last, current) ->
//            if (current.incomingDir == last.incomingDir) {
//                1.toInt()
//            } else {
//                1001
//            }
//        }
//    }

    fun List<Pair<Int, Int>>.pathLen(): Int {
        var direction = 0 to 1//east
        var sum = 0
        zipWithNext().forEach { (last, current) ->
            if (current.sub(direction) == last) {
                sum += 1
            } else {
                sum += 1001
                direction = current.sub(last)
            }
        }
        return sum
    }

    fun Pair<Int, Int>.sub(sub: Pair<Int, Int>) = first - sub.first to second - sub.second

    fun List<Node>.printOnMap(input: List<String>) {
        input.forEachIndexed { row, rowStr ->
            rowStr.mapIndexed { col, colCh ->
                if (any { it.row == row && it.col == col }) 'O' else colCh
            }.joinToString(separator = "").println()
        }
    }

}
