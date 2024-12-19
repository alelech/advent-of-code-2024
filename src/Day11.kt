fun main() {
    val dataPrefix = "Day11"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small(expected 55312)=${Day11.part1(inputSmall)}")
    println("part1(expected 193607)=${Day11.part1(input)}")
//
    println("part2_small(expected 65601038650482)=${Day11.part2(inputSmall)}")
    println("part2(expected 229557103025807)=${Day11.part2(input)}")
}

object Day11 {

    fun part1(input: List<String>): Long {
        var stones = input.single().split(" ").map { it.toLong() }
        repeat(25) {
            stones = stones.blinkAt()
        }
        return stones.count().toLong()
    }

    fun part2(input: List<String>): Long {
        return input.single().split(" ").map { it.toLong() }.sumOf {
            countSingle(75, it)
        }.also {
            cache.size.println()
        }
    }

    val cache: MutableMap<Pair<Long, Long>, Long> = mutableMapOf()

    fun countSingle(n: Long, stone: Long): Long = if (n > 0) {
        blinkAt(stone).sumOf {
            var cachedCount = cache[it to n - 1]
            if (cachedCount == null) {
                cachedCount = countSingle(n - 1, it)
                cache[it to n - 1] = cachedCount
            }
            cachedCount
        }
    } else {
        1
    }

    fun List<Long>.blinkAt(): List<Long> = flatMap {
        blinkAt(it)
    }

    private fun blinkAt(it: Long): List<Long> = buildList {
        if (it == 0L) {
            add(1)
        } else {
            val strNumber = it.toString()
            if (strNumber.length % 2 == 0) {
                add(strNumber.substring(0..<strNumber.length / 2).toLong())
                add(strNumber.substring(strNumber.length / 2).toLong())
            } else {
                add(it * 2024)
            }
        }
    }

}