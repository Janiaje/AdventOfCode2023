import java.io.File
import kotlin.math.pow

fun main() {
    Day4.compute()
}

object Day4 {

    private val inputFile = File("src/Day4/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
    //     "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
    //     "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
    //     "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
    //     "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
    //     "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11",
    // )

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = text
        .sumOf {
            val matchCount = calculateMatches(it).size

            if (matchCount != 0) {
                2.0.pow((matchCount - 1).toDouble()).toInt()
            } else {
                0
            }
        }

    private fun calculateMatches(it: String): Set<Int> {
        val parts = it
            .split(": ")
            .last()
            .split(" | ")
            .map { it.parseInts().sorted() }

        val winningNumbers = parts.first()
        val numbersIHave = parts.last()

        return winningNumbers.intersect(numbersIHave.toSet())
    }

    private val intRegex = "\\b(\\d+)\\b".toRegex()

    private fun String.parseInts(): List<Int> = intRegex.findAll(this)
        .map { it.value.toInt() }
        .toList()

    private val matchMap = mutableMapOf<Int, Set<Int>>()
    private val scoreMap = mutableMapOf<Int, Int>()

    private fun part2(): Int {
        text
            .mapIndexed { index, line ->
                val matches = calculateMatches(line)
                val cardNumber = index + 1
                matchMap[cardNumber] = matches
                Pair(cardNumber, matches)
            }
            .also { scoreMap[it.last().first] = 1 }
            .reversed()
            .forEach {

                val value = if (it.second.isNotEmpty()) {
                    val cardIdRange = (it.first + 1)..(it.first + it.second.size)
                    val cardIds = scoreMap.keys.intersect(cardIdRange)
                    val additionalCardValue = cardIds.sumOf { scoreMap[it]!! }

                    1 + additionalCardValue
                } else {
                    1
                }

                scoreMap[it.first] = value
            }

        return scoreMap.values.sum()
    }

}
