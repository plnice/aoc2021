fun main() {
    fun part1(input: Map<Int, Map<Int, Int>>): Int {
        val lowPoints = mutableListOf<Int>()

        for (x in 0 until input.size) {
            for (y in 0 until input[x]!!.size) {
                val value = input[x]!![y]!!
                val up = input[x]!![y - 1]
                val down = input[x]!![y + 1]
                val left = input[x - 1]?.get(y)
                val right = input[x + 1]?.get(y)

                if (listOfNotNull(up, down, left, right).all { it > value }) {
                    lowPoints.add(value)
                }
            }
        }

        return lowPoints.sumOf { it + 1 }
    }

    fun part2(input: Map<Int, Map<Int, Int>>): Int {
        val basinSizes = mutableListOf<Int>()

        fun basinIndices(x: Int, y: Int): Set<Pair<Int, Int>> {
            val value = input[x]!![y]!!
            val up = input[x]!![y - 1] ?: 9
            val down = input[x]!![y + 1] ?: 9
            val left = input[x - 1]?.get(y) ?: 9
            val right = input[x + 1]?.get(y) ?: 9

            val indices = mutableSetOf(x to y)

            if (up != 9 && up - value >= 1) {
                indices.add(x to (y - 1))
                indices.addAll(basinIndices(x, y - 1))
            }
            if (down != 9 && down - value >= 1) {
                indices.add(x to (y + 1))
                indices.addAll(basinIndices(x, y + 1))
            }
            if (left != 9 && left - value >= 1) {
                indices.add((x - 1) to y)
                indices.addAll(basinIndices(x - 1, y))
            }
            if (right != 9 && right - value >= 1) {
                indices.add((x + 1) to y)
                indices.addAll(basinIndices(x + 1, y))
            }

            return indices
        }

        for (x in 0 until input.size) {
            for (y in 0 until input[x]!!.size) {
                val value = input[x]!![y]!!
                val up = input[x]!![y - 1]
                val down = input[x]!![y + 1]
                val left = input[x - 1]?.get(y)
                val right = input[x + 1]?.get(y)

                if (listOfNotNull(up, down, left, right).all { it > value }) {
                    basinSizes += basinIndices(x, y).size
                }
            }
        }

        return basinSizes
            .sorted()
            .takeLast(3)
            .fold(1) { acc, i -> acc * i }
    }

    val testInput = getMappedInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = getMappedInput("Day09")
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
