fun main() {
    val dataPrefix = "Day08"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small=${Day08.part1(inputSmall)}")//14
    println("part1=${Day08.part1(input)}")//220
//
    println("part2_small=${Day08.part2(inputSmall)}")//34
    println("part2=${Day08.part2(input)}")//813
}

object Day08 {

    fun part1(input: List<String>): Int {
        val allPoints = input.flatMapIndexed { row, str ->
            str.mapIndexed { col, ch -> Triple(row, col, ch) }
        }

        val maxRowExcl = input.size
        val maxColExcl = input.first().length

        val groupedByLetter = allPoints.filterNot { it.third == '.' }.groupBy({ it.third }, { it.first to it.second })

        return groupedByLetter.values.flatMap { pointsWithSameLetter ->
            generateAllPairs(pointsWithSameLetter).flatMap { (p1, p2) ->
                generateAntiNodes(p1, p2, maxRowExcl, maxColExcl)
            }
        }.distinct().count()
    }

    fun generateAntiNodes(
        p1: Pair<Int, Int>,
        p2: Pair<Int, Int>,
        maxRowExcl: Int,
        maxColExcl: Int
    ): List<Pair<Int, Int>> =
        buildList {
            val rowDiff = p1.first - p2.first
            val colDiff = p1.second - p2.second
            val a1 = p1.first + rowDiff to p1.second + colDiff
            val a2 = p2.first - rowDiff to p2.second - colDiff
            if (a1.inRange(maxRowExcl, maxColExcl)) add(a1)
            if (a2.inRange(maxRowExcl, maxColExcl)) add(a2)
        }

    fun generateAntiNodes2(
        p1: Pair<Int, Int>,
        p2: Pair<Int, Int>,
        maxRowExcl: Int,
        maxColExcl: Int
    ): List<Pair<Int, Int>> =
        buildList {
            val rowDiff = p1.first - p2.first
            val colDiff = p1.second - p2.second
            generateSequence(p1){
                (it.first + rowDiff to it.second + colDiff).takeIf { it.inRange(maxRowExcl, maxColExcl) }
            }.forEach { add(it) }
            generateSequence(p2){
                (it.first - rowDiff to it.second - colDiff).takeIf { it.inRange(maxRowExcl, maxColExcl) }
            }.forEach { add(it) }
        }

    private fun Pair<Int, Int>.inRange(maxRowExcl: Int, maxColExcl: Int) =
        first in 0..<maxRowExcl && second in 0..<maxColExcl

    fun generateAllPairs(input: List<Pair<Int, Int>>) = sequence {
        input.forEach { in1 ->
            input.minus(in1).map { in1 to it }.forEach { yield(it) }
        }
    }

    fun part2(input: List<String>): Int {
        val allPoints = input.flatMapIndexed { row, str ->
            str.mapIndexed { col, ch -> Triple(row, col, ch) }
        }

        val maxRowExcl = input.size
        val maxColExcl = input.first().length

        val groupedByLetter = allPoints.filterNot { it.third == '.' }.groupBy({ it.third }, { it.first to it.second })

        return groupedByLetter.values.flatMap { pointsWithSameLetter ->
            generateAllPairs(pointsWithSameLetter).flatMap { (p1, p2) ->
                generateAntiNodes2(p1, p2, maxRowExcl, maxColExcl)
            }
        }.distinct().count()
    }


    //--------------------------------------------------------------------


}