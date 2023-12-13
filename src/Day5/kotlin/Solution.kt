import java.io.File

object Solution {

    // private val inputFile = File("input.txt")
    private val inputFile = File("src/Day5/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "seeds: 79 14 55 13",
    //     "",
    //     "seed-to-soil map:",
    //     "50 98 2",
    //     "52 50 48",
    //     "",
    //     "soil-to-fertilizer map:",
    //     "0 15 37",
    //     "37 52 2",
    //     "39 0 15",
    //     "",
    //     "fertilizer-to-water map:",
    //     "49 53 8",
    //     "0 11 42",
    //     "42 0 7",
    //     "57 7 4",
    //     "",
    //     "water-to-light map:",
    //     "88 18 7",
    //     "18 25 70",
    //     "",
    //     "light-to-temperature map:",
    //     "45 77 23",
    //     "81 45 19",
    //     "68 64 13",
    //     "",
    //     "temperature-to-humidity map:",
    //     "0 69 1",
    //     "1 0 69",
    //     "",
    //     "humidity-to-location map:",
    //     "60 56 37",
    //     "56 93 4",
    // )

    fun compute() {
        println("Part 1 => ${part1()}")
        // println("Part 2 => ${part2()}")
    }

    private fun part1(): Long {
        val inputIterator = text.iterator()

        val seeds = inputIterator.next().parseLongs()
        inputIterator.next() // to jump the empty line

        val mappers = readMappers(inputIterator)

        var mappedValues = seeds

        mappers.forEach { mapper ->
            mappedValues = mappedValues.map { mapper.map(it) }
        }

        return mappedValues.min()
    }

    private fun readMappers(inputIterator: Iterator<String>): List<Mapper> {
        val mappers = mutableListOf<Mapper>()

        var line: String
        while (inputIterator.hasNext()) { // For all mappers

            line = inputIterator.next()
            // val from = line.split("-to-").first()
            // val to = line.split("-to-").last().split(" ").first()

            val rules = mutableListOf<MappingRule>()

            while (inputIterator.hasNext()) { // For a single mapper's rules

                line = inputIterator.next()

                if (line == "") {
                    break
                }

                val (destinationRangeStart, sourceRangeStart, rangeLength) = line.parseLongs()

                rules.add(
                    MappingRule(
                        destinationRangeStart,
                        sourceRangeStart,
                        rangeLength
                    )
                )

            }

            mappers.add(
                Mapper(
                    // from,
                    // to,
                    rules
                )
            )

        }

        return mappers
    }

    private val intRegex = "\\b(\\d+)\\b".toRegex()

    private fun String.parseLongs(): List<Long> = intRegex.findAll(this)
        .map { it.value.toLong() }
        .toList()

    class Mapper(
        // val from: String,
        // val to: String,
        private val rules: List<MappingRule>
    ) {
        fun map(value: Long): Long {

            rules.forEach {
                if (it.isRuling(value)) {
                    return it.mapValue(value)
                }
            }

            return value
        }

        fun mergeWith(other: Mapper): Mapper {

            val newRules = rules

            other.rules
                //.sortedBy { it.destinationRange.first }
                .flatMap {


                    if (otherRuler == null) {
                        return@flatMap listOf(it)
                    }

                    return@flatMap listOf(it)
                }

        }

        fun getRulers(range: LongRange) = rules.filter { it.isRuling(range) }
    }

    class MappingRule(
        private val destinationRangeStart: Long,
        private val sourceRangeStart: Long,
        private val rangeLength: Long
    ) {
        private val sourceRange = sourceRangeStart..<(sourceRangeStart + rangeLength)
        private val destinationRange = destinationRangeStart..<(destinationRangeStart + rangeLength)

        fun isRuling(sourceValue: Long): Boolean = sourceRange.contains(sourceValue)
        fun isRuling(range: LongRange): Boolean = sourceRange.contains(range.first) || sourceRange.contains(range.last)

        fun mapValue(sourceValue: Long): Long = destinationRangeStart + sourceValue - sourceRangeStart

        fun merge(secondLevelRule: MappingRule): List<MappingRule> {

            if (!secondLevelRule.isRuling(destinationRange)) {
                return mergeNonOverlappingRules(this, secondLevelRule)
            }



        }

        private fun mergeNonOverlappingRules(first: MappingRule, second: MappingRule): List<MappingRule> =
            if (first.sourceRange.last + 1 == second.sourceRange.first) {
                listOf(
                    MappingRule(
                        first.destinationRangeStart,
                        first.sourceRangeStart,
                        first.rangeLength + second.rangeLength
                    )
                )
            } else {
                listOf(
                    first,
                    second
                )
            }
    }

    private fun part2(): Long {
        val inputIterator = text.iterator()

        val seedRanges = inputIterator.next()
            .parseLongs()
            .chunked(2)
            .map {
                val start = it.first()
                val length = it.last()
                start..<(start + length)
            }

        inputIterator.next() // to jump the empty line

        val mappers = readMappers(inputIterator)

        var mappedValues = seedRanges

        mappers.forEach { mapper ->
            mappedValues = mappedValues.map { mapper.map(it) }
        }

        return mappedValues.min()
    }

}

fun main() {
    Solution.compute()
}
