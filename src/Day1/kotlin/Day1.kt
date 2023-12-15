import java.io.File

fun main() {
    Day1.compute()
}

object Day1 {

    private val inputFile = File("src/Day1/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "1abc2",
    //     "pqr3stu8vwx",
    //     "a1b2c3d4e5f",
    //     "treb7uchet",
    // )
    // private val text = listOf(
    //     "two1nine",
    //     "eightwothree",
    //     "abcone2threexyz",
    //     "xtwone3four",
    //     "4nineeightseven2",
    //     "zoneight234",
    //     "7pqrstsixteen",
    // )

    private val partOneFirstDigitRegex = "^\\D*?(?<first>\\d)".toRegex()
    private val partOneLastDigitRegex = "(?<last>\\d)\\D*$".toRegex()

    private val partTwoFirstDigitRegexPart = "^\\D*?(?<first>\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex()
    private val partTwoLastDigitRegexPart = "^\\D*?(?<last>\\d|${"one|two|three|four|five|six|seven|eight|nine".reversed()})"
        .toRegex()

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = text.sumOf {
        val firstDigit = partOneFirstDigitRegex.find(it)!!.groups["first"]!!.value
        val lastDigit = partOneLastDigitRegex.find(it)!!.groups["last"]!!.value
        "$firstDigit$lastDigit".toInt()
    }

    private fun part2() = text.sumOf {
        val firstDigit = partTwoFirstDigitRegexPart.find(it)!!.groups["first"]!!.value.parseToInt()
        val lastDigit = partTwoLastDigitRegexPart.find(it.reversed())!!.groups["last"]!!.value.reversed().parseToInt()
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
        else -> this
    }
}
