fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val stack = mutableListOf<Char>()
            var points = 0L

            for (c in line) {
                if (c in listOf('(', '[', '{', '<')) {
                    stack.add(c)
                } else {
                    val d = stack.removeLast()
                    val isRound = d == '(' && c == ')'
                    val isSquare = d == '[' && c == ']'
                    val isCurly = d == '{' && c == '}'
                    val isAngle = d == '<' && c == '>'
                    if (!isRound && !isSquare && !isCurly && !isAngle) {
                        points = when (c) {
                            ')' -> 3
                            ']' -> 57
                            '}' -> 1197
                            '>' -> 25137
                            else -> throw IllegalArgumentException()
                        }
                        break
                    }
                }
            }

            points
        }
    }

    fun part2(input: List<String>): Long {
        return input
            .map { line ->
                val stack = mutableListOf<Char>()
                var isCorrupted = false

                for (c in line) {
                    if (c in listOf('(', '[', '{', '<')) {
                        stack.add(c)
                    } else {
                        val d = stack.removeLast()
                        val isRound = d == '(' && c == ')'
                        val isSquare = d == '[' && c == ']'
                        val isCurly = d == '{' && c == '}'
                        val isAngle = d == '<' && c == '>'
                        if (!isRound && !isSquare && !isCurly && !isAngle) {
                            isCorrupted = true
                            break
                        }
                    }
                }

                if (!isCorrupted) {
                    stack
                        .reversed()
                        .map {
                            when (it) {
                                '(' -> 1
                                '[' -> 2
                                '{' -> 3
                                '<' -> 4
                                else -> throw IllegalArgumentException()
                            }
                        }
                        .fold(0L) { acc, p -> acc * 5 + p }
                } else {
                    -1
                }
            }
            .filter { it != -1L }
            .sorted()
            .let { it[it.size / 2] }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397L)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
