import kotlin.time.measureTime

fun main() {
    val dataPrefix = "Day09"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small(should be 1928)=${Day09.part1(inputSmall)}")
    println("part1(should be 6262891638328)=${Day09.part1(input)}")
//
    println("part2_small(should be 2858)=${Day09.part2(inputSmall)}")
    println("part2(should be 6287317016845)=${Day09.part2(input)}")
}

object Day09 {

    fun part1(input: List<String>): Long {
        val fullSpec = input.single().toFullSpec().toMutableList()
//        fullSpec.println()
        var dot = 0
        var block = fullSpec.size - 1
        while (dot < block) {
            while (dot < fullSpec.size && fullSpec[dot] != ".") ++dot
            while (block >= 0 && fullSpec[block] == ".") --block
            if (dot < block) {
                fullSpec[dot++] = fullSpec[block]
                fullSpec[block--] = "."
            }
        }
//        fullSpec.println()
        return fullSpec.checksum()
    }

    fun part2(input: List<String>): Long {
        val fileSpec = input.single().toFileSpec().toMutableList()
//        fileSpec.println()
        val topId = fileSpec.last { it.first != "." }.first.toInt()
        (topId downTo 0).forEach { fileId ->
            val fileIdx = fileSpec.indexOfLast { it.first == fileId.toString() }
            val file = fileSpec[fileIdx]
            val fileLen = file.second
            val firstEmptyIdx =
                fileSpec.slice(0..<fileIdx).indexOfFirst { (block, blockSize) -> block == "." && blockSize >= fileLen }
            if (firstEmptyIdx >= 0) {
                val firstEmpty = fileSpec[firstEmptyIdx]
                val rem = firstEmpty.copy(second = firstEmpty.second - fileLen)
                if (rem.second > 0) {
                    fileSpec[firstEmptyIdx] = rem
                }
                fileSpec[fileIdx] = "." to fileLen
                if (rem.second > 0) {
                    fileSpec.add(firstEmptyIdx, file)
                } else {
                    fileSpec[firstEmptyIdx] = file
                }
            }
        }
//        fileSpec.println()
        val result = fileSpec.flatMap { (block, len) ->
            if (len > 0) (1..len).map { block } else emptyList()
        }
//        result.println()
        return result.checksum()
    }

    private fun List<String>.checksum() = mapIndexed { index, blockId ->
        if (blockId == ".") 0 else blockId.toLong() * index
    }.sum()

    fun String.toFullSpec(): List<String> = flatMapIndexed { idx, ch ->
        if (idx % 2 == 0) {
            (1..ch.digitToInt()).map { (idx / 2).toString() }
        } else {
            (1..ch.digitToInt()).map { "." }
        }
    }

    fun String.toFileSpec(): List<Pair<String, Int>> = mapIndexed { idx, ch ->
        if (idx % 2 == 0) {
            (idx / 2).toString() to ch.digitToInt()
        } else {
            "." to ch.digitToInt()
        }
    }

    //--------------------------------------------------------------------


}