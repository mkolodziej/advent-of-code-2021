const val LINE_REGEX = """([a-g]* )*\|( [a-g]*){4}"""

class SignalPattern(val pattern: String) {

    fun guessNumber(): Int? =
        when(pattern.length) {
            2 -> 1
            4 -> 4
            3 -> 7
            7 -> 8
           else -> null
        }

    val sortedPattern by lazy { pattern.toCharArray().sortedArray().joinToString("")}
    var number = guessNumber()

    override fun toString() = "$pattern ($number)"

    override fun equals(other: Any?) =
        other is SignalPattern && sortedPattern == other.sortedPattern

    override fun hashCode(): Int {
        return sortedPattern.hashCode()
    }
}
data class OutputValue(val signalPatterns: List<SignalPattern>)
data class Entry(val allSignalPatterns: List<SignalPattern>, val outputValue: OutputValue)

fun main() {

    fun parseSignalPatterns(signalPatterns: String): List<SignalPattern> =
        signalPatterns
            .split(" ")
            .map { SignalPattern(it.trim()) }

    fun parseEntry(line: String): Entry =
        line
            .split(""" | """)
            .let {
                Entry(
                    parseSignalPatterns(it[0]),
                    OutputValue(parseSignalPatterns(it[1]))
                )
            }

    fun part1(input: List<String>): Int {
        val entries = input
            .map { parseEntry(it) }

        return entries
            .map(Entry::outputValue)
            .flatMap(OutputValue::signalPatterns)
            .mapNotNull(SignalPattern::guessNumber)
            .count()
    }

    fun analyzeEntry(entry: Entry): String {
        val allSignalPatterns = entry.allSignalPatterns

        val p1 = allSignalPatterns.find { it.number == 1 }!!
        val p4 = allSignalPatterns.find { it.number == 4 }!!
        val p7 = allSignalPatterns.find { it.number == 7 }!!
        val p8 = allSignalPatterns.find { it.number == 8 }!!

        val p6 = allSignalPatterns
            .filter { it.pattern.length == 6 } // only patterns for 0, 6, 9 have 6 elements
            .find { it.pattern.toList().union(p1.pattern.toList()).size == 7 }!! // only p.f. 6 union p.f. 1 is p.f. 8
            .apply { number = 6 }

        val p9 = allSignalPatterns
            .filter { it.pattern.length == 6 } // only patterns for 0, 6, 9 have 6 elements
            .find { it.pattern.toList().union(p4.pattern.toList()).size == 6 }!! // only p.f. 9 union p.f. 4 still has 6 el.
            .apply { number = 9 }

        val p0 = allSignalPatterns
            .filter { it.pattern.length == 6 } // only patterns for 0, 6, 9 have 6 elements
            .find { it != p9 && it != p6 }!! // if not p.f. 6 or 9, then it is p.f. 0
            .apply { number = 0 }

        val p3 = allSignalPatterns
            .filter { it.pattern.length == 5 } // only patterns for 2, 3, 5 have 5 elements
            .find { it.pattern.toList().union(p1.pattern.toList()).size == 5 }!! // only p.f. 3 union p.f. 1 still has 5 el.
            .apply { number = 3 }

        val p2 = allSignalPatterns
            .filter { it.pattern.length == 5 } // only patterns for 2, 3, 5 have 5 elements
            .find { it.pattern.toList().union(p4.pattern.toList()).size == 7 }!! // only p.f. 2 union p.f. 4 is p.f. 8
            .apply { number = 2 }

        val p5 = allSignalPatterns
            .filter { it.pattern.length == 5 } // only patterns for 2, 3, 5 have 5 elements
            .find { it != p3 && it != p2 }!! // if not p.f. 3 or 2, then it is p.f. 5
            .apply { number = 5 }


        // allSignalPatterns.sortedBy { it.number }.forEach {
        //     println(it)
        // }

        // work on only for simple test input
        // check(p0.pattern == "cagedb")
        // check(p1.pattern == "ab")
        // check(p2.pattern == "gcdfa")
        // check(p3.pattern == "fbcad")
        // check(p4.pattern == "eafb")
        // check(p5.pattern == "cdfbe")
        // check(p6.pattern == "cdfgeb")
        // check(p8.pattern == "acedgfb")
        // check(p9.pattern == "cefabd")

        return entry.outputValue.signalPatterns
            .map { sp -> allSignalPatterns.find { otherSp -> sp == otherSp }!!.number}
            .joinToString("")
            .also{ println("Output $it") }
    }

    fun part2(input: List<String>): Int {
        return input
            .map(::parseEntry)
            .map(::analyzeEntry)
            .sumOf(String::toInt)
    }

    val simpleTestInput = listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
    check(part2(simpleTestInput) == 5353)

    // test if implementation meets criteria from the description
    val testInput = readInput("Day08_test")
    // part 1 test
    // check(part1(testInput) == 26)

    // part 2 test
    val testOutput = part2(testInput)
    println(testOutput)
    check(testOutput == 61229)

    val input = readInput("Day08")
    // part 1
    // println(part1(input))
    // check(part1(input) == 349812)

    // part 2
    val output = part2(input)
    println(output)
    check(output == 1041746)
}
