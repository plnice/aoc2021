import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<Line>): Int {
        return input
            .map { (from, to) ->
                buildList {
                    if (from.x == to.x) {
                        val startY = min(from.y, to.y)
                        val endY = max(from.y, to.y)
                        for (y in startY..endY) add(from.x to y)
                    }
                    if (from.y == to.y) {
                        val startX = min(from.x, to.x)
                        val endX = max(from.x, to.x)
                        for (x in startX..endX) add(x to from.y)
                    }
                }
            }
            .flatten()
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 2 }
            .size
    }

    fun part2(input: List<Line>): Int {
        return input
            .map { (from, to) ->
                buildList {
                    if (from.x == to.x) {
                        val startY = min(from.y, to.y)
                        val endY = max(from.y, to.y)
                        for (y in startY..endY) add(from.x to y)
                    } else if (from.y == to.y) {
                        val startX = min(from.x, to.x)
                        val endX = max(from.x, to.x)
                        for (x in startX..endX) add(x to from.y)
                    } else {
                        var x = from.x
                        var y = from.y
                        while (x != to.x && y != to.y) {
                            add(x to y)
                            if (from.x < to.x) x++ else x--
                            if (from.y < to.y) y++ else y--
                        }
                        add(to.x to to.y)
                    }
                }
            }
            .flatten()
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 2 }
            .size
    }

    val testInput = getMappedInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = getMappedInput("Day05")
    println(part1(input))
    println(part2(input))
}

private data class Coors(val x: Int, val y: Int)
private data class Line(val from: Coors, val to: Coors)

private fun String.toCoors() = split(",")
    .let { Coors(it[0].toInt(), it[1].toInt()) }

private fun getMappedInput(name: String) = readInput(name)
    .map { it.split(" -> ") }
    .map { (from, to) -> Line(from.toCoors(), to.toCoors()) }
