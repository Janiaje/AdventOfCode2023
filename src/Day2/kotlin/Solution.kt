import java.io.File

object Solution {

    private val inputFile = File("input.txt")

    private val maxDraw = Draw(12, 13, 14)

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = inputFile
        .readLines()
        .map { it.parseGame() }
        .filter { it.isValid() }
        .sumOf { it.id }

    private fun part2() = inputFile
        .readLines()
        .sumOf { it.parseGame().getPower() }

    private data class Game(val id: Int, val draws: List<Draw>) {
        fun isValid(): Boolean {

            draws.forEach {
                if (
                    it.red > maxDraw.red
                    || it.green > maxDraw.green
                    || it.blue > maxDraw.blue
                ) {
                    return false
                }
            }

            return true
        }

        private fun getFewestPossibleCubeCount(): Triple<Int, Int, Int> {

            val red = draws.maxOfOrNull { it.red } ?: 0
            val green = draws.maxOfOrNull { it.green } ?: 0
            val blue = draws.maxOfOrNull { it.blue } ?: 0

            return Triple(red, green, blue)
        }

        fun getPower(): Int = getFewestPossibleCubeCount().toList().reduce { acc, i -> acc * i }
    }

    private fun String.parseGame(): Game {
        val (game, rawDraws) = this.split(": ")

        val gameId = game.drop("Game ".length).toInt()
        val draws = rawDraws.split("; ")
            .map { it.parseDraw() }

        return Game(gameId, draws)
    }

    private data class Draw(val red: Int, val green: Int, val blue: Int) {

        companion object {
            val redRegex = "(?<value>\\d+) red".toRegex()
            val greenRegex = "(?<value>\\d+) green".toRegex()
            val blueRegex = "(?<value>\\d+) blue".toRegex()

            fun parseValue(input: String, regex: Regex): Int =
                regex.find(input)?.groups?.get("value")?.value?.toInt() ?: 0
        }

    }

    private fun String.parseDraw(): Draw {
        val red = Draw.parseValue(this, Draw.redRegex)
        val green = Draw.parseValue(this, Draw.greenRegex)
        val blue = Draw.parseValue(this, Draw.blueRegex)

        return Draw(red, green, blue)
    }
}

fun main() {
    Solution.compute()
}
