import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day3.compute()
}

object Day3 {

    private val inputFile = File("src/Day3/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "467..114..",
    //     "...*......",
    //     "..35..633.",
    //     "......#...",
    //     "617*......",
    //     ".....+.58.",
    //     "..592.....",
    //     "......755.",
    //     "...$.*....",
    //     ".664.598..",
    // )

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private val numberRegex = "\\b(?<number>\\d+)\\b".toRegex()

    private fun part1() = text
        .mapIndexed { lineIndex, line ->
            numberRegex.findAll(line)
                .sumOf {
                    if (doesHaveAdjacentSymbol(lineIndex, it.range)) {
                        it.value.toInt()
                    } else {
                        0
                    }
                }
        }
        .sum()

    private val symbolRegex = "[^.\\d]".toRegex()

    private fun doesHaveAdjacentSymbol(lineIndex: Int, numberRange: IntRange): Boolean {
        val currentLine = text[lineIndex]

        val start = max(numberRange.first - 1, 0)
        val end = min(numberRange.last + 1, currentLine.lastIndex)

        var adjacentCharacters = ""

        if (lineIndex > 0) {
            adjacentCharacters += text[lineIndex - 1].slice(start..end)
        }

        if (numberRange.first > 0) {
            val previousCharIndex = numberRange.first - 1
            adjacentCharacters += currentLine.slice(previousCharIndex..previousCharIndex)
        }

        if (numberRange.last < currentLine.lastIndex) {
            val nextCharIndex = numberRange.last + 1
            adjacentCharacters += currentLine.slice(nextCharIndex..nextCharIndex)
        }

        if (lineIndex < text.lastIndex) {
            adjacentCharacters += text[lineIndex + 1].slice(start..end)
        }

        return symbolRegex.find(adjacentCharacters) != null
    }

    private val starRegex = "\\*".toRegex()

    private fun part2() = text
        .mapIndexed { lineIndex, line ->
            if (!line.contains("*")) {
                return@mapIndexed 0
            }

            starRegex.findAll(line)
                .sumOf {
                    val adjacentNumbers = getAdjacentNumbers(lineIndex, it.range.first)

                    when (adjacentNumbers.size) {
                        2 -> adjacentNumbers.first() * adjacentNumbers.last()
                        1 -> 0
                        0 -> 0
                        else -> throw IllegalStateException("Gear has more than one adjacent number!")
                    }

                }
        }
        .sum()

    private fun getAdjacentNumbers(lineIndex: Int, starIndex: Int): MutableList<Int> {
        val currentLine = text[lineIndex]

        val start = max(starIndex - 1, 0)
        val end = min(starIndex + 1, currentLine.lastIndex)

        val starRange = (start..end).toList()

        val adjacentNumbers = mutableListOf<Int>()

        val lines = mutableListOf(
            currentLine
        )

        if (lineIndex > 0) {
            lines.add(text[lineIndex - 1])
        }

        if (lineIndex < text.lastIndex) {
            lines.add(text[lineIndex + 1])
        }

        lines.forEach { line ->
            numberRegex.findAll(line)
                .filter { it.range.toList().any { it in starRange } }
                .map { it.value.toInt() }
                .also { adjacentNumbers.addAll(it) }
        }

        return adjacentNumbers
    }

}
