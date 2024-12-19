import java.util.ArrayDeque
import java.util.Deque

fun main() {
    val dataPrefix = "Day10"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small(expected 36)=${Day10.part1(inputSmall)}")
    println("part1(expected 531)=${Day10.part1(input)}")
//
    println("part2_small(expected 81)=${Day10.part2(inputSmall)}")
    println("part2(expected 1210)=${Day10.part2(input)}")
}

object Day10 {

    fun part1(input: List<String>): Int {
        val zeroes = findStartingPoints(input)
        return zeroes.sumOf { (row, col) -> count9From(row, col, input) }
    }

    fun part2(input: List<String>): Int {
        val zeroes = findStartingPoints(input)
        return zeroes.sumOf { (row, col) -> count9From(row, col, input, false) }
    }

    private fun findStartingPoints(input: List<String>) = input.flatMapIndexed { row: Int, line: String ->
        line.mapIndexedNotNull { col, ch ->
            if (ch == '0') row to col else null
        }
    }

    fun count9From(row: Int, col: Int, input: List<String>, trackVisited: Boolean = true): Int {
        val stack: Deque<Pair<Int, Int>> = ArrayDeque()
        val visited = mutableSetOf<Pair<Int, Int>>()
        var count = 0
        stack.addLast(row to col)
        while (!stack.isEmpty()) {
            val last = stack.removeLast()
            if (last !in visited) {
                if (trackVisited) visited.add(last)
                val (lastRow, lastCol) = last
                val lastHeight = input[lastRow][lastCol].digitToInt()
                if (lastHeight == 9) {
                    ++count
                } else {
                    listOfNotNull(
                        (lastRow - 1 to lastCol).takeIf { it inside input && input.heightAt(it) == lastHeight + 1 },
                        (lastRow + 1 to lastCol).takeIf { it inside input && input.heightAt(it) == lastHeight + 1 },
                        (lastRow to lastCol - 1).takeIf { it inside input && input.heightAt(it) == lastHeight + 1 },
                        (lastRow to lastCol + 1).takeIf { it inside input && input.heightAt(it) == lastHeight + 1 },
                    ).forEach { stack.addLast(it) }
                }
            }
        }
        return count
    }

    fun List<String>.heightAt(point: Pair<Int, Int>): Int = point.let { (row, col) -> get(row)[col].digitToInt() }

    infix fun Pair<Int, Int>.inside(input: List<String>) =
        first in input.indices && second in input.first().indices


}