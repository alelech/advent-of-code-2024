fun main() {
    val input = readInput("Day03")
    val inputSmall = readInput("Day03_small")

    println("part1=${part1(input)}")
    println("part1_small=${part1(inputSmall)}")

    println("part2=${part2(input)}")
    println("part2_small=${part2(inputSmall)}")
}

val mulRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
val mulOrSwitchRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)")

fun part1(input: List<String>): Int = input.sumOf {
    mulRegex.findAll(it).sumOf {
        val (a, b) = it.destructured
        a.toInt() * b.toInt()
    }
}

fun part2(input: List<String>): Int {
    val allMatches = input.flatMap { mulOrSwitchRegex.findAll(it) }
    var enabled = true
    var mul = 0
    allMatches.forEach {
        if (it.value == "do()") {
            enabled = true
        } else if (it.value == "don't()") {
            enabled = false
        } else {
            if (enabled) {
                val (a, b) = it.destructured
                mul += a.toInt() * b.toInt()
            }
        }
    }
    return mul
}
