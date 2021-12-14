fun main() {
    fun part1(template: String, rules: List<Rule>): Int {
        fun List<Rule>.find(elements: String) = first { it.elements == elements }
        fun String.extendWith(rule: Rule) = this[0].toString() + rule.toAdd + this[1]

        var polymer = template

        repeat(10) {
            polymer = polymer
                .windowed(2)
                .reduce { acc, s ->
                    if (acc.length == 2) acc.extendWith(rules.find(acc)).dropLast(1) + s.extendWith(rules.find(s))
                    else acc.dropLast(1) + s.extendWith(rules.find(s))
                }
        }

        return polymer
            .groupingBy { it }
            .eachCount()
            .let { map -> map.values.let { it.maxOrNull()!! - it.minOrNull()!! } }
    }

    fun part2(template: String, rules: List<Rule>, repeats: Int): Long {
        fun List<Rule>.find(elements: String) = first { it.elements == elements }
        fun String.extendWith(rule: Rule) = listOf(this[0].toString() + rule.toAdd, rule.toAdd.toString() + this[1])

        fun merge(map1: Map<Char, Long>, map2: Map<Char, Long>): Map<Char, Long> {
            val keys = map1.keys + map2.keys
            return keys.associateWith { key -> (map1[key] ?: 0) + (map2[key] ?: 0) }
        }

        val pairsCount = template
            .windowed(2)
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()

        repeat(repeats) {
            val pairsCountExtended = pairsCount.mapKeys { it.key.extendWith(rules.find(it.key)) }

            pairsCount.clear()

            pairsCountExtended.forEach { (listKey, value) ->
                pairsCount[listKey[0]] = (pairsCount[listKey[0]] ?: 0) + value
                pairsCount[listKey[1]] = (pairsCount[listKey[1]] ?: 0) + value
            }
        }

        val map = pairsCount.entries.fold(emptyMap<Char, Long>()) { acc, (pair, count) ->
            if (acc.isEmpty()) merge(mapOf(pair[0] to count), mapOf(pair[1] to count))
            else acc + (pair[1] to (acc[pair[1]] ?: 0) + count)
        }

        return map.values.let { it.maxOrNull()!! - it.minOrNull()!! }
    }

    val (testTemplate, testRules) = getMappedInput("Day14_test")
    check(part1(testTemplate, testRules) == 1588)
    check(part2(testTemplate, testRules, repeats = 10) == 1588L)
    check(part2(testTemplate, testRules, repeats = 40) == 2188189693529L)

    val (template, rules) = getMappedInput("Day14")
    println(part1(template, rules))
    println(part2(template, rules, repeats = 40))
}

private data class Rule(val elements: String, val toAdd: Char)

private fun getMappedInput(name: String) = readInput(name)
    .filter { it.isNotEmpty() }
    .partition { it.contains("->") }
    .let { (rules, template) ->
        template[0] to rules
            .map { rule ->
                rule.split(" -> ").let { Rule(it[0], it[1][0]) }
            }
    }
