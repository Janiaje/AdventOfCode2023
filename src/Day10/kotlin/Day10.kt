import Day10.CardinalDirection.*
import Day10.CardinalDirection.Companion.computeDirection
import Day10.Direction.LEFT
import Day10.Direction.RIGHT
import Day10.TileType.Start
import java.io.File

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
            .mapNotNull { coordinate.goSafe(it) }
            .map { it.getTile() }

        private fun isConnectingWith(tile: Tile): Boolean {
            return this.type.connectingDirections.contains(computeDirection(this.coordinate, tile.coordinate))
                && tile.type.connectingDirections.contains(computeDirection(tile.coordinate, this.coordinate))
        }
    }

    enum class TileType(
        val symbol: Char,
        val drawSymbol: Char,
        val connectingDirections: List<CardinalDirection>,
        val getSideCoordinates: (Coordinate, CardinalDirection, Direction) -> List<Coordinate>
    ) {
        PipeVertical(
            '|',
            '|',
            listOf(NORTH, SOUTH),
            { current, travelingDirection, side ->
                if (travelingDirection == NORTH && side == LEFT || travelingDirection == SOUTH && side == RIGHT) {
                    listOfNotNull(current.goSafe(WEST))
                } else {
                    listOfNotNull(current.goSafe(EAST))
                }
            }
        ),
        PipeHorizontal(
            '-',
            '—',
            listOf(EAST, WEST),
            { current, travelingDirection, side ->
                if (travelingDirection == EAST && side == LEFT || travelingDirection == WEST && side == RIGHT) {
                    listOfNotNull(current.goSafe(NORTH))
                } else {
                    listOfNotNull(current.goSafe(SOUTH))
                }
            }
        ),
        PipeNorthEast(
            'L',
            '╰',
            listOf(NORTH, EAST),
            { current, travelingDirection, side ->
                if (travelingDirection == NORTH && side == LEFT || travelingDirection == EAST && side == RIGHT) {
                    listOfNotNull(current.goSafe(WEST), current.goSafe(SOUTH))
                } else {
                    listOf()
                }
            }
        ),
        PipeNorthWest(
            'J',
            '╯',
            listOf(NORTH, WEST),
            { current, travelingDirection, side ->
                if (travelingDirection == NORTH && side == RIGHT || travelingDirection == WEST && side == LEFT) {
                    listOfNotNull(current.goSafe(EAST), current.goSafe(SOUTH))
                } else {
                    listOf()
                }
            }
        ),
        PipeSouthWest(
            '7',
            '╮',
            listOf(SOUTH, WEST),
            { current, travelingDirection, side ->
                if (travelingDirection == SOUTH && side == LEFT || travelingDirection == WEST && side == RIGHT) {
                    listOfNotNull(current.goSafe(NORTH), current.goSafe(EAST))
                } else {
                    listOf()
                }
            }
        ),
        PipeSouthEast(
            'F',
            '╭',
            listOf(SOUTH, EAST),
            { current, travelingDirection, side ->
                if (travelingDirection == SOUTH && side == RIGHT || travelingDirection == EAST && side == LEFT) {
                    listOfNotNull(current.goSafe(NORTH), current.goSafe(WEST))
                } else {
                    listOf()
                }
            }
        ),
        Ground(
            '.',
            ' ',
            listOf(),
            { _, _, _ -> throw Exception("N/A") }
        ),
        Start(
            'S',
            'S',
            CardinalDirection.entries,
            { _, _, _ -> throw Exception("N/A") }
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

        fun goSafe(direction: CardinalDirection) = runCatching { go(direction) }.getOrNull()

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
        val tiles: List<MutableList<Tile>> = text.mapIndexed { y, row ->
            row.toList()
                .mapIndexed { x, tileCharacter ->
                    val type = TileType.entries.first { it.symbol == tileCharacter }
                    val coordinate = Coordinate(x, y)
                    Tile(type, coordinate)
                }
                .toMutableList()
        }

        val maxY = tiles.lastIndex
        val maxX = tiles.first().lastIndex

        fun findStartingTile(): Tile = tiles.flatten().first { it.type == Start }

        fun updateStartingPoint(loop: List<Coordinate>) {
            val start = loop.first()
            val second = loop.drop(1).first()
            val last = loop.last()

            val startToFirstDirection = computeDirection(start, second)
            val startToLastDirection = computeDirection(start, last)

            val connectingDirections = setOf(startToFirstDirection, startToLastDirection)

            val startingTileType = TileType.entries.first {
                it.connectingDirections.intersect(connectingDirections.toSet()).size == 2
            }

            tiles[start.y][start.x] = Tile(startingTileType, start)
        }

        fun draw() {
            tiles.forEach { println(it.joinToString { it.type.drawSymbol.toString() }) }
        }

        fun draw(loop: List<Tile>, enclosedTiles: Set<Tile>? = null) {
            println("=".repeat(maxX + 1))

            tiles.forEach { row ->
                println(
                    row.joinToString("") {
                        if (loop.contains(it)) {
                            it.type.drawSymbol.toString()
                        } else if (enclosedTiles?.contains(it) == true) {
                            "●"
                        } else {
                            "·"
                        }
                    }
                )
            }

            println("=".repeat(maxX + 1))
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
        val loopCoordinates = loop.map { it.coordinate }

        val insideDirection: Direction = !getOutsideDirection(loopCoordinates)

        val enclosedTiles = mutableSetOf<Tile>()

        Map.updateStartingPoint(loopCoordinates)

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
            .flatMap { (current, next) ->
                val loopDirection = CardinalDirection.computeDirection(current, next)
                current.getTile().type.getSideCoordinates(current, loopDirection, insideDirection)
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
