fun main() {
    fun part1(input: Map<Int, Map<Int, Int>>, steps: Int = 100): Int {
        var flashes = 0
        val map = input.mutableCopy()

        fun increaseAndReturn(x: Int, y: Int): Int? {
            if (x >= 0 && y >= 0 && x < map.size && y < map[0]!!.size) {
                val previous = map[x]!![y]!!
                if (previous == 0) return null
                val value = (previous + 1).mod(10)
                map[x]!![y] = value
                return value
            }
            return null
        }

        fun flash(x: Int, y: Int) {
            flashes++

            listOf(
                x - 1 to y - 1, x to y - 1, x + 1 to y - 1,
                x - 1 to y, x + 1 to y,
                x - 1 to y + 1, x to y + 1, x + 1 to y + 1
            ).forEach { (nextX, nextY) ->
                if (increaseAndReturn(nextX, nextY) == 0) flash(nextX, nextY)
            }
        }

        repeat(steps) {
            val indices = mutableListOf<Pair<Int, Int>>()

            for (x in 0 until map.size) {
                for (y in 0 until map[x]!!.size) {
                    val value = (map[x]!![y]!! + 1).mod(10)
                    map[x]!![y] = value
                    if (value == 0) indices.add(x to y)
                }
            }

            indices.forEach { (x, y) -> flash(x, y) }
        }

        return flashes
    }

    fun part2(input: Map<Int, Map<Int, Int>>): Int {
        val map = input.mutableCopy()

        fun increaseAndReturn(x: Int, y: Int): Int? {
            if (x >= 0 && y >= 0 && x < map.size && y < map[0]!!.size) {
                val previous = map[x]!![y]!!
                if (previous == 0) return null
                val value = (previous + 1).mod(10)
                map[x]!![y] = value
                return value
            }
            return null
        }

        fun flash(x: Int, y: Int) {
            listOf(
                x - 1 to y - 1, x to y - 1, x + 1 to y - 1,
                x - 1 to y, x + 1 to y,
                x - 1 to y + 1, x to y + 1, x + 1 to y + 1
            ).forEach { (nextX, nextY) ->
                if (increaseAndReturn(nextX, nextY) == 0) flash(nextX, nextY)
            }
        }

        var step = 1

        while (true) {
            val indices = mutableListOf<Pair<Int, Int>>()

            for (x in 0 until map.size) {
                for (y in 0 until map[x]!!.size) {
                    val value = (map[x]!![y]!! + 1).mod(10)
                    map[x]!![y] = value
                    if (value == 0) indices.add(x to y)
                }
            }

            indices.forEach { (x, y) -> flash(x, y) }

            if (map.flatten().distinct().size == 1) return step

            step++
        }
    }

    val exampleInput = getMappedInput("Day11_example")
    part1(exampleInput, steps = 2)

    val testInput = getMappedInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = getMappedInput("Day11")
    println(part1(input))
    println(part2(input))
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

private fun Map<Int, Map<Int, Int>>.mutableCopy(): MutableMap<Int, MutableMap<Int, Int>> {
    return entries.associate { it.key to it.value.toMutableMap() }.toMutableMap()
}

private fun Map<Int, Map<Int, Int>>.flatten(): List<Int> {
    return values.map { it.values }.flatten()
}
