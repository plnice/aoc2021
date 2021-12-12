fun main() {
    fun part1(input: List<Pair<String, String>>): Int {
        val edges = input + input.map { it.second to it.first }

        fun paths(vertex: String, edges: List<Pair<String, String>>): Int {
            if (vertex == "end") return 1
            val vertexEdges = edges.filter { it.first == vertex }

            val nextVertices = vertexEdges.map { it.second }
            val nextEdges = if (vertex.all { it.isLowerCase() }) edges - vertexEdges.toSet() else edges

            return if (nextVertices.isNotEmpty() && nextEdges.isNotEmpty())
                nextVertices.sumOf { paths(it, nextEdges) }
            else 0
        }

        return paths("start", edges)
    }

    fun part2(input: List<Pair<String, String>>): Int {
        val edges = (input + input.map { it.second to it.first })
        val vertices = edges.map { listOf(it.first, it.second) }.flatten().distinct()
        val lowercaseVertices = vertices.filter { v -> v != "start" && v != "end" && v.all { it.isLowerCase() } }

        val visitsCombinations = lowercaseVertices.map { vertex ->
            vertices.associateWith { value ->
                when {
                    value == "start" -> 1
                    value == vertex -> 2
                    value.all { it.isLowerCase() } -> 1
                    else -> -1
                }
            }
        }

        fun paths(path: List<String>, edges: List<Pair<String, String>>, visits: Map<String, Int>): List<List<String>> {
            val vertex = path.last()

            if (vertex == "end") return listOf(path)
            val vertexEdges = edges.filter { it.first == vertex }

            val visitsLeft = visits[vertex]!! - 1

            val nextVertices = vertexEdges.map { it.second }
            val nextEdges = if (visitsLeft == 0) edges - vertexEdges.toSet() else edges
            val nextVisits = visits + (vertex to visitsLeft)

            return if (nextVertices.isNotEmpty() && nextEdges.isNotEmpty())
                nextVertices.map { paths(path + it, nextEdges, nextVisits) }.flatten()
            else emptyList()
        }

        return visitsCombinations
            .map { visits -> paths(listOf("start"), edges, visits) }
            .flatten()
            .distinct()
            .size
    }

    check(part1(getMappedInput("Day12_test1")) == 10)
    check(part1(getMappedInput("Day12_test2")) == 19)
    check(part1(getMappedInput("Day12_test3")) == 226)

    check(part2(getMappedInput("Day12_test1")) == 36)
    check(part2(getMappedInput("Day12_test2")) == 103)
    check(part2(getMappedInput("Day12_test3")) == 3509)

    val input = getMappedInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun getMappedInput(name: String) = readInput(name)
    .map { it.split("-") }
    .map { it[0] to it[1] }
