fun main() {
    val dataPrefix = "Day04"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1=${Day04.part1(input)}")
    println("part1_small=${Day04.part1(inputSmall)}")
//
    println("part2=${Day04.part2(input)}")
    println("part2_small=${Day04.part2(inputSmall2)}")
}

object Day04 {

    fun part1(input: List<String>): Int {
        val horizontal = count(input, ::isXmasHorizontal)
        val vertical = count(input, ::isXmasVertical)
        val diagonalR = count(input, ::isDiagonalRight)
        val diagonalL = count(input, ::isDiagonalLeft)

        return horizontal + vertical + diagonalR + diagonalL
    }

    fun part2(input: List<String>): Int = countX(input)

    //--------------------------------------------------------------------

    fun count(list: List<String>, predicate: (list: List<String>, row: Int, col: Int) -> Boolean) =
        list.mapIndexed { row, rowStr ->
            rowStr.indices.count { col -> (predicate(list, row, col)) }
        }.sum()

    fun countX(list: List<String>) = list.mapIndexed { row, rowStr ->
        rowStr.indices.sumOf { col ->
            if (isDiagonalRight(list, row, col, "MAS") &&
                isDiagonalLeft(list, row, col+2, "MAS")) 1.toInt() else 0
        }
    }.sum()

    fun isXmasHorizontal(list: List<String>, row: Int, col: Int, expectedString: String = "XMAS"): Boolean {
        val remainingCols = list[row].length - col
        if (remainingCols < 4) return false
        val string = list[row].slice(col..col + 3)
        return string == expectedString || string == expectedString.reversed()
    }

    fun isXmasVertical(list: List<String>, row: Int, col: Int, expectedString: String = "XMAS"): Boolean {
        val remainingRows = list.size - row
        if (remainingRows < 4) return false
        val string = list.slice(row..row + 3).map { it[col] }.joinToString(separator = "")
        return string == expectedString || string == expectedString.reversed()
    }

    fun isDiagonalRight(list: List<String>, row: Int, col: Int, expectedString: String = "XMAS"): Boolean {
        val remainingRows = list.size - row
        if (remainingRows < expectedString.length) return false
        val remainingCols = list[row].length - col
        if (remainingCols < expectedString.length) return false
        val string = list.slice(row..<row + expectedString.length).mapIndexed { idx, str -> str[col + idx] }.joinToString(separator = "")
        return expectedString == string || expectedString == string.reversed()
    }

    fun isDiagonalLeft(list: List<String>, row: Int, col: Int, expectedString: String = "XMAS"): Boolean {
        val remainingRows = list.size - row
        if (remainingRows < expectedString.length) return false
        if (col < expectedString.length - 1 || col >= list[row].length) return false
        val string = list.slice(row..<row + expectedString.length).mapIndexed { idx, str -> str[col - idx] }.joinToString(separator = "")
        return expectedString == string || expectedString == string.reversed()
    }


}