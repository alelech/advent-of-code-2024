import java.util.LinkedList

fun main() {
    val dataPrefix = "Day05"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1=${Day05.part1(input)}")
    println("part1_small=${Day05.part1(inputSmall)}")
//
    println("part2=${Day05.part2(input)}")
    println("part2_small=${Day05.part2(inputSmall)}")
}

object Day05 {

    fun part1(input: List<String>): Int {
        val afterMap = readAfterMapping(input)
        val realInput = input.slice(input.indexOfFirst { it.isBlank() } + 1..<input.size)
        val intInput = realInput.map { it.split(",").map { it.toInt() } }



        return intInput.sumOf { intList ->
            if (intList.filterIndexed { index, page ->
                    afterMap[page]?.let { after -> intList.slice(0..<index).any { it in after } } == true
                }.isNotEmpty()) 0 else intList[intList.size / 2]
        }
    }

    fun part2(input: List<String>): Int {
        val afterMap = readAfterMapping(input)
        val realInput = input.slice(input.indexOfFirst { it.isBlank() } + 1..<input.size)
        val intInput = realInput.map { it.split(",").map { it.toInt() } }

        val invalidLists = intInput.filter { intList ->
            intList.filterIndexed { index, page ->
                afterMap[page]?.let { after ->
                    intList.slice(0..<index).any { it in after }
                } == true
            }.isNotEmpty()
        }

        val validLists = invalidLists.map { makeListValid(it,afterMap) }

        return validLists.sumOf { it[it.size/2] }
    }

    //--------------------------------------------------------------------

    fun readAfterMapping(input: List<String>): Map<Int, List<Int>> {
        return input.takeWhile { it.isNotBlank() }
            .groupBy({ it.substringBefore('|').toInt() }, { it.substringAfter('|').toInt() })
    }

    fun makeListValid(input: List<Int>, afterMap: Map<Int, List<Int>>): List<Int> {
        val result = mutableListOf<Int>()
        input.forEach {
            val after = afterMap[it] ?: emptyList()
            val target = result.indexOfLast { it !in after } + 1
            result.add(target, it)
        }
        return result
    }

}