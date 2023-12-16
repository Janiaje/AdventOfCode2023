import Day10.CardinalDirection.*
import Day10.Direction.LEFT
import Day10.TileType.Start
import java.io.File
import java.util.function.BiPredicate
import kotlin.math.abs

fun main() {
    Day10.compute()
    // Part 1 => 6725
    // Part 2 =>
    //     - Try #1: 381
}

object Day10 {

    private val inputFile = File("src/Day10/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf( // Part 2 solution => 1
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
    // private val text = listOf( // Part 2 solution => 4
    //     "...........",
    //     ".S-------7.",
    //     ".|F-----7|.",
    //     ".||.....||.",
    //     ".||.....||.",
    //     ".|L-7.F-J|.",
    //     ".|..|.|..|.",
    //     ".L--J.L--J.",
    //     "...........",
    // )
    // private val text = listOf( // Part 2 solution => 8
    //     ".F----7F7F7F7F-7....",
    //     ".|F--7||||||||FJ....",
    //     ".||.FJ||||||||L7....",
    //     "FJL7L7LJLJ||LJ.L-7..",
    //     "L--J.L7...LJS7F-7L7.",
    //     "....F-J..F7FJ|L7L7L7",
    //     "....L7.F7||L7|.L7L7|",
    //     ".....|FJLJ|FJ|F7|.LJ",
    //     "....FJL-7.||.||||...",
    //     "....L---J.LJ.LJLJ...",
    // )
    // private val text = listOf( // Part 2 solution => 10
    //     "FF7FSF7F7F7F7F7F---7",
    //     "L|LJ||||||||||||F--J",
    //     "FL-7LJLJ||||||LJL-77",
    //     "F--JF--7||LJLJ7F7FJ-",
    //     "L---JF-JLJ.||-FJLJJ7",
    //     "|F|F-JF---7F7-L7L|7|",
    //     "|FFJF7L7F-JF7|JL---7",
    //     "7-L-JL7||F7|L7F-7F7|",
    //     "L.L7LFJ|||||FJL7||LJ",
    //     "L7JLJL-JLJLJL--JLJ.L",
    // )

    data class Tile(
        val type: TileType,
        val coordinate: Coordinate
    ) {
        fun getConnectedNeighboringTiles() = getNeighboringTiles().filter { it.isConnectingWith(this) }

        fun getNeighboringTiles(): List<Tile> = CardinalDirection.entries
            .mapNotNull { runCatching { coordinate.go(it) }.getOrNull() }
            .map { it.getTile() }

        private fun isConnectingWith(tile: Tile): Boolean {
            return this.type.isConnecting.test(this.coordinate, tile.coordinate)
                && tile.type.isConnecting.test(tile.coordinate, this.coordinate)
        }
    }

    enum class TileType(
        val symbol: Char,
        val drawSymbol: Char,
        val isConnecting: BiPredicate<Coordinate, Coordinate>
    ) {
        PipeVertical(
            '|',
            '|',
            { current, other -> current.x == other.x && abs(current.y - other.y) == 1 }
        ),
        PipeHorizontal(
            '-',
            '—',
            { current, other -> current.y == other.y && abs(current.x - other.x) == 1 }
        ),
        PipeNorthEast(
            'L',
            '╰',
            { current, other ->
                (current.x == other.x && current.y == (other.y + 1))
                    || (current.y == other.y && current.x == (other.x - 1))
            }
        ),
        PipeNorthWest(
            'J',
            '╯',
            { current, other ->
                (current.x == other.x && current.y == (other.y + 1))
                    || (current.y == other.y && current.x == (other.x + 1))
            }
        ),
        PipeSouthWest(
            '7',
            '╮',
            { current, other ->
                (current.x == other.x && current.y == (other.y - 1))
                    || (current.y == other.y && current.x == (other.x + 1))
            }
        ),
        PipeSouthEast(
            'F',
            '╭',
            { current, other ->
                (current.x == other.x && current.y == (other.y - 1))
                    || (current.y == other.y && current.x == (other.x - 1))
            }
        ),
        Ground(
            '.',
            ' ',
            { _, _ -> false }
        ),
        Start(
            'S',
            'S',
            { _, _ -> true }
        )
    }

    data class Coordinate(
        val x: Int,
        val y: Int
    ) {
        fun go(direction: CardinalDirection): Coordinate = when (direction) {
            NORTH -> goNorth()
            SOUTH -> goSouth()
            EAST -> goEast()
            WEST -> goWest()
        }

        private fun goNorth(): Coordinate {
            if (y == 0) {
                throw LeavingMapException()
            }

            return Coordinate(x, y - 1)
        }

        private fun goSouth(): Coordinate {
            if (y == Map.maxY) {
                throw LeavingMapException()
            }

            return Coordinate(x, y + 1)
        }

        private fun goWest(): Coordinate {
            if (x == 0) {
                throw LeavingMapException()
            }

            return Coordinate(x - 1, y)
        }

        private fun goEast(): Coordinate {
            if (x == Map.maxX) {
                throw LeavingMapException()
            }

            return Coordinate(x + 1, y)
        }

        fun getTile() = Map.tiles[y][x]
    }

    class LeavingMapException : Exception("Leaving the map!")

    enum class CardinalDirection(private val index: Int) {
        NORTH(0),
        EAST(1),
        SOUTH(2),
        WEST(3);

        private val size by lazy { CardinalDirection.entries.size }

        fun turn(direction: Direction): CardinalDirection {
            val delta = if (direction == LEFT) -1 else 1

            val newIndex = index + delta

            val effectiveIndex = (newIndex % size + size) % size

            return CardinalDirection.entries[effectiveIndex]
        }

        companion object {
            fun computeDirection(current: Coordinate, next: Coordinate): CardinalDirection = when (next) {
                runCatching { current.go(NORTH) }.getOrNull() -> NORTH
                runCatching { current.go(EAST) }.getOrNull() -> EAST
                runCatching { current.go(SOUTH) }.getOrNull() -> SOUTH
                runCatching { current.go(WEST) }.getOrNull() -> WEST
                else -> throw Exception("The following tiles are not neighbours! current=$current next=$next")
            }
        }
    }

    enum class Direction {
        LEFT, RIGHT;

        operator fun not(): Direction {
            return when (this) {
                LEFT -> RIGHT
                RIGHT -> LEFT
            }
        }
    }

    object Map {
        val tiles: List<List<Tile>> = text.mapIndexed { y, row ->
            row.toList()
                .mapIndexed { x, tileCharacter ->
                    val type = TileType.entries.first { it.symbol == tileCharacter }
                    val coordinate = Coordinate(x, y)
                    Tile(type, coordinate)
                }
        }

        val maxY = tiles.lastIndex
        val maxX = tiles.first().lastIndex

        fun findStartingTile(): Tile = tiles.flatten().first { it.type == Start }

        fun draw() {
            tiles.forEach { println(it.joinToString { it.type.drawSymbol.toString() }) }
        }

        fun draw(loop: List<Tile>, enclosedTiles: Set<Tile>? = null) {
            println()

            tiles.forEach { row ->
                println(
                    row.joinToString("") {
                        if (loop.contains(it)) {
                            it.type.drawSymbol.toString()
                        } else if (enclosedTiles?.contains(it) == true) {
                            "⬤"
                        } else {
                            "·"
                            // " "
                        }
                    }
                )
            }

            println()
        }
    }

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
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
        val startingTile = Map.findStartingTile()
        val randomDirectionFirst = startingTile.getConnectedNeighboringTiles().first()

        val loop = mutableListOf(startingTile)

        var currentTile: Tile = randomDirectionFirst
        while (currentTile != startingTile) {
            loop.add(currentTile)
            currentTile = currentTile.getConnectedNeighboringTiles().first { it != loop[loop.lastIndex - 1] }
        }

        return loop
    }

    private fun part2(): Int {
        val loop = getLoop()

        val insideDirection: Direction = !getOutsideDirection(loop.map { it.coordinate })

        val enclosedTiles = mutableSetOf<Tile>()

        var addedTiles: List<Tile> = getLoopSide(loop, insideDirection).filter { !loop.contains(it) }
        while (addedTiles.isNotEmpty()) {

            enclosedTiles.addAll(addedTiles)

            addedTiles = addedTiles.flatMap {
                it.getNeighboringTiles()
                    .filter { !loop.contains(it) }
                    .filter { !enclosedTiles.contains(it) }
            }

        }

        Map.draw(loop, enclosedTiles)

        return enclosedTiles.size
    }

    private fun getLoopSide(loop: List<Tile>, insideDirection: Direction): List<Tile> =
        loop.map { it.coordinate }
            .toMutableList()
            .also { it.add(it.first()) }
            .zipWithNext()
            .mapNotNull { (current, next) ->
                val loopDirection = CardinalDirection.computeDirection(current, next)
                val cardinalDirection = loopDirection.turn(insideDirection)

                try {
                    current.go(cardinalDirection)
                } catch (e: LeavingMapException) {
                    null
                }
            }
            .map { it.getTile() }

    /**
     * The "outside" is defined as the region from where one can travel straight to the edge of the map
     * without encountering any points/tiles belonging to the loop.
     */
    private fun getOutsideDirection(loopCoordinates: List<Coordinate>): Direction {

        loopCoordinates.zipWithNext { current, next ->

            val loopDirection = CardinalDirection.computeDirection(current, next)

            Direction.entries.forEach {
                val cardinalDirection = loopDirection.turn(it)
                if (canReachMapSide(current, cardinalDirection, loopCoordinates)) {
                    return it
                }
            }

        }

        throw Exception("Cannot found the loop's out side!")
    }

    /**
     * "Walk" in a direction and stop if
     *   - the current coordinate is contained by the loop  => return false
     *   - it is the side of the map                        => return true
     */
    private fun canReachMapSide(
        current: Coordinate,
        direction: CardinalDirection,
        loopCoordinates: List<Coordinate>
    ): Boolean {
        var currentStep = current

        try {
            while (true) {
                currentStep = currentStep.go(direction)
                if (loopCoordinates.contains(currentStep)) {
                    return false
                }
            }
        } catch (e: LeavingMapException) {
            return true
        }
    }

}
