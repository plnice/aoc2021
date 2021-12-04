fun main() {
    fun part1(input: Input): Int {
        val (drawn, boards) = input
        val checking = drawn.subList(0, 5).toMutableList()
        var toAdd = 5

        while (true) {
            val winningBoard = boards.firstOrNull { (it.rows + it.columns).any { checking.containsAll(it) } }
            if (winningBoard != null) {
                val unmarked = winningBoard.rows.flatten().filter { !checking.contains(it) }
                return unmarked.sum() * checking.last()
            }
            if (checking.size == drawn.size) break
            checking += drawn[toAdd]
            toAdd++
        }

        throw IllegalArgumentException("No winners!")
    }

    fun part2(input: Input): Int {
        val drawn = input.drawn
        val boards = input.boards.toMutableList()
        val checking = drawn.subList(0, 5).toMutableList()
        var toAdd = 5
        var lastWinningResult = 0

        while (boards.isNotEmpty()) {
            val winningBoards = boards.filter { (it.rows + it.columns).any { checking.containsAll(it) } }
            winningBoards.forEach {
                boards.remove(it)
                val unmarked = it.rows.flatten().filter { !checking.contains(it) }
                lastWinningResult = unmarked.sum() * checking.last()
            }
            if (checking.size == drawn.size) break
            checking += drawn[toAdd]
            toAdd++
        }

        return lastWinningResult
    }

    val testInput = getMappedInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = getMappedInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Input(val drawn: List<Int>, val boards: List<Board>)
data class Board(val rows: List<List<Int>>, val columns: List<List<Int>>)

private fun getMappedInput(name: String) = readInput(name)
    .let { input ->
        val drawn = input[0].split(',').map(String::toInt)
        val boards = input
            .drop(2)
            .chunked(6)
            .map { it.subList(0, 5) }
            .map {
                val rows = it.map { it.split(' ').filter(String::isNotBlank).map(String::toInt) }
                val columns = buildList { for (i in 0..4) add(rows.map { it[i] }) }
                Board(rows, columns)
            }
        Input(drawn, boards)
    }
