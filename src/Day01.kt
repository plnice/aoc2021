fun main() {
    fun part1(input: List<String>): Int {
        val longs = input.map { it.toLong() }
        return longs
                .zip(longs.drop(1))
                .count { (first, second) -> first < second }
    }

    fun part2(input: List<String>): Int {
        val longs = input.map { it.toLong() }
        val sums = longs
                .zip(longs.drop(1))
                .map { (first, second) -> first + second }
                .zip(longs.drop(2))
                .map { (sum, third) -> sum + third }
        return sums
                .zip(sums.drop(1))
                .count { (first, second) -> first < second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
