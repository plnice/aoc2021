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

    fun part2(input: List<Entry>): Long {
        val top = 0
        val middle = 1
        val bottom = 2
        val topLeft = 3
        val topRight = 4
        val bottomLeft = 5
        val bottomRight = 6

        fun String.permutations(prefix: String = ""): List<String> {
            return when {
                isEmpty() -> listOf(prefix)
                else -> mapIndexed { i, c ->
                    (substring(0, i) + substring(i + 1, length)).permutations(prefix + c)
                }.flatten()
            }
        }

        fun String.extend(): List<String> {
            return when (length) {
                2 -> listOf(
                    MutableList(7) { '*' }.also { // 1
                        it[topRight] = this[0]
                        it[bottomRight] = this[1]
                    }.joinToString("")
                )
                3 -> listOf(
                    MutableList(7) { '*' }.also { // 7
                        it[top] = this[0]
                        it[topRight] = this[1]
                        it[bottomRight] = this[2]
                    }.joinToString("")
                )
                4 -> listOf(
                    MutableList(7) { '*' }.also { // 4
                        it[middle] = this[0]
                        it[topLeft] = this[1]
                        it[topRight] = this[2]
                        it[bottomRight] = this[3]
                    }.joinToString("")
                )
                5 -> listOf(
                    MutableList(7) { '*' }.also { // 2
                        it[top] = this[0]
                        it[middle] = this[1]
                        it[bottom] = this[2]
                        it[topRight] = this[3]
                        it[bottomLeft] = this[4]
                    }.joinToString(""),
                    MutableList(7) { '*' }.also { // 3
                        it[top] = this[0]
                        it[middle] = this[1]
                        it[bottom] = this[2]
                        it[topRight] = this[3]
                        it[bottomRight] = this[4]
                    }.joinToString(""),
                    MutableList(7) { '*' }.also { // 5
                        it[top] = this[0]
                        it[middle] = this[1]
                        it[bottom] = this[2]
                        it[topLeft] = this[3]
                        it[bottomRight] = this[4]
                    }.joinToString("")
                )
                6 -> listOf(
                    MutableList(7) { '*' }.also { // 0
                        it[top] = this[0]
                        it[bottom] = this[1]
                        it[topLeft] = this[2]
                        it[topRight] = this[3]
                        it[bottomLeft] = this[4]
                        it[bottomRight] = this[5]
                    }.joinToString(""),
                    MutableList(7) { '*' }.also { // 6
                        it[top] = this[0]
                        it[middle] = this[1]
                        it[bottom] = this[2]
                        it[topLeft] = this[3]
                        it[bottomLeft] = this[4]
                        it[bottomRight] = this[5]
                    }.joinToString(""),
                    MutableList(7) { '*' }.also { // 9
                        it[top] = this[0]
                        it[middle] = this[1]
                        it[bottom] = this[2]
                        it[topLeft] = this[3]
                        it[topRight] = this[4]
                        it[bottomRight] = this[5]
                    }.joinToString(""),
                )
                else -> listOf(this)
            }
        }

        fun Pair<String, String>.match(): Boolean {
            return (first.indices)
                .map { i -> first[i] == second[i] || first[i] == '*' || second[i] == '*' }
                .all { it }
        }

        fun patternsToDigits(patterns: List<String>, configuration: String): Map<Set<Char>, Int> {
            return patterns.map { it.toSet() }.associateWith { pattern ->
                when (pattern.map { configuration.indexOf(it) }.toSet()) {
                    setOf(top, bottom, topLeft, topRight, bottomLeft, bottomRight) -> 0
                    setOf(topRight, bottomRight) -> 1
                    setOf(top, middle, bottom, topRight, bottomLeft) -> 2
                    setOf(top, middle, bottom, topRight, bottomRight) -> 3
                    setOf(middle, topLeft, topRight, bottomRight) -> 4
                    setOf(top, middle, bottom, topLeft, bottomRight) -> 5
                    setOf(top, middle, bottom, topLeft, bottomLeft, bottomRight) -> 6
                    setOf(top, topRight, bottomRight) -> 7
                    setOf(top, middle, bottom, topLeft, topRight, bottomLeft, bottomRight) -> 8
                    setOf(top, middle, bottom, topLeft, topRight, bottomRight) -> 9
                    else -> throw IllegalStateException()
                }
            }
        }

        val allConfigurations = "abcdefg".permutations()

        return input.sumOf { (patterns, output) ->
            val patternPermutations = patterns.map { it.permutations().map { it.extend() }.flatten() }

            val configuration = allConfigurations.first { configuration ->
                patternPermutations.all { permutations -> permutations.any { (configuration to it).match() } }
            }

            val patternsToDigits = patternsToDigits(patterns, configuration)

            output.map { patternsToDigits.getValue(it.toSet()) }.joinToString("").toLong()
        }
    }

    val exampleInput = getMappedInput("Day08_example")
    check(part2(exampleInput) == 5353L)

    val testInput = getMappedInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229L)

    val input = getMappedInput("Day08")
    println(part1(input))
    println(part2(input))
}

private data class Entry(val patterns: List<String>, val output: List<String>)

private fun getMappedInput(name: String) = readInput(name)
    .map { it.split(" | ") }
    .map { (patterns, outputs) -> Entry(patterns.split(" "), outputs.split(" ")) }
