import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

object Solution {

    private val inputFile = File("input.txt")
    // private val inputFile = File("src/Day6/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "Time:      7  15   30",
    //     "Distance:  9  40  200",
    // )

    private data class Game(val time: Long, val distance: Long) {

        fun solution(): Long {
            val min = ceil((time - sqrt(time.toDouble().pow(2.0) - 4 * distance)) / 2.0)
                .toLong()
                .let { result ->
                    if (result * time.minus(result) > distance) {
                        result
                    } else {
                        result + 1
                    }
                }

            val max = floor((time + sqrt(time.toDouble().pow(2.0) - 4 * distance)) / 2.0)
                .toLong()
                .let { result ->
                    if (result * time.minus(result) > distance) {
                        result
                    } else {
                        result - 1
                    }
                }

            val solution = max - min + 1

            // println("Game = $this")
            // println("min = $min")
            // println("max = $max")
            // println("solution = $solution")

            return solution
        }

    }

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = text
        .let {
            val times = text.first().parseLongs()
            val distances = text.last().parseLongs()
            val games = times.zip(distances).map { Game(it.first, it.second) }

            games
        }
        .map { it.solution() }
        .reduce { acc, i -> acc * i }

    private fun part2() = text
        .let {
            val time = text.first().parseLongs().joinToString("").toLong()
            val distance = text.last().parseLongs().joinToString("").toLong()

            Game(time, distance).solution()
        }

    private val intRegex = "\\b(\\d+)\\b".toRegex()

    private fun String.parseLongs() = intRegex.findAll(this)
        .map { it.value.toLong() }
        .toList()

}

fun main() {
    Solution.compute()
}
