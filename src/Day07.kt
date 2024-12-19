import kotlin.math.max

fun main() {
    val dataPrefix = "Day07"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small=${Day07.part1(inputSmall)}")//41
    println("part1=${Day07.part1(input)}")//4778
//
    println("part2_small=${Day07.part2(inputSmall)}")//6
    println("part2=${Day07.part2(input)}")
}

object Day07 {

    enum class Op(val op: (String, String) -> String) {
        PLUS({ a, b -> (a.toLong() + b.toLong()).toString() }),
        MULTIPLY({ a, b -> (a.toLong() * b.toLong()).toString() }),
        CONCAT({ a, b -> a + b })
    }

    fun part1(input: List<String>): Long {

        return input.sumOf { line ->
            val sum = line.substringBefore(':').toLong()
            val numbers = line.substringAfter(':').trim().split(' ')
            val opSlot = numbers.size - 1
            val allPossibleOpCombinations = generateAll(opSlot, availableOps = setOf(Op.PLUS, Op.MULTIPLY))
            allPossibleOpCombinations.forEach {
                val result = evaluate(numbers, it)
                if (result == sum) return@sumOf sum
            }
            return@sumOf 0L
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val sum = line.substringBefore(':').toLong()
            val numbers = line.substringAfter(':').trim().split(' ')
            val opSlot = numbers.size - 1
            val allPossibleOpCombinations = generateAll(opSlot, availableOps = setOf(Op.PLUS, Op.MULTIPLY, Op.CONCAT))
            allPossibleOpCombinations.forEach {
                val result = evaluate(numbers, it)
                if (result == sum) return@sumOf sum
            }
            return@sumOf 0L
        }
    }

    fun evaluate(numbers: List<String>, ops: List<Op>): Long {
        check(numbers.size == ops.size + 1)

        return numbers.reduceIndexed { index, acc, next ->
            ops[index - 1].op(acc, next)
        }.toLong()
    }

    fun generateAll(n: Int, current: List<List<Op>> = emptyList(), availableOps: Set<Op>): Sequence<List<Op>> {
        return sequence {
            if (n > 1) {
                generateAll(n - 1, current, availableOps).forEach { list ->
                    availableOps.forEach { yield(list.plus(it)) }
                }
            } else {
                availableOps.forEach { yield(listOf(it)) }
            }
        }
    }


    //--------------------------------------------------------------------


}