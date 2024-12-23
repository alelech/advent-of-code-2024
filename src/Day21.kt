import java.util.*
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sign

fun main() {
    val dataPrefix = "Day21"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")

//    val inputSmall2 = reaInput("${dataPrefix}_small2")

//    println("part1_small(expected )=${Day21.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day21.part1(inputSmall2)}")
    println("part1(expected )=${Day21.part1(input)}")
//
//    println("part2_small(expected )=${Day21.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day21.part2(inputSmall2)}")
    println("part2(expected )=${Day21.part2(input)}")
//    println("partX(expected )=${Day21.partX(input)}")
}



object Day21 {
    data class Pos(
        val row: Int,
        val col: Int,
    )

    val keypad = listOf(
        "789",
        "456",
        "123",
        " 0A",
    )
    val keypadABut = Pos(3, 2)

    val robotKeyPad = listOf(
        " ^A",
        "<v>",
    )
    val robotKeypadABut = Pos(0, 2)

    fun part1(input: List<String>): Long {

        return input.sumOf { code ->

            val shortest = shortestToCode(keypad, keypadABut, code).map {
                shortestToCodeLen(robotKeyPad, robotKeypadABut, it,2)
            }.min()
//            shortest.println()
            shortest * code.dropLast(1).toInt()
        }

    }

    fun part2(input: List<String>): Long {

        return input.sumOf { code ->

            val shortest = shortestToCode(keypad, keypadABut, code).map {
                shortestToCodeLen(robotKeyPad, robotKeypadABut, it)
            }.min()
            shortest.println()
            shortest * code.dropLast(1).toInt()
        }

    }

    fun shortestToCode(keypad: List<String>, from: Pos, code: String): Set<String> {
        val codeButtons = code.map {
            keypad.findPointWithValue(it)
        }
        var moves = setOf("")
        buildList {
            add(from)
            addAll(codeButtons)
        }.zipWithNext().forEach { buttons ->
            moves = shortestFromTo(keypad, buttons.first, buttons.second).flatMap { nextPart ->
                moves.map { it + nextPart + "A" }
            }.toSet()
        }
        return moves
    }

    fun shortestToCodeLen(
        keypad: List<String>,
        from: Pos,
        code: String,
        n: Int = 25,
        cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
    ): Long {
//        (" ".repeat(2-n)+"from=${keypad[from.row][from.col]}, code=$code, n=$n").println()
        if (n == 0) {
            return code.length.toLong()/*.also {
                (" ".repeat(2-n)+"min=$it").println()
            }*/
        }
        val codeButtons = code.map {
            keypad.findPointWithValue(it)
        }
        return buildList {
            add(from)
            addAll(codeButtons)
        }.zipWithNext().map { buttons ->
            shortestFromTo(keypad, buttons.first, buttons.second).map { nextPart ->
                cache[nextPart + "A" to n - 1] ?: (
                        shortestToCodeLen(keypad, robotKeypadABut, nextPart + "A", n - 1, cache).also {
                            cache.put(nextPart + "A" to n - 1, it)
                        }
                        )
            }.min()
        }.sum()
//        .also {
//            (" ".repeat(2-n)+"min=$it").println()
//        }
    }

    fun shortestFromTo(input: List<String>, from: Pos, to: Pos): Set<String> {
        val stack = ArrayDeque<List<Pos>>()
        val result = mutableSetOf<List<Pos>>()
        val moves = from.movesTo(to)
        stack.addLast(listOf(from))
        while (stack.isNotEmpty()) {
            val currentMoves = stack.removeLast()
            val current = currentMoves.last()
            if (current == to) {
                result.add(currentMoves)
            } else {
                moves.map {
                    Pos(current.row + it.first, current.col + it.second)
                }.filter { it.isIn(input) && input.valueAt(it) != ' ' }
                    .map { currentMoves.plus(it) }
                    .forEach { stack.addLast(it) }
            }
        }
        return result.map {
            it.zipWithNext().map {
                it.first.dirTo(it.second)
            }.joinToString("")
        }.toSet()
    }

    fun Pos.dirTo(other: Pos): Char {
        val rowDiff = other.row - row
        val colDiff = other.col - col
        if (rowDiff == 1 && colDiff == 0) {
            return 'v'
        }
        if (rowDiff == -1 && colDiff == 0) {
            return '^'
        }
        if (rowDiff == 0 && colDiff == 1) {
            return '>'
        }
        if (rowDiff == 0 && colDiff == -1) {
            return '<'
        }
        throw IllegalArgumentException("$this -> $other")
    }

    private fun Pos.movesTo(to: Pos): Set<Pair<Int, Int>> {
        return (to.row - row to to.col - col).let {
            listOf(
                it.first.sign to 0,
                0 to it.second.sign,
            ).filterNot { it == 0 to 0 }.toSet()
        }
    }

    fun List<String>.allMatching(test: (Pos, Char) -> Boolean): Sequence<Pos> = sequence {
        forEachIndexed { row, rowStr ->
            rowStr.forEachIndexed { col, colCh ->
                Pos(row, col).takeIf { test(it, colCh) }?.let { yield(it) }
            }
        }
    }


    private fun List<String>.findPointWithValue(ch: Char) =
        allMatching { _, inputCh -> inputCh == ch }.single()


    private fun List<String>.valueAt(it: Pos) = this[it.row][it.col]

    private fun Pos.isIn(input: List<String>) =
        row in input.indices && col in input[row].indices
}
