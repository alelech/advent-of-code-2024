import kotlin.math.absoluteValue

fun main() {

    fun String.toListOfInts() = split(" ").map { it.toInt() }

    fun isSafe(intList: List<Int>): Boolean {
        var increasing = true
        var decreasing = true
        var diffOk = true
        intList.zipWithNext().forEach { (a, b) ->
            increasing = increasing && b > a
            decreasing = decreasing && a > b
            val diff = (b - a).absoluteValue
            diffOk = diffOk && diff in 1..3
        }
        return ((increasing || decreasing) && diffOk)
    }

    fun part1(input: List<String>): Int = input.sumOf { line ->
        if (isSafe(line.toListOfInts())) 1.toInt() else 0
    }

    fun generateAllListsSkippingOne(list: List<Int>): Sequence<List<Int>> {
        var k = list.size
        return generateSequence {
            (--k).takeIf { it >= 0 }?.let { except ->
                list.filterIndexed { index, _ -> index != except }
            }
        }
    }

    fun part2(input: List<String>):Int = input.sumOf { line ->
        if(generateAllListsSkippingOne(line.toListOfInts()).any { isSafe(it) }) 1.toInt() else 0
    }

    val input = readInput("Day02")
//    val input_small = readInput("Day02_small")
    part1(input).println()
    part2(input).println()
//    part2(input_small).println()
}
