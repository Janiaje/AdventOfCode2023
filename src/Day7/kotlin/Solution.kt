import Solution.Card.C_J
import java.io.File
import java.util.function.BiPredicate
import java.util.function.Predicate

object Solution {

    private val inputFile = File("input.txt")
    // private val inputFile = File("src/Day7/kotlin/input.txt")

    private val text = inputFile.readLines()
    // private val text = listOf(
    //     "32T3K 765",
    //     "T55J5 684",
    //     "KK677 28",
    //     "KTJJT 220",
    //     "QQQJA 483",
    // )

    enum class Card(val symbol: String, val partOneValue: Int, val partTwoValue: Int) {
        C_A("A", 14, 14),
        C_K("K", 13, 13),
        C_Q("Q", 12, 12),
        C_J("J", 11, 1),
        C_T("T", 10, 10),
        C_9("9", 9, 9),
        C_8("8", 8, 8),
        C_7("7", 7, 7),
        C_6("6", 6, 6),
        C_5("5", 5, 5),
        C_4("4", 4, 4),
        C_3("3", 3, 3),
        C_2("2", 2, 2)
    }

    fun String.mapToCards() = this.split("")
        .filter { it.isNotBlank() }
        .map { character -> Card.entries.first { it.symbol == character } }

    enum class HandType(
        val value: Int,
        val partOnePredicate: Predicate<Map<Card, Int>>,
        val partTwoPredicate: BiPredicate<Map<Card, Int>, Int>
    ) {
        FIVE_OF_A_KIND(
            7,
            { cardCounts -> cardCounts.any { it.value == 5 } },
            { _, highestCardCountWithJ -> highestCardCountWithJ == 5 }
        ),
        FOUR_OF_A_KIND(
            6,
            { cardCounts -> cardCounts.any { it.value == 4 } },
            { _, highestCardCountWithJ -> highestCardCountWithJ == 4 }
        ),
        FULL_HOUSE(
            5,
            { cardCounts -> cardCounts.any { it.value == 3 } && cardCounts.any { it.value == 2 } },
            { cardCounts, highestCardCountWithJ ->
                (cardCounts.any { it.value == 3 } && cardCounts.any { it.value == 2 })
                    || (
                        TWO_PAIR.partTwoPredicate.test(cardCounts.filter { it.key != C_J }, highestCardCountWithJ)
                            && (cardCounts[C_J] ?: 0) == 1
                    )
            }
        ),
        THREE_OF_A_KIND(
            4,
            { cardCounts -> cardCounts.any { it.value == 3 } },
            { _, highestCardCountWithJ -> highestCardCountWithJ == 3 }
        ),
        TWO_PAIR(
            3,
            { cardCounts -> cardCounts.filter { it.value == 2 }.size == 2 },
            { cardCounts, _ -> cardCounts.filter { it.value == 2 }.size == 2 }
        ),
        ONE_PAIR(
            2,
            { cardCounts -> cardCounts.filter { it.value == 2 }.size == 1 },
            { _, highestCardCountWithJ -> highestCardCountWithJ == 2 }
        ),
        HIGH_CARD(
            1,
            { cardCounts -> cardCounts.size == 5 },
            { cardCounts, _ -> cardCounts.size == 5 }
        ),
    }

    data class Hand(val cards: String, val bid: Int) {

        private val cardLists = cards.mapToCards()

        private val cardCounts = cardLists
            .groupBy { it }
            .mapValues { it.value.size }

        private val cardCountsWithoutJ = cardCounts.filter { it.key != C_J }
        private val JCount = cardCounts[C_J] ?: 0

        private val highestCardCount = cardCountsWithoutJ.ifEmpty { null }?.maxOf { it.value } ?: 0
        private val highestCardCountWithJ = highestCardCount + JCount

        private val partOneType by lazy {
            HandType.entries
                .filter { it.partOnePredicate.test(cardCounts) }
                .maxBy { it.value }
        }

        private val partTwoType by lazy {
            HandType.entries
                .filter { it.partTwoPredicate.test(cardCounts, highestCardCountWithJ) }
                .maxBy { it.value }
        }

        fun partOneCompareTo(other: Hand): Int {
            if (this.cards == other.cards) {
                throw IllegalStateException("The hands are equal! this=$this other=$other")
            }

            if (this.partOneType != other.partOneType) {

                return if (this.partOneType.value > other.partOneType.value) {
                    1
                } else {
                    -1
                }

            }

            this.cardLists
                .zip(other.cardLists)
                .forEach { (thisCard, otherCard) ->

                    if (thisCard.partOneValue == otherCard.partOneValue) {
                        return@forEach
                    }

                    return if (thisCard.partOneValue > otherCard.partOneValue) {
                        1
                    } else {
                        -1
                    }

                }

            throw IllegalStateException("The hands are equal! this=$this other=$other")
        }

        fun partTwoCompareTo(other: Hand): Int {
            if (this.cards == other.cards) {
                throw IllegalStateException("The hands are equal! this=$this other=$other")
            }

            if (this.partTwoType != other.partTwoType) {

                return if (this.partTwoType.value > other.partTwoType.value) {
                    1
                } else {
                    -1
                }

            }

            this.cardLists
                .zip(other.cardLists)
                .forEach { (thisCard, otherCard) ->

                    if (thisCard.partTwoValue == otherCard.partTwoValue) {
                        return@forEach
                    }

                    return if (thisCard.partTwoValue > otherCard.partTwoValue) {
                        1
                    } else {
                        -1
                    }

                }

            throw IllegalStateException("The hands are equal! this=$this other=$other")
        }

    }

    private fun String.parseHand() = this.split(" ").let { Hand(it.first(), it.last().toInt()) }

    fun compute() {
        println("Part 1 => ${part1()}")
        println("Part 2 => ${part2()}")
    }

    private fun part1() = text
        .map { it.parseHand() }
        .sortedWith { left, right ->
            left.partOneCompareTo(right)
        }
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()

    private fun part2() = text
        .map { it.parseHand() }
        .sortedWith { left, right ->
            left.partTwoCompareTo(right)
        }
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()

}

fun main() {
    Solution.compute()
}
