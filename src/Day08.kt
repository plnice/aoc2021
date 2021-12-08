fun main() {
    fun part1(input: List<Entry>): Int {
        return input
            .map { it.output }
            .flatten()
            .mapNotNull {
                when (it.length) {
                    2 -> 1
                    4 -> 4
                    3 -> 7
                    7 -> 8
                    else -> null
                }
            }
            .count()
    }

    val testInput = getMappedInput("Day08_test")
    check(part1(testInput) == 26)

    val input = getMappedInput("Day08")
    println(part1(input))
}

private data class Entry(val patterns: List<String>, val output: List<String>)

private fun getMappedInput(name: String) = readInput(name)
    .map { it.split(" | ") }
    .map { (patterns, outputs) -> Entry(patterns.split(" "), outputs.split(" ")) }
