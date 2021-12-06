fun main() {
    fun part1(input: List<Lanternfish>): Int {
        var fishes = input.toList()
        fun Lanternfish.next() = Lanternfish(
            when (timer) {
                8 -> 7
                7 -> 6
                else -> (timer - 1).mod(7)
            }
        )

        repeat(80) {
            val toAdd = fishes.count { it.timer == 0 }
            fishes = fishes.map { it.next() } + List(toAdd) { Lanternfish() }
        }

        return fishes.size
    }

    fun part2(input: List<Lanternfish>, days: Int): Long {
        val resultMap = mutableMapOf<Int, Long>()

        resultMap[0] = input.size.toLong()
        for (day in 1..days) resultMap[day] = 0L

        input.forEach {
            var i = it.timer + 1
            while (i <= days) {
                resultMap[i] = resultMap.getValue(i) + 1L
                i += 7
            }
        }

        for (day in 1..days) {
            val newFishes = resultMap.getValue(day)
            if (newFishes > 0) {
                var i = day + 9
                while (i <= days) {
                    resultMap[i] = resultMap.getValue(i) + newFishes
                    i += 7
                }
            }
        }

        for (day in 1..days) {
            resultMap[day] = resultMap.getValue(day - 1) + resultMap.getValue(day)
        }

        return resultMap.getValue(days)
    }

    val testInput = getMappedInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput, days = 80) == 5934L)
    check(part2(testInput, days = 256) == 26984457539)

    val input = getMappedInput("Day06")
    println(part1(input))
    println(part2(input, days = 256))
}

private data class Lanternfish(val timer: Int = 8)

private fun getMappedInput(name: String) = readInput(name)
    .first()
    .split(",")
    .map { Lanternfish(it.toInt()) }
