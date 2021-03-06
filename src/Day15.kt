import java.util.*

fun main() {
    fun part1(input: Map<Int, Map<Int, Int>>): Int {
        val map = mutableMapOf<Int, MutableMap<Int, Int>>()

        for (x in 0 until input.size) {
            map[x] = mutableMapOf()
            for (y in 0 until input[0]!!.size) {
                val left = map[x - 1]?.get(y) ?: Integer.MAX_VALUE
                val top = map[x]?.get(y - 1) ?: Integer.MAX_VALUE
                map[x]!![y] = if (x == 0 && y == 0) 0 else minOf(left, top) + input[x]!![y]!!
            }
        }

        return map[input.size - 1]!![input[0]!!.size - 1]!!
    }

    fun part2(input: Map<Int, Map<Int, Int>>, multiplier: Int): Int {
        val xSize = input.size
        val ySize = input[0]!!.size

        fun getOriginalValue(x: Int, y: Int): Int {
            val xPart = x / xSize
            val yPart = y / ySize
            val result = input[x.mod(xSize)]!![y.mod(ySize)]!! + xPart + yPart
            return (result - 1).mod(9) + 1
        }

        val distances = mutableMapOf<Pair<Int, Int>, Int>()
        fun getPriority(v: Pair<Int, Int>) = distances[v]!!
        val queue = PriorityQueue<Pair<Int, Int>> { a, b -> getPriority(a) - getPriority(b) }

        for (x in 0 until xSize * multiplier) {
            for (y in 0 until ySize * multiplier) {
                distances[x to y] = Integer.MAX_VALUE
                queue.add(x to y)
            }
        }

        distances[0 to 0] = 0

        fun updateMaybe(u: Pair<Int, Int>, v: Pair<Int, Int>) {
            val pathLength = distances[u]!! + getOriginalValue(v.x, v.y)
            if (pathLength < distances[v]!!) {
                distances[v] = pathLength
                queue.remove(v)
                queue.add(v)
            }
        }

        while (queue.isNotEmpty()) {
            val u = queue.poll()
            if (u == xSize * multiplier - 1 to ySize * multiplier - 1) break

            val left = u.x - 1 to u.y
            val right = u.x + 1 to u.y
            val top = u.x to u.y - 1
            val bottom = u.x to u.y + 1

            if (left.x >= 0 && left.x < xSize * multiplier) updateMaybe(u, left)
            if (right.x < xSize * multiplier) updateMaybe(u, right)
            if (top.y >= 0 && top.y < ySize * multiplier) updateMaybe(u, top)
            if (bottom.y < ySize * multiplier) updateMaybe(u, bottom)
        }

        return distances[xSize * multiplier - 1 to ySize * multiplier - 1]!!
    }

    val testInput = getMappedInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput, multiplier = 1) == 40)
    check(part2(testInput, multiplier = 5) == 315)

    val input = getMappedInput("Day15")
    println(part1(input))
    println(part2(input, multiplier = 5))
}

private fun getMappedInput(name: String): Map<Int, Map<Int, Int>> {
    val lines = readInput(name)
    return buildMap {
        for (x in lines.indices) {
            put(x, buildMap {
                for (y in lines[x].indices) {
                    put(y, Character.getNumericValue(lines[x][y]))
                }
            })
        }
    }
}

val <T, U> Pair<T, U>.x get() = first
val <T, U> Pair<T, U>.y get() = second
