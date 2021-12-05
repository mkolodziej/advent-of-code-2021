fun main() {

    fun transpose(rows: List<String>): List<CharSequence> {
        if(rows.isEmpty()) throw IllegalArgumentException("At least 1 row is required")

        val columns = ArrayList<StringBuilder>()
        columns.addAll((1..rows[0].length).map { StringBuilder() })

        for(row in rows) {
            for(cIdx in row.indices) {
                columns[cIdx].append(row[cIdx])
            }
        }

        return columns
    }

    fun mostCommonBit(sequence: CharSequence): Char {
        var zeroes = 0
        var ones = 0

        for(element in sequence) {
            if(element == '1')
                ones++
            else
                zeroes++
        }

        return if(ones >= zeroes) '1' else '0'
    }

    fun leastCommonBit(sequence: CharSequence): Char =
        if(mostCommonBit(sequence) == '1') '0' else '1'

    fun calculatePowerConsumption(input: List<String>): Int {
        val columns = transpose(input)

        val gammaRate = columns
            .fold(StringBuilder()) { sb, column -> sb.append(mostCommonBit(column)) }

        val epsilonRate = gammaRate
            .map { ch -> if(ch == '1') '0' else '1' }
            .fold(StringBuilder()) { sb, ch -> sb.append(ch) }

        return gammaRate.toString().toInt(2) * epsilonRate.toString().toInt(2)
    }

    fun calculateRating(input: List<String>, bitSelector: (String) -> Char): Int {
        var currentResult = input

        for (currentBitPosition in 0 until input[0].length) {
            val currentColumn = currentResult.map { str -> str[currentBitPosition] }.joinToString("")
            val selectedBit = bitSelector(currentColumn)
            currentResult = currentResult.filter { row -> row[currentBitPosition] == selectedBit }

            if(currentResult.size < 2) break
        }

        return currentResult[0].toInt(2)
    }

    fun oxygenGeneratorRating(input: List<String>): Int =
        calculateRating(input, ::mostCommonBit)

    fun cO2ScrubberRating(input: List<String>): Int =
        calculateRating(input, ::leastCommonBit)

    fun lifeSupportRating(input: List<String>): Int =
        oxygenGeneratorRating(input) * cO2ScrubberRating(input)

    // test if implementation meets criteria from the description
    val testInput = readInput("Day03_test")
    // part 1 test
    check(calculatePowerConsumption(testInput) == 198)
    // part 2 test
    check(lifeSupportRating(testInput) == 230)

    val input = readInput("Day03")
    // part 1
    val powerConsumption = calculatePowerConsumption(input)
    println(powerConsumption)
    check(powerConsumption == 1307354)
    // part 2
    val lSR = lifeSupportRating(input)
    println(lSR)
    check(lSR == 482500)
}
