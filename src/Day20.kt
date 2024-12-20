import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.hypot

fun main() {
    val dataPrefix = "Day20"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")

//    val inputSmall2 = reaInput("${dataPrefix}_small2")

//    println("part1_small(expected )=${Day20.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day20.part1(inputSmall2)}")
    println("part1(expected )=${Day20.part1(input)}")
//
//    println("part2_small(expected )=${Day20.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day20.part2(inputSmall2)}")
//    println("part2(expected )=${Day20.part2(input)}")
//    println("partX(expected )=${Day20.partX(input)}")
}

data class Pos(
    val row: Int,
    val col: Int,
)

data class Cheat(
    val start: Pos,
    val end: Pos,
)

object Day20 {

    fun List<String>.saveAsImage(name: String, map: (Pos) -> Color) {
        val img = BufferedImage(size, first().length, BufferedImage.TYPE_INT_RGB)

        onEveryPos { pos, _ ->
            img.setRGB(pos.col, pos.row, map(pos).rgb)
        }
        ImageIO.write(img, "png", Files.newOutputStream(Paths.get("Day20/$name")))
    }

    fun part1(input: List<String>): Int {
        val start = input.findPointWithValue('S')
        val end = input.findPointWithValue('E')


        input.saveAsImage("0.png") { p ->
            if(input.valueAt(p)=='#')
            Color(0, 0, 0)
            else Color(255, 255, 255)
        }

        val (distMap, prevMap) = dijkstra(input, start, end)
        val shortest = buildPath(end, prevMap)
        val shortestLength = shortest.size

        val allCheats = shortest.flatMap { posOnShortest ->
            exploreAllCheats(input, posOnShortest, 20, distMap)
        }.toSet()

        val cheatsMoreThan100 = allCheats.map {
            it to (distMap[it.end]!! - distMap[it.start]!! - it.start.distManTo(it.end))
        }.sortedByDescending { it.second }.groupBy { it.second }.mapValues { it.value.size }
            .filterKeys { it >= 100 }
//            .filterKeys { it==72 }
//            .values.single().map {
//                listOf(it.first.start,it.first.end)
//            }.chunked(10).forEach { chunk ->
//                chunk.map { posList ->
//                    posList.fold(input) { map, pos ->
//                         replace(map,pos,'C')
//                    }
//                }.let {
//                    for (line in input.indices){
//                        it.map { it[line] }.joinToString ( " " ).println()
//                    }
//                }
//            }


        val result = cheatsMoreThan100.values.sum()


        return 0

//        (1..10_000).forEach {
//            robots.forEach { it.step(1) }
//            val img = BufferedImage(TILES_COLS, TILES_ROWS, BufferedImage.TYPE_INT_RGB)
//            robots.forEach {
//                img.setRGB(it.pos.second.toInt(),it.pos.first.toInt(),255)
//            }
//            ImageIO.write(img, "png", Files.newOutputStream(Paths.get("Day14/$it.png")))
//        }

//        val visited = mutableSetOf<Pair<Int, Int>>()
//
//        val allCheats = shortest.flatMapIndexed { idx, point ->
//            "$idx/${shortest.size}".println()
//            point.neighbourWalls(input).mapNotNull { possibleCheat ->
//                possibleCheat.takeIf { it !in visited }?.let { nextPossibleCheat ->
//                    val newMap = replace(input, nextPossibleCheat, '.')
//                    val newShortest = buildPath(end, dijkstra(newMap, start, end).second)
//                    (nextPossibleCheat to shortestLength - newShortest.size).takeIf { it.second >= 100 }?.also {
//                        visited.add(it.first)
//                    }
//                }
//            }
//        }
//
//        return allCheats.size
    }

    fun exploreAllCheats(input: List<String>, pos: Pos, max: Int, distMap: Map<Pos, Int>) = buildSet {
        val cheatStarts = pos.cheatStarts(input)
        cheatStarts.forEach { start ->
            input.allMatching { pos, _ ->
                start.distManTo(pos).let { it in 1..<max }
            }
                .filter { input.valueAt(it) == '.' || input.valueAt(it) == 'E' }
                .filter { distMap[it]!! > distMap[pos]!! }
                .forEach { add(Cheat(pos, it)) }
        }
    }

    fun List<String>.allMatching(test: (Pos, Char) -> Boolean): Sequence<Pos> = sequence {
        forEachIndexed { row, rowStr ->
            rowStr.forEachIndexed { col, colCh ->
                Pos(row, col).takeIf { test(it, colCh) }?.let { yield(it) }
            }
        }
    }

    fun List<String>.onEveryPos(action: (Pos, Char) -> Unit) {
        forEachIndexed { row, rowStr ->
            rowStr.forEachIndexed { col, colCh ->
                action(Pos(row, col), colCh)
            }
        }
    }

    fun Pos.distManTo(other: Pos): Int = abs(other.row - row) + abs(other.col - col)

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun List<String>.findPointWithValue(ch: Char) =
        allMatching { _, inputCh -> inputCh == ch }.single()

    fun Pair<Int, Int>.sub(sub: Pair<Int, Int>) = first - sub.first to second - sub.second

    fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>): Double = b.sub(a).let {
        hypot(it.first.toDouble(), it.second.toDouble())
    }

    fun dijkstra(
        input: List<String>,
        start: Pos,
        end: Pos,
    ): Pair<MutableMap<Pos, Int>, MutableMap<Pos, Pos>> {
        val prevMap = mutableMapOf<Pos, Pos>()
        val distMap = input.flatMapIndexed { row, xStr ->
            xStr.mapIndexedNotNull { col, colCh ->
                if (colCh != '#') {
                    Pos(row, col) to Int.MAX_VALUE
                } else {
                    null
                }
            }
        }.toMap().toMutableMap()
        val toProcess: Queue<Pos> = PriorityQueue(Comparator.comparing({
            distMap[it]!!
        }))
        toProcess.add(start)
        distMap[start] = 0

        while (toProcess.isNotEmpty()) {
            val lastMin = toProcess.remove()
            val neighbours = lastMin.neighbours(input)
            neighbours.forEach { next ->
                val dist = distMap[lastMin]!! + 1
                if (dist < distMap[next]!!) {
                    distMap[next] = dist
                    prevMap[next] = lastMin
                    toProcess.add(next)
                }
            }
        }
        return distMap to prevMap
    }

    fun buildPath(end: Pos, prevMap: Map<Pos, Pos>) = buildList<Pos> {
        addFirst(end)
        var current = end
        while (prevMap.containsKey(current)) {
            addFirst(prevMap[current]!!)
            current = prevMap[current]!!
        }
    }

    fun Pair<Int, Int>.neighbours(input: List<String>): List<Pair<Int, Int>> = listOf(
        first to second + 1,
        first - 1 to second,
        first to second - 1,
        first + 1 to second,
    )
        .filter { it.first in input.indices && it.second in input[it.first].indices }
        .filter { input[it.first][it.second] != '#' }

    fun Pair<Int, Int>.neighbourWalls(input: List<String>): List<Pair<Int, Int>> = listOf(
        first to second + 1,
        first - 1 to second,
        first to second - 1,
        first + 1 to second,
    )
        .filter { it.first in input.indices && it.second in input[it.first].indices }
        .filter { input[it.first][it.second] == '#' }
        .filter { it.first != 0 && it.first != input.indices.last }
        .filter { it.second != 0 && it.second != input[it.first].indices.last }

    fun Pos.cheatStarts(input: List<String>): Set<Pos> = listOf(
        copy(col = col + 1),
        copy(col = col - 1),
        copy(row = row - 1),
        copy(row = row + 1),
    )
        .filter {
            it.isIn(input)
                    && !it.isOnVerticalBorder(input)
                    && !it.isOnHorizontalBorder(input)
        }
        .toSet()

    fun Pos.neighbours(input: List<String>): Set<Pos> = listOf(
        copy(col = col + 1),
        copy(col = col - 1),
        copy(row = row - 1),
        copy(row = row + 1),
    )
        .filter { it.isIn(input) && input.valueAt(it) != '#' }
        .toSet()

    private fun Pos.isOnHorizontalBorder(input: List<String>) = input.indices.let { row == it.first || row == it.last }
    private fun Pos.isOnVerticalBorder(input: List<String>) =
        input[row].indices.let { col == it.first || col == it.last }

    private fun List<String>.valueAt(it: Pos) = this[it.row][it.col]

    private fun Pos.isIn(input: List<String>) =
        row in input.indices && col in input[row].indices


//    fun Iterable<Pair<Int, Int>>.printOnMap(input: List<String>) {
//        input.forEachIndexed { row, rowStr ->
//            rowStr.mapIndexed { col, colCh ->
//                if (any { it.first == row && it.second == col }) 'O' else colCh
//            }.joinToString(separator = "").println()
//        }
//    }


    fun Iterable<Pos>.printOnMap(input: List<String>, replaceCh: Char = 'O') {
        input.forEachIndexed { row, rowStr ->
            rowStr.mapIndexed { col, colCh ->
                if (any { it.row == row && it.col == col }) replaceCh else colCh
            }.joinToString(separator = "").println()
        }
    }

    private fun replace(
        map: List<String>,
        pos: Pair<Int, Int>,
        replacement: Char
    ): List<String> {
        return map.mapIndexed { row, rowStr ->
            rowStr.mapIndexed { col, colCh ->
                if (row == pos.first && col == pos.second) replacement else colCh
            }.joinToString("")
        }
    }

    private fun replace(
        map: List<String>,
        pos: Pos,
        replacement: Char
    ): List<String> {
        return map.mapIndexed { row, rowStr ->
            rowStr.mapIndexed { col, colCh ->
                if (row == pos.row && col == pos.col) replacement else colCh
            }.joinToString("")
        }
    }

}
