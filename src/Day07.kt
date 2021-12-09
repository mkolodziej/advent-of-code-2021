import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun hPositions(input: List<String>) =
        input.first()
            .split(',')
            .map(String::toInt)

    fun maxRange(hPositions: List<Int>) =
        hPositions
            .fold(0..0) { maxRange, hPos -> min(maxRange.first, hPos)..max( maxRange.last, hPos ) }
            .also { println("Max. range: $it")}


    fun fuelRequiredPart1(hPositions: List<Int>, posCandidate: Int) =
        hPositions.sumOf { hPos -> abs(hPos - posCandidate) }

    fun part1(input: List<String>): Int? {
        val hPositions = hPositions(input)

        return maxRange(hPositions)
            .map { posCandidate -> Pair(posCandidate, fuelRequiredPart1(hPositions, posCandidate)) }
            .also { println("Pos/fuel calculations: $it") }
            .minByOrNull { it.second }
            .also { println("Pos/minimum fuel: $it") }
            ?.second
    }

    // step n uses n fuel, so for n first steps fuel used is sum all integers up to n
    fun fuelUsed(moves: Int): Int = moves * (moves + 1) / 2

    fun fuelRequiredPart2(hPositions: List<Int>, posCandidate: Int) =
        hPositions.sumOf { hPos -> fuelUsed(abs(hPos - posCandidate)) }


    fun part2(input: List<String>): Int? {
        val hPositions = hPositions(input)

        return maxRange(hPositions)
            .map { posCandidate -> Pair(posCandidate, fuelRequiredPart2(hPositions, posCandidate)) }
            .also { println("Pos/fuel calculations: $it") }
            .minByOrNull { it.second }
            .also { println("Pos/minimum fuel: $it") }
            ?.second
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day07_test")
    // part 1 test
    check(part1(testInput) == 37)

    // part 2 test
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    // part 1
    check(part1(input) == 349812)

    // part 2
    check(part2(input) == 99763899)
}
