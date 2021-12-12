import java.util.Stack

sealed interface Result
object Success : Result
data class Corrupted(val illegalChar: Char) : Result
data class Incomplete(val missingChars: List<Char>) : Result

fun main() {

    val closeSymbols = listOf(')', ']', '}', '>')
    val openSymbols = listOf('(', '[', '{', '<')
    val symbolPairs = openSymbols.zip(closeSymbols)

    val syntaxErrorScoreMap = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    fun findMatchingCloseSymbol(openSymbol: Char): Char? =
        symbolPairs
            .find { it.first == openSymbol }
            .let { it?.second }

    fun mapOpenToCloseSymbols(openSymbols: List<Char>): List<Char> =
        openSymbols
            .map(::findMatchingCloseSymbol)
            .requireNoNulls()

    fun symbolsMatch(openSymbol: Char, closeSymbol: Char): Boolean =
        findMatchingCloseSymbol(openSymbol) == closeSymbol

    fun parseLine(line: String): Result {
        val stack = Stack<Char>()

        for(symbol in line) {
            when {
                openSymbols.contains(symbol) -> stack.push(symbol)
                closeSymbols.contains(symbol) -> if(symbolsMatch(stack.peek(), symbol)) stack.pop() else return Corrupted(symbol)
                else -> throw RuntimeException("Invalid character $symbol")
            }
        }

        if(stack.isNotEmpty()) return Incomplete(mapOpenToCloseSymbols(stack.elements().toList().reversed()))

        return Success
    }

    fun part1(input: List<String>): Int =
        input
            .map(::parseLine)
            .filterIsInstance<Corrupted>()
            .sumOf { syntaxErrorScoreMap.getOrDefault(it.illegalChar, 0) }

    val completionScoreMap = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    fun completionScore(chars: List<Char>): Long =
        chars
            .fold(0L) { acc, char -> acc * 5 + completionScoreMap.getOrDefault(char, 0)}

    fun part2(input: List<String>): Long =
        input
            .map(::parseLine)
            .filterIsInstance<Incomplete>()
            .map { completionScore(it.missingChars) }
            .sorted()
            .let { scores -> scores[scores.size / 2 ] } // list is 0 indexed, so size/2 is middle (no +1 required)

    // test if implementation meets criteria from the description
    val testInput = readInput("Day10_test")

    // part 1 test
    val testOutput = part1(testInput)
    println("Test part 1: $testOutput")
    check(testOutput == 26397)

    // part 2 test
    val testOutput2 = part2(testInput)
    println("Test part 2: $testOutput2")
    check(testOutput2 == 288957L)

    val input = readInput("Day10")
    // part 1
    val output = part1(input)
    println("Part 1: $output")
    check(output == 319233)

    // part 2
    val output2 = part2(input)
    println("Part 2: $output2")
    check(output2 == 1118976874L)
}
