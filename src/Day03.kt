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
        var ogrList = input.toList()
        var co2SRList = input.toList()

        for (i in length - 1 downTo 0) {
            if (ogrList.size > 1) {
                val (bitsSet, bitsUnset) = ogrList.partition { (it shr i) and 1 == 1 }
                ogrList = if (bitsSet.size >= bitsUnset.size) bitsSet else bitsUnset
            }
            if (co2SRList.size > 1) {
                val (bitsSet, bitsUnset) = co2SRList.partition { (it shr i) and 1 == 1 }
                co2SRList = if (bitsSet.size < bitsUnset.size) bitsSet else bitsUnset
            }
            if (ogrList.size == 1 && co2SRList.size == 1) break
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

private fun getMappedInput(name: String) = readInput(name)
    .map { Integer.parseInt(it, 2) to it.length }
    .let { it.map { it.first } to it.first().second }
