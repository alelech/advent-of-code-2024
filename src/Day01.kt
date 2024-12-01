import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val firsts = input.map { it.substringBefore(" ").toInt() }.sorted()
        val seconds = input.map { it.substringAfterLast(" ").toInt() }.sorted()

        return firsts.zip(seconds).sumOf { (it.first - it.second).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val secondDistro = input.map { it.substringAfterLast(" ").toInt() }.groupingBy { it }.eachCount()
        val firsts = input.map { it.substringBefore(" ").toInt() }

        return firsts.sumOf { secondDistro.getOrDefault(it, 0) * it }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
