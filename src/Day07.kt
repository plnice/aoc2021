import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<Int>): Int {
        fun fuel(value: Int): Int {
            return input.sumOf { (it - value).absoluteValue }
        }
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!
        return (min..max).minOf { fuel(it) }
    }

    fun part2(input: List<Int>): Int {
        fun fuel(value: Int): Int {
            return input.sumOf {
                var result = 0
                val steps = (it - value).absoluteValue
                var stepValue = 1
                repeat(steps) {
                    result += stepValue
                    stepValue++
                }
                result
            }
        }
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!
        return (min..max).minOf { fuel(it) }
    }

    val testInput = getMappedInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = getMappedInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun getMappedInput(name: String) = readInput(name)
    .first()
    .split(",")
    .map { it.toInt() }
