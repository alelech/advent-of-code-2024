import kotlin.math.pow

fun main() {
    val dataPrefix = "Day17"
    val input = readInput(dataPrefix)
    val inputSmall = readInput("${dataPrefix}_small")
//    val inputSmall2 = readInput("${dataPrefix}_small2")

//    println("part1_small(expected )=${Day17.part1(inputSmall)}")
//    println("part1_small2(expected )=${Day17.part1(inputSmall2)}")
    println("part1(expected )=${Day17.part1(input)}")
//
//    println("part2_small(expected )=${Day17.part2(inputSmall)}")
//    println("part2_small2(expected )=${Day17.part2(inputSmall2)}")
//    println("part2(expected )=${Day17.part2(input)}")
    println("partX(expected )=${Day17.partX(input)}")
}

object Day17 {


    fun part1(input: List<String>): Int {
        val ra = input[0].substringAfter("A:").trim().toLong()
        val rb = input[1].substringAfter("B:").trim().toLong()
        val rc = input[2].substringAfter("C:").trim().toLong()
        val program = input[4].substringAfter(":").trim().split(',').map { it.toLong() }
        val output = runProgram(ra, rb, rc, program)

        output.joinToString(separator = ",").println()

        return 0
    }

    private fun runProgram(
        ra: Long,
        rb: Long,
        rc: Long,
        program: List<Long>,
        checkExpected: Boolean = false
    ): List<Long> = buildList {
        var ra1 = ra
        var rb1 = rb
        var rc1 = rc
        var ip = 0L

        var currentOut = 0

        do {

            val opcode = program[ip.toInt()]
            ++ip
            val operand = program[ip.toInt()]
            ++ip
            when (opcode) {
                0L -> ra1 /= (pow2(combo(operand, ra1, rb1, rc1)))  //adv - division
                1L -> rb1 = rb1 xor operand //bxl
                2L -> rb1 = combo(operand, ra1, rb1, rc1) % 8//bst
                3L -> if (ra1 != 0L) ip = operand//jnz
                4L -> rb1 = rb1 xor rc1//bxc
                5L -> {
                    val out = combo(operand, ra1, rb1, rc1) % 8
                    if (checkExpected && program[currentOut] != out) {
                        return@buildList
                    }
                    ++currentOut
                    add(out) //out
                }

                6L -> rb1 = ra1 / (pow2(combo(operand, ra1, rb1, rc1)))//bdv
                7L -> rc1 = ra1 / (pow2(combo(operand, ra1, rb1, rc1)))//cdv
            }
        } while (ip in program.indices)
    }

    fun pow2(exp: Long) = 2.0.pow(exp.toInt()).toLong()

    fun combo(operand: Long, ra: Long, rb: Long, rc: Long): Long {
        return when {
            operand < 4 -> operand
            operand == 4L -> ra
            operand == 5L -> rb
            operand == 6L -> rc
            else -> throw IllegalArgumentException()
        }
    }

    fun part2(input: List<String>): Long {
        val ra = input[0].substringAfter("A:").trim().toLong()
        val rb = input[1].substringAfter("B:").trim().toLong()
        val rc = input[2].substringAfter("C:").trim().toLong()
        val program = input[4].substringAfter(":").trim().split(',').map { it.toLong() }

        val start = 2.0.pow((program.size - 1) * 3).toLong() //35184372088832L
        val max = 2.0.pow(program.size * 3).toLong()//281474976710656L

        val pows = (1..14).map { 2.0.pow(it * 3).toLong() }.reversed()

        var ranges = listOf(start..max)

        pows.forEachIndexed { powIdx, pow ->
            ranges = ranges.flatMap { range ->
                range.step(pow).filter {
                    val output = runProgram(it, rb, rc, program)
                    output.takeLast(0 + powIdx) == program.takeLast(0 + powIdx)
                }.map {
                    it..it + pow
                }
            }
        }

//        ranges.map {
//            it to runProgram(it.first(), rb, rc, program).joinToString("")
//        }.forEach { it.println() }

        val solutions2 = ranges.flatMap {
            it.filter {
                program == runProgram(it, rb, rc, program)
            }
        }

        val min = solutions2.min()
        min.let { it to runProgram(it, rb, rc, program).joinToString("") }.println()

        return min

    }

    fun partX(input: List<String>): Long {
        val ra = input[0].substringAfter("A:").trim().toLong()
        val rb = input[1].substringAfter("B:").trim().toLong()
        val rc = input[2].substringAfter("C:").trim().toLong()
        val program = input[4].substringAfter(":").trim().split(',').map { it.toLong() }

//        program.joinToString("").toLong().str8().println()

        (0..7L)
            .map { it * shl8(15) }
            .map { it to runProgram(it, rb, rc, program).asLong() }
//            .forEach { "${it.first} -> ${it.second}".println() }

        val k =10
        listOf(
            //14
//            105553116266496
            //13
//            105553116266496,
//            109951162777600,
//            127543348822016,
//            136339441844224,
            //12
//            105553116266496,
//            107752139522048,
//            108851651149824,
//            109401406963712,
//            109951162777600,
//            136889197658112,
            //11
//            105690555219968,
//            105827994173440,
//            105965433126912,
//            106034152603648,
//            107889578475520,
//            108027017428992,
//            108233175859200,
//            108989090103296,
//            109126529056768,
//            109538845917184,
//            110088601731072,
//            110226040684544,
//            110363479638016,
//            110432199114752,
//            136889197658112,
            //10
            105690555219968,
            105699145154560,
            105733504892928,
            105870943846400,
            106008382799872,
            106016972734464,
            106077102276608,
            106094282145792,
            107889578475520,
            107898168410112,
            107906758344704,
            107932528148480,
            108044197298176,
            108069967101952,
            108250355728384,
            108276125532160,
            108293305401344,
            108997680037888,
            109032039776256,
            109169478729728,
            109581795590144,
            110088601731072,
            110097191665664,
            110131551404032,
            110268990357504,
            110406429310976,
            110415019245568,
            110475148787712,
            110492328656896,
            136932147331072,

        ).flatMap { nn ->
            (0..7L)
                .map { it * shl8(k) + nn }
                .map { it to runProgram(it, rb, rc, program).asLong() }
                .filter { it.second.toString().takeLast(16-k) == program.takeLast(16-k).joinToString("") }
        }.forEach { "${it.first} -> ${it.first.str8()} -> ${it.second}".println() }

//        val k = 175921860444160L
//        (0..7L).map {
//            it to it * shl8(14)+k
//        }.forEach {
//            java.lang.Long.toOctalString(it.second).println()
//        }

        return 0
    }

    fun shl8(n: Int) = 1L shl (n * 3)

    fun Long.str8() = java.lang.Long.toOctalString(this)

    fun List<Long>.asLong() = joinToString("").toLong()

}
