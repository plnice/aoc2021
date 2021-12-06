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

    data class DayTimer(val day: Int, val timer: Int)

    fun part2v1(input: List<Lanternfish>, days: Int): Long {
        var result = input.size.toLong()

        var creationDates = input
            .map {
                val dates = mutableListOf<Int>()
                var date = it.timer + 1
                while (date <= days) {
                    dates.add(date)
                    date += 7
                }
                dates
            }
            .flatten()

        result += creationDates.size

        while (creationDates.isNotEmpty()) {
            creationDates = creationDates
                .map {
                    val dates = mutableListOf<Int>()
                    var date = it + 9
                    while (date <= days) {
                        dates.add(date)
                        date += 7
                    }
                    dates
                }
                .flatten()
            result += creationDates.size
        }

        return result
    }

    fun part2v2(input: List<Lanternfish>, days: Int): Long {
        fun recursiveSpawns(dayTimer: DayTimer, maxDays: Int): Long {
            val (day, timer) = dayTimer

            if (day == maxDays) return 1

            var result = 0L

            if (timer == 0) {
                result += recursiveSpawns(DayTimer(day + 1, 8), maxDays)
            }

            val next = DayTimer(
                day + 1, when (timer) {
                    8 -> 7
                    7 -> 6
                    else -> (timer - 1).mod(7)
                }
            )

            return result + recursiveSpawns(next, maxDays)
        }

        return input.sumOf { recursiveSpawns(DayTimer(0, it.timer), days) }
    }

    fun part2v3(input: List<Lanternfish>, days: Int): Long {
        var result = 0L
        val dayTimers = input.map { DayTimer(0, it.timer) }.toMutableList()

        while (dayTimers.isNotEmpty()) {
            val (day, timer) = dayTimers.removeFirst()
            if (day == days) {
                result++
                continue
            }
            if (timer == 0) {
                dayTimers.add(DayTimer(day + 1, 8))
            }
            dayTimers.add(
                DayTimer(
                    day + 1, when (timer) {
                        8 -> 7
                        7 -> 6
                        else -> (timer - 1).mod(7)
                    }
                )
            )
        }

        return result
    }

    val testInput = getMappedInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2v1(testInput, days = 80) == 5934L)
    check(part2v2(testInput, days = 80) == 5934L)
    check(part2v3(testInput, days = 80) == 5934L)
//    check(part2(testInput, days = 256) == 26984457539)

    val input = getMappedInput("Day06")
    println(part1(input))
//    println(part2(input, days = 256))
}

private data class Lanternfish(val timer: Int = 8) {
    override fun toString() = timer.toString()
}

private fun getMappedInput(name: String) = readInput(name)
    .first()
    .split(",")
    .map { Lanternfish(it.toInt()) }
