import java.io.File
import java.util.function.Predicate

object Solution {

    private val inputFile = File("input.txt")
    // private val inputFile = File("src/Day8/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "RL",
    //     "",
    //     "AAA = (BBB, CCC)",
    //     "BBB = (DDD, EEE)",
    //     "CCC = (ZZZ, GGG)",
    //     "DDD = (DDD, DDD)",
    //     "EEE = (EEE, EEE)",
    //     "GGG = (GGG, GGG)",
    //     "ZZZ = (ZZZ, ZZZ)",
    // )
    // private val text = listOf(
    //     "LLR",
    //     "",
    //     "AAA = (BBB, BBB)",
    //     "BBB = (AAA, ZZZ)",
    //     "ZZZ = (ZZZ, ZZZ)",
    // )
    // private val text = listOf(
    //     "LR",
    //     "",
    //     "11A = (11B, XXX)",
    //     "11B = (XXX, 11Z)",
    //     "11Z = (11B, XXX)",
    //     "22A = (22B, XXX)",
    //     "22B = (22C, 22C)",
    //     "22C = (22Z, 22Z)",
    //     "22Z = (22B, 22B)",
    //     "XXX = (XXX, XXX)",
    // )

    private const val startNodeKey = "AAA"
    private const val endNodeKey = "ZZZ"

    private val isStartNodePart2: Predicate<String> = Predicate { node: String -> node.endsWith("A") }
    private val isEndNodePart2: Predicate<String> = Predicate { node: String -> node.endsWith("Z") }

    enum class Direction(val symbol: Char, val index: Int) {
        LEFT('L', 0),
        RIGHT('R', 1)
    }

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1(): Int {

        val directions = parseDirections()
        val edges = parseEdges()

        var currentNode = startNodeKey

        var currentDirection: Direction
        var stepCounter = 0

        while (currentNode != endNodeKey) {
            stepCounter++
            currentDirection = directions.next()
            currentNode = edges[currentNode]!![currentDirection.index]
        }

        return stepCounter
    }

    private fun part2(): Long {

        val directions = parseDirections()
        val edges = parseEdges()

        var ghosts = edges.keys.filter { isStartNodePart2.test(it) }

        val loopLengths = mutableListOf<Int>()

        var currentDirection: Direction
        var stepCounter = 0

        while (ghosts.isNotEmpty()) {
            stepCounter++
            currentDirection = directions.next()
            ghosts = ghosts.map { edges[it]!![currentDirection.index] }

            if (ghosts.any { isEndNodePart2.test(it) }) {
                loopLengths.addAll(
                    List(ghosts.filter { isEndNodePart2.test(it) }.size) { stepCounter }
                )
                ghosts = ghosts.filter { !isEndNodePart2.test(it) }
            }
        }

        return findLCM(loopLengths)
    }

    private fun parseEdges() = text.drop(2)
        .associate {
            val sides = it.split(" = ")
            val key = sides.first()
            val value = sides.last()
                .trim('(', ')')
                .split(", ")

            key to value
        }

    private fun parseDirections() = text.first()
        .toList()
        .map { direction -> Direction.entries.first { it.symbol == direction } }
        .let { generateSequence { it }.flatten() }
        .iterator()

    private fun findLCM(numbers: List<Int>): Long {
        val highest = numbers.max()

        var currentValue = highest.toLong()
        while (!numbers.all { currentValue % it == 0L }) {
            currentValue += highest
        }

        return currentValue
    }

}

fun main() {
    Solution.compute()
}
