import java.util.*
import java.util.ArrayDeque

fun main() {
    val dataPrefix = "Day12"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small(expected 1184)=${Day12.part1(inputSmall)}")
    println("part1(expected 1456082)=${Day12.part1(input)}")
//
    println("part2_small(expected 368)=${Day12.part2(inputSmall)}")
    println("part2(expected 872382)=${Day12.part2(input)}")
}

object Day12 {

    fun part1(input: List<String>): Long {
        val regions = toRegions(input)

        return regions.sumOf { it.priceWithFence(input) }
    }

    private fun Set<Pair<Int, Int>>.priceWithFence(input: List<String>): Long {
        val fenceLen = sumOf { point ->
            val (row, col) = point
            listOfNotNull(
                (row + 1 to col).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) },
                (row - 1 to col).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) },
                (row to col + 1).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) },
                (row to col - 1).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) },
            ).count()
        }
        return fenceLen * size.toLong()
    }

    private fun Set<Pair<Int, Int>>.priceWithSides(input: List<String>): Long {
        val sidesTop = mutableSetOf<Pair<Int, Int>>()
        val sidesLeft = mutableSetOf<Pair<Int, Int>>()
        val sidesRight = mutableSetOf<Pair<Int, Int>>()
        val sidesBottom = mutableSetOf<Pair<Int, Int>>()
        forEach { point ->
            val (row, col) = point
            (row + 1 to col).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) }
                ?.let { sidesBottom.add(row  to col) }
            (row - 1 to col).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) }
                ?.let { sidesTop.add(row to col) }
            (row to col + 1).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) }
                ?.let { sidesRight.add(row to col ) }
            (row to col - 1).takeIf { !it.isInTable(input) || input.at(it) != input.at(point) }
                ?.let { sidesLeft.add(row to col) }
        }
        val topSides = countUniqueSides(sidesTop, { it.first }) { it.second }
        val bottomSides = countUniqueSides(sidesBottom, { it.first }) { it.second }
        val leftSides = countUniqueSides(sidesLeft, { it.second }) { it.first }
        val rightSides = countUniqueSides(sidesRight, { it.second }) { it.first }
        return (topSides +bottomSides+ leftSides +rightSides) * size.toLong()
    }

    private fun countUniqueSides(
        originalSides: Set<Pair<Int, Int>>,
        mainIndex: (Pair<Int, Int>) -> Int,
        subIndex: (Pair<Int, Int>) -> Int
    ) =
        originalSides.groupBy(mainIndex).values.sumOf {
            val sortedSide = it.sortedBy(subIndex)
            var sides = 1
            for (i in 1..<sortedSide.size) {
                if (subIndex(sortedSide[i]) != (subIndex(sortedSide[i - 1]) + 1)) ++sides
            }
            sides
        }

    fun part2(input: List<String>): Long {
        return toRegions(input).sumOf { it.priceWithSides(input) }
    }

    fun toRegions(input: List<String>): List<Set<Pair<Int, Int>>> = buildList {
        val visited = mutableSetOf<Pair<Int, Int>>()
        while (visited.size < input.size * input.first().length) {
            val firstNotVisited = input.flatMapIndexed { row: Int, rowStr: String ->
                rowStr.mapIndexedNotNull { col, colCh ->
                    (row to col).takeIf { it !in visited }
                }
            }.first()
            add(buildSet {
                val stack: Deque<Pair<Int, Int>> = ArrayDeque()
                stack.addLast(firstNotVisited)
                while (!stack.isEmpty()) {
                    val last = stack.removeLast()
                    if (last !in visited) {
                        visited.add(last)
                        add(last)
                        listOfNotNull(
                            (last.first + 1 to last.second).takeIf { it.isInTableAndEqualsTo(input, last) },
                            (last.first - 1 to last.second).takeIf { it.isInTableAndEqualsTo(input, last) },
                            (last.first to last.second + 1).takeIf { it.isInTableAndEqualsTo(input, last) },
                            (last.first to last.second - 1).takeIf { it.isInTableAndEqualsTo(input, last) },
                        ).forEach {
                            stack.addLast(it)
                        }
                    }
                }
            })
        }
    }

    private fun Pair<Int, Int>.isInTableAndEqualsTo(
        input: List<String>,
        expectedValueIndex: Pair<Int, Int>
    ) =
        isInTable(input) && input[first][second] == input[expectedValueIndex.first][expectedValueIndex.second]

    private fun Pair<Int, Int>.isInTable(input: List<String>) =
        first in input.indices && second in input.first().indices

    private fun List<String>.at(point: Pair<Int, Int>) = get(point.first)[point.second]

}
