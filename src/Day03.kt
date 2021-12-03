fun main() {
    fun part1(input: List<Int>, length: Int): Int {
        var gamma = 0
        var epsilon = 0

        for (i in 0 until length) {
            val bitsSet = input.sumOf { (it shr i) and 1 }
            if (bitsSet > input.size - bitsSet) gamma = gamma or (1 shl i)
            else epsilon = epsilon or (1 shl i)
        }

        return gamma * epsilon
    }

    fun part2(input: List<Int>, length: Int): Int {
        val ogrList = input.toMutableList()
        val co2SRList = input.toMutableList()

        for (i in length - 1 downTo 0) {
            val bitsSet = ogrList.sumOf { (it shr i) and 1 }
            val mostCommon = if (bitsSet >= ogrList.size - bitsSet) 1 else 0
            ogrList.removeIf { (it shr i) and 1 == if (mostCommon == 1) 0 else 1 }
            if (ogrList.size == 1) break
        }

        for (i in length - 1 downTo 0) {
            val bitsSet = co2SRList.sumOf { (it shr i) and 1 }
            val leastCommon = if (bitsSet < co2SRList.size - bitsSet) 1 else 0
            co2SRList.removeIf { (it shr i) and 1 == if (leastCommon == 1) 0 else 1 }
            if (co2SRList.size == 1) break
        }

        return ogrList.single() * co2SRList.single()
    }

    val testInput = getMappedInput("Day03_test")
    check(part1(testInput.first, testInput.second) == 198)
    check(part2(testInput.first, testInput.second) == 230)

    val input = getMappedInput("Day03")
    println(part1(input.first, input.second))
    println(part2(input.first, input.second))
}

fun getMappedInput(name: String) = readInput(name)
    .map { Integer.parseInt(it, 2) to it.length }
    .let { it.map { it.first } to it.first().second }
