fun main() {
    val dataPrefix = "Day06"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1=${Day06.part1(input)}")//4778
    println("part1_small=${Day06.part1(inputSmall)}")//41
//
    println("part2=${Day06.part2(input)}")
//    println("part2_small=${Day06.part2(inputSmall)}")//6
}

object Day06 {

    fun part1(input: List<String>): Int {
        val (startRow, startCol) = findStart(input)

        return walk(startRow, startCol, input).first.size
    }


    fun part2(input: List<String>): Int {
        val start = findStart(input)
        val (startRow, startCol) = start

        val (originalSteps, _) = walk(startRow, startCol, input)
        val obstaclePositions = originalSteps.mapIndexedNotNull { index, step ->
            println("progress = ${index + 1}/${originalSteps.size}")
            if (step != start) {
                val newInput = input.toMutableList()
                val str = newInput[step.first]
                newInput[step.first] = str.replaceRange(step.second..<step.second + 1, "#")
                val (_, looped) = walk(startRow, startCol, newInput)
                if (looped) step else null
            } else {
                null
            }
        }
        return obstaclePositions.count()
    }

    //--------------------------------------------------------------------

    private fun findStart(input: List<String>) = input.mapIndexedNotNull { row, str ->
        str.indexOf("^").takeIf { it != -1 }?.let { row to it }
    }.single()

    private fun walk(startRow: Int, startCol: Int, input: List<String>): Pair<Set<Pair<Int, Int>>, Boolean> {
//        var sum = 1//count starting point
        var currentRow = startRow
        var currentCol = startCol
        val steps = mutableSetOf(currentRow to currentCol)
        val stepDirection: MutableMap<Pair<Int, Int>, MutableSet<Int>> = mutableMapOf()

        while (true) {
            //up1
            run {
                val nextObstacleRow = input.slice(0..<currentRow).map { it[currentCol] }.lastIndexOf('#')
//                sum += currentRow - nextObstacleRow - 1
                val upSteps = (nextObstacleRow + 1..currentRow).map { it to currentCol }
                upSteps.forEach { steps.add(it) }
                currentRow = nextObstacleRow + 1
                if (nextObstacleRow == -1) {
                    return steps to false
                }
                if (upSteps.any { stepDirection[it]?.contains(1) == true }) return steps to true
                upSteps.dropLast(1).forEach { stepDirection.computeIfAbsent(it) { mutableSetOf() }.add(1) }
            }
            //right2
            run {
                val nextObstacleCol = input[currentRow].slice(currentCol..<input[currentRow].length).indexOf('#')
                if (nextObstacleCol == -1) {
//                    sum += input[currentRow].length - currentCol - 1
                    val rightSteps = (currentCol..<input[currentRow].length).map { currentRow to it }
                    rightSteps.forEach { steps.add(it) }
                    return steps to false
                } else {
//                    sum += nextObstacleCol - 1
                    val rightSteps = (currentCol..<nextObstacleCol + currentCol).map { currentRow to it }
                    rightSteps.forEach { steps.add(it) }
                    if (rightSteps.any { stepDirection[it]?.contains(2) == true }) return steps to true
                    rightSteps.dropLast(1).forEach { stepDirection.computeIfAbsent(it) { mutableSetOf() }.add(2) }
                }
                currentCol = currentCol + nextObstacleCol - 1
            }
            //down3
            run {
                val nextObstacleRow = input.slice(currentRow..<input.size).map { it[currentCol] }.indexOf('#')
                if (nextObstacleRow == -1) {
//                    sum += input[currentRow].length - 1 - currentRow
                    val downSteps = (currentRow..<input.size).map { it to currentCol }
                    downSteps.forEach { steps.add(it) }
                    return steps to false
                } else {
//                    sum += nextObstacleRow - 1
                    val downSteps = (currentRow..<nextObstacleRow + currentRow).map { it to currentCol }
                    downSteps.forEach { steps.add(it) }
                    if (downSteps.any { stepDirection[it]?.contains(3) == true }) return steps to true
                    downSteps.dropLast(1).forEach { stepDirection.computeIfAbsent(it) { mutableSetOf() }.add(3) }
                }
                currentRow = currentRow + nextObstacleRow - 1
            }
            //left4
            run {
                val nextObstacleCol = input[currentRow].slice(0..<currentCol).lastIndexOf('#')
                val leftSteps = (nextObstacleCol + 1..currentCol).map { currentRow to it }
                leftSteps.forEach { steps.add(it) }
                if (nextObstacleCol == -1) //{
//                    sum += currentCol

                    return steps to false
                else if (leftSteps.any { stepDirection[it]?.contains(4) == true }) return steps to true
                leftSteps.dropLast(1).forEach { stepDirection.computeIfAbsent(it) { mutableSetOf() }.add(4) }
//                } else {
//                    sum += currentCol - nextObstacleCol - 1
//                }
                currentCol = nextObstacleCol + 1
            }
        }
    }
}