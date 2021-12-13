import Along.X
import Along.Y

fun main() {
    fun List<Pair<Int, Int>>.foldX(foldX: Int) = map { (x, y) ->
        if (x < foldX) x to y
        else x - (x - foldX) * 2 to y
    }.distinct()

    fun List<Pair<Int, Int>>.foldY(foldY: Int) = map { (x, y) ->
        if (y < foldY) x to y
        else x to y - (y - foldY) * 2
    }.distinct()

    fun List<Pair<Int, Int>>.fold(fold: Fold) = when (fold.along) {
        X -> foldX(fold.value)
        Y -> foldY(fold.value)
    }

    fun part1(dots: List<Pair<Int, Int>>, folds: List<Fold>): Int {
        return dots.fold(folds.first()).count()
    }

    fun part2(dots: List<Pair<Int, Int>>, folds: List<Fold>) {
        val result = folds.fold(dots) { acc, fold -> acc.fold(fold) }

        val maxX = result.maxOf { it.first }
        val maxY = result.maxOf { it.second }

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                print(if (result.contains(x to y)) "#" else " ")
            }
            println()
        }
    }

    val (testDots, testFolds) = getMappedInput("Day13_test")
    check(part1(testDots, testFolds) == 17)

    val (dots, folds) = getMappedInput("Day13")
    println(part1(dots, folds))
    part2(dots, folds)
}

private enum class Along { X, Y }
private data class Fold(val along: Along, val value: Int)

private fun getMappedInput(name: String) = readInput(name)
    .filter { it.isNotEmpty() }
    .partition { it.contains(',') }
    .let { (dots, folds) ->
        val parsedDots = dots.map { dot ->
            dot
                .split(",")
                .let { it[0].toInt() to it[1].toInt() }
        }
        val parsedFolds = folds.map { fold ->
            fold
                .removePrefix("fold along ")
                .split("=")
                .let { Fold(along = if (it[0] == "x") X else Y, value = it[1].toInt()) }
        }

        parsedDots to parsedFolds
    }
