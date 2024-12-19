import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO
import kotlin.system.exitProcess

fun main() {
    val dataPrefix = "Day14"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

    println("part1_small(expected )=${Day14.part1(inputSmall)}")
    println("part1(expected 229839456)=${Day14.part1(input)}")
//
//    println("part2_small(expected )=${Day14.part2(inputSmall)}")
    println("part2(expected )=${Day14.part2(input)}")
}

object Day14 {

    const val TILES_ROWS = 103
    const val TILES_COLS = 101

    data class Robot(
        var pos: Pair<Long, Long>,
        val velocity: Pair<Long, Long>,
    ) {
        fun step(n: Int) {
            var row = (n * velocity.first + pos.first) % TILES_ROWS
            var col = (n * velocity.second + pos.second) % TILES_COLS
            if (row < 0) row += TILES_ROWS
            if (col < 0) col += TILES_COLS
            pos = row to col
        }
    }

    fun part1(input: List<String>): Long {
        val robots = input.map { Robot(parsePosition(it), parseVelocity(it)) }
        robots.forEach { it.step(100) }
        return robots.mapNotNull { it.quadrant() }.groupBy { it }.values.map { it.count() }.fold(1, Int::times).toLong()
    }

    fun Robot.quadrant(): Int? {
        val (row, col) = pos
        return when {
            row < TILES_ROWS / 2 && col < TILES_COLS / 2 -> 0
            row < TILES_ROWS / 2 && col > TILES_COLS / 2 -> 1
            row > TILES_ROWS / 2 && col < TILES_COLS / 2 -> 2
            row > TILES_ROWS / 2 && col > TILES_COLS / 2 -> 3
            else -> null
        }
    }

    private fun parsePosition(it: String): Pair<Long, Long> {
        return it.substringAfter("p=").substringBefore(" ").split(",").let { it[1].toLong() to it[0].toLong() }
    }

    private fun parseVelocity(it: String): Pair<Long, Long> {
        return it.substringAfter("v=").trim().split(",").let { it[1].toLong() to it[0].toLong() }
    }


    fun part2(input: List<String>): Int {
        val robots = input.map { Robot(parsePosition(it), parseVelocity(it)) }
//        (1..10_000).forEach {
//            robots.forEach { it.step(1) }
//            val img = BufferedImage(TILES_COLS, TILES_ROWS, BufferedImage.TYPE_INT_RGB)
//            robots.forEach {
//                img.setRGB(it.pos.second.toInt(),it.pos.first.toInt(),255)
//            }
//            ImageIO.write(img, "png", Files.newOutputStream(Paths.get("Day14/$it.png")))
//        }
        robots.forEach { it.step(7138) }
        robots.printTable()
        return 0
    }

    fun List<Robot>.printTable() {
        val robotsPos = map { it.pos }.toSet()
        (0..<TILES_ROWS).map {
            (0..TILES_COLS).map {
                " "
            }.joinToString(separator = "")
        }.mapIndexed { row, rowStr ->
            rowStr.mapIndexed { col, colCh ->
                if (row.toLong() to col.toLong() in robotsPos) "R" else colCh
            }
        }.forEach { it.println() }
    }

}
