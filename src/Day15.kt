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
        val map = mutableMapOf<Int, MutableMap<Int, Int>>()

        val xSize = input.size
        val ySize = input[0]!!.size

        fun getOriginalValue(x: Int, y: Int): Int {
            if (x < 0 || y < 0) return Integer.MAX_VALUE
            val xPart = x / xSize
            val yPart = y / ySize
            val result = input[x.mod(xSize)]!![y.mod(ySize)]!! + xPart + yPart
            return if (result >= 10) result.mod(10) + 1 else result
        }

        for (x in 0 until xSize * multiplier) {
            map[x] = mutableMapOf()
            for (y in 0 until ySize * multiplier) {
                val left = map[x - 1]?.get(y) ?: Integer.MAX_VALUE
                val top = map[x]?.get(y - 1) ?: Integer.MAX_VALUE
                map[x]!![y] = if (x == 0 && y == 0) 0 else minOf(left, top) + getOriginalValue(x, y)
            }
        }

        return map[xSize * multiplier - 1]!![ySize * multiplier - 1]!!
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
