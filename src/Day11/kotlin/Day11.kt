import java.io.File
import kotlin.math.abs

fun main() {
    Day11.compute()
}

object Day11 {

    private val inputFile = File("src/Day11/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "...#......",
    //     ".......#..",
    //     "#.........",
    //     "..........",
    //     "......#...",
    //     ".#........",
    //     ".........#",
    //     "..........",
    //     ".......#..",
    //     "#...#.....",
    // )

    private const val EMPTY_SPACE_CHAR = '.'
    private const val GALAXY_CHAR = '#'

    fun compute() {
        println("Part 1 => ${part1()}")
        // println("Part 2 => ${part2()}")
    }

    data class Galaxy(val x: Int, val y: Int) {

        companion object {
            private var id = 1
        }

        val id = Galaxy.id++

        fun distanceTo(other: Galaxy): Int = abs(x - other.x) + abs(y - other.y)

    }

    private fun part1(): Int = expandMap(text)
        .flatMapIndexed { y: Int, row: List<Char> ->
            row.mapIndexedNotNull { x, cell ->
                if (cell == GALAXY_CHAR) {
                    Galaxy(x, y)
                } else {
                    null
                }
            }
        }
        .let { generateRoutes(it) }
        .sumOf { it.first.distanceTo(it.second) }

    private fun generateRoutes(galaxies: List<Galaxy>): List<Pair<Galaxy, Galaxy>> =
        galaxies.flatMapIndexed { index, first ->
            galaxies.drop(index + 1).map { second -> Pair(first, second) }
        }

    private fun expandMap(text: List<String>): List<List<Char>> = text
        .map { it.toList() }
        .let { expandRows(it) }
        .let { transpose(it) }
        .let { expandRows(it) }
        .let { transpose(it) }

    private fun expandRows(text: List<List<Char>>): List<List<Char>> = text.flatMap {

        return@flatMap if (it.contains('#')) {
            listOf(it)
        } else {
            listOf(it, it)
        }

    }

    private inline fun <reified T> transpose(matrix: List<List<T>>): List<List<T>> {
        val cols = matrix[0].size
        val rows = matrix.size
        return List(cols) { j ->
            List(rows) { i ->
                matrix[i][j]
            }
        }
    }

    private fun part2() = text

}
