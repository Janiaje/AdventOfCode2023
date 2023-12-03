import java.io.File

object Solution {

    private val inputFile = File("input.txt")
    // private val inputFile = File("src/Day2/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
    //     "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
    //     "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
    //     "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
    //     "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green",
    // )

    private val maxDraw = Draw(12, 13, 14)

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = text
        .map { it.parseGame() }
        .filter { it.isValid() }
        .sumOf { it.id }

    private fun part2() = text
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
