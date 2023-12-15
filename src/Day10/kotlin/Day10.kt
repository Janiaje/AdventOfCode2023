import Day10.TileType.Start
import java.io.File
import java.util.function.BiPredicate
import kotlin.math.abs

fun main() {
    Day10.compute()
}

object Day10 {

    private val inputFile = File("src/Day10/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     ".....",
    //     ".S-7.",
    //     ".|.|.",
    //     ".L-J.",
    //     ".....",
    // )
    // private val text = listOf(
    //     "7-F7-",
    //     ".FJ|7",
    //     "SJLL7",
    //     "|F--J",
    //     "LJ.LJ",
    // )

    data class Tile(
        val type: TileType,
        val coordinate: Coordinate
    ) {
        fun getConnectedNeighboringTiles() = getNeighboringTiles().filter { it.isConnectingWith(this) }

        fun getNeighboringTiles(): List<Tile> = listOf(
            { coordinate.goNorth() },
            { coordinate.goEast() },
            { coordinate.goSouth() },
            { coordinate.goWest() },
        )
            .mapNotNull { statement -> runCatching { statement() }.getOrNull() }
            .map { it.getTile() }

        private fun isConnectingWith(tile: Tile): Boolean {
            return this.type.isConnecting.test(this.coordinate, tile.coordinate)
                && tile.type.isConnecting.test(tile.coordinate, this.coordinate)
        }
    }

    enum class TileType(
        val symbol: Char,
        val isConnecting: BiPredicate<Coordinate, Coordinate>
    ) {
        PipeVertical(
            '|',
            { current, other -> current.x == other.x && abs(current.y - other.y) == 1 }
        ),
        PipeHorizontal(
            '-',
            { current, other -> current.y == other.y && abs(current.x - other.x) == 1 }
        ),
        PipeNorthEast(
            'L',
            { current, other ->
                (current.x == other.x && current.y == (other.y + 1))
                    || (current.y == other.y && current.x == (other.x - 1))
            }
        ),
        PipeNorthWest(
            'J',
            { current, other ->
                (current.x == other.x && current.y == (other.y + 1))
                    || (current.y == other.y && current.x == (other.x + 1))
            }
        ),
        PipeSouthWest(
            '7',
            { current, other ->
                (current.x == other.x && current.y == (other.y - 1))
                    || (current.y == other.y && current.x == (other.x + 1))
            }
        ),
        PipeSouthEast(
            'F',
            { current, other ->
                (current.x == other.x && current.y == (other.y - 1))
                    || (current.y == other.y && current.x == (other.x - 1))
            }
        ),
        Ground(
            '.',
            { _, _ -> false }
        ),
        Start(
            'S',
            { _, _ -> true }
        )
    }

    data class Coordinate(
        val x: Int,
        val y: Int
    ) {
        fun goNorth(): Coordinate {
            if (y == 0) {
                throw Exception("Leaving the map!")
            }

            return Coordinate(x, y - 1)
        }

        fun goSouth(): Coordinate {
            if (y == maxY) {
                throw Exception("Leaving the map!")
            }

            return Coordinate(x, y + 1)
        }

        fun goWest(): Coordinate {
            if (x == 0) {
                throw Exception("Leaving the map!")
            }

            return Coordinate(x - 1, y)
        }

        fun goEast(): Coordinate {
            if (x == maxX) {
                throw Exception("Leaving the map!")
            }

            return Coordinate(x + 1, y)
        }

        fun getTile() = tiles[y][x]
    }

    private val tiles = text.mapIndexed { y, row ->
        row.toList()
            .mapIndexed { x, tileCharacter ->
                val type = TileType.entries.first { it.symbol == tileCharacter }
                val coordinate = Coordinate(x, y)
                Tile(type, coordinate)
            }
    }

    private val maxY = tiles.lastIndex
    private val maxX = tiles.first().lastIndex

    fun compute() {
        println("Part 1 => ${part1()}")
        // println("Part 2 => ${part2()}")
    }

    private fun part1(): Int {
        val loop = getLoop()

        if (loop.size % 2 == 1) {
            throw Exception("The loop has 2 furthest tile!")
        }

        val furthestDistance = ((loop.drop(1).size - 1) / 2) + 1

        return furthestDistance
    }

    private fun getLoop(): List<Tile> {
        val startingTile = findStartingTile()
        val randomDirectionFirst = startingTile.getConnectedNeighboringTiles().first()

        val loop = mutableListOf(startingTile)

        var currentTile: Tile = randomDirectionFirst
        while (currentTile != startingTile) {
            loop.add(currentTile)
            currentTile = currentTile.getConnectedNeighboringTiles().first { it != loop[loop.lastIndex - 1] }
        }

        return loop
    }

    private fun findStartingTile(): Tile = tiles.flatten().first { it.type == Start }

    enum class Direction{
        LEFT, RIGHT;

        operator fun not(): Direction {
            return when (this) {
                LEFT -> RIGHT
                RIGHT -> LEFT
            }
        }
    }

    private fun part2(): Int {
        val loop = getLoop()

        val insideDirection : Direction = !getOutsideDirection()

        val enclosedTiles = mutableSetOf<Tile>()

        var addedTiles: List<Tile> = getLoopSide(insideDirection).filter{ !loop.contains(it) }
        while (addedTiles.isNotEmpty()) {

            enclosedTiles.addAll(addedTiles)

            addedTiles = addedTiles.flatMap {
                it.getNeighboringTiles()
                    .filter { !loop.contains(it) }
                    .filter { !enclosedTiles.contains(it) }
            }

        }

        return enclosedTiles.size
    }

    /**
     * The "outside" is defined as the region from where one can travel straight to the edge of the map
     * without encountering any points/tiles belonging to the loop.
     */
    private fun getOutsideDirection(): Direction {
        TODO("Not yet implemented")
    }

}
