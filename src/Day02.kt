import Type.*

fun main() {
    fun part1(input: List<Command>): Long {
        return input
            .fold(Position()) { acc, (type, value) ->
                when (type) {
                    FORWARD -> acc.copy(horizontal = acc.horizontal + value)
                    DOWN -> acc.copy(depth = acc.depth + value)
                    UP -> acc.copy(depth = acc.depth - value)
                }
            }
            .let { it.horizontal * it.depth }
    }

    fun part2(input: List<Command>): Long {
        return input
            .fold(Position()) { acc, (type, value) ->
                when (type) {
                    DOWN -> acc.copy(aim = acc.aim + value)
                    UP -> acc.copy(aim = acc.aim - value)
                    FORWARD -> acc.copy(
                        horizontal = acc.horizontal + value,
                        depth = acc.depth + acc.aim * value
                    )
                }
            }
            .let { it.horizontal * it.depth }
    }

    val testInput = readInput("Day02_test").map(String::toCommand)
    check(part1(testInput) == 150L)
    check(part2(testInput) == 900L)

    val input = readInput("Day02").map(String::toCommand)
    println(part1(input))
    println(part2(input))
}

private enum class Type { FORWARD, DOWN, UP }
private data class Command(val type: Type, val value: Long)

private fun String.toCommand() = split(" ").let { (type, value) ->
    Command(Type.valueOf(type.uppercase()), value.toLong())
}

private data class Position(val horizontal: Long = 0L, val depth: Long = 0L, val aim: Long = 0L)
