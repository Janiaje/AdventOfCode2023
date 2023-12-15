import java.io.File

fun main() {
    Day9.compute()
}

object Day9 {

    private val inputFile = File("src/Day9/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "0 3 6 9 12 15",
    //     "1 3 6 10 15 21",
    //     "10 13 16 21 30 45",
    // )

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = text.sumOf { extrapolateNextValue(it) }

    private fun part2() = text.sumOf { extrapolatePreviousValue(it) }

    private fun extrapolateNextValue(history: String) = generateDeltaLists(history)
        .reversed()
        .sumOf { it.last() }

    private fun extrapolatePreviousValue(history: String) = generateDeltaLists(history)
        .reversed()
        .map { it.first() }
        .reduce { acc, i -> i - acc }

    private fun generateDeltaLists(history: String) = history.split(" ")
        .map { it.toInt() }
        .let {
            val lists = mutableListOf<List<Int>>()

            var lastList = it
            while (lastList.any { it != 0 }) {
                lists.add(lastList)
                lastList = lastList.zipWithNext { a, b -> b - a }
            }

            lists
        }

}
