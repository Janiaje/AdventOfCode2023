import java.io.File

object Solution {

    private val inputFile = File("input.txt")

    private val firstDigitRegex = "^\\D*?(?<first>\\d)".toRegex()
    private val lastDigitRegex = "(?<last>\\d)\\D*$".toRegex()

    private val firstDigitRegexPart2 = "^\\D*?(?<first>\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex()
    private val lastDigitRegexPart2 = "^\\D*?(?<last>\\d|${"one|two|three|four|five|six|seven|eight|nine".reversed()})"
        .toRegex()

    fun compute() {
        println("The part one results are ${part1()}")
        println("The part two results are ${part2()}")
    }

    private fun part1() = inputFile
        .readLines()
        .sumOf {
            val firstDigit = firstDigitRegex.find(it)!!.groups["first"]!!.value
            val lastDigit = lastDigitRegex.find(it)!!.groups["last"]!!.value
            "$firstDigit$lastDigit".toInt()
        }

    private fun part2() = inputFile
        .readLines()
        .sumOf {
            val firstDigit = firstDigitRegexPart2.find(it)!!.groups["first"]!!.value.parseToInt()
            val lastDigit = lastDigitRegexPart2.find(it.reversed())!!.groups["last"]!!.value.reversed().parseToInt()
            "$firstDigit$lastDigit".toInt()
        }

    private fun String.parseToInt() = when (this) {
        "zero" -> 0
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> toInt()
    }
}

fun main() {
    Solution.compute()
}
