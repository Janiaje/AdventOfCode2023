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

    private const val GALAXY_CHAR = '#'

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    data class Galaxy(val x: Int, val y: Int) {

        companion object {
            private val emptyRowIndexes = text
                .mapIndexedNotNull { index, row ->
                    if (!row.contains(GALAXY_CHAR)) {
                        index
                    } else {
                        null
                    }
                }

            private val emptyColumnIndexes = text
                .map { it.toList() }
                .transpose()
                .mapIndexedNotNull { index, row ->
                    if (!row.contains(GALAXY_CHAR)) {
                        index
                    } else {
                        null
                    }
                }
        }

        fun distanceTo(other: Galaxy, expansionRatio: Long): Long {

            val originalDistance = abs(x - other.x) + abs(y - other.y)

            val xRange = x.properRange(other.x)
            val yRange = y.properRange(other.y)

            val expansionDistance = listOf(
                emptyColumnIndexes.filter { xRange.contains(it) },
                emptyRowIndexes.filter { yRange.contains(it) },
            )
                .flatten()
                .count()
                .let { it * (expansionRatio - 1) }

            return originalDistance + expansionDistance
        }

    }

    private fun Int.properRange(other: Int) = if (this <= other) this..other else other..this

    private fun List<List<Char>>.transpose(): List<List<Char>> {
        val cols = this[0].size
        val rows = this.size
        return List(cols) { j ->
            List(rows) { i ->
                this[i][j]
            }
        }
    }

    private fun part1() = computeDistancesSum(2L)

    private fun computeDistancesSum(expansionRatio: Long) = text
        .map { it.toList() }
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
        .sumOf { it.first.distanceTo(it.second, expansionRatio) }

    private fun generateRoutes(galaxies: List<Galaxy>): List<Pair<Galaxy, Galaxy>> =
        galaxies.flatMapIndexed { index, first ->
            galaxies.drop(index + 1).map { second -> Pair(first, second) }
        }

    private fun part2() = computeDistancesSum(1_000_000L)

}
