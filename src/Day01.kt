fun main() {

    fun countMeasurementIncreases(input: List<Int>): Int {
        return input
            .windowed(2, 1)
            .map { if(it.last() > it.first()) 1 else 0 }
            .sum()
    }

    fun countSumOfSlidingWindow3Increases(input: List<Int>): Int {
        return countMeasurementIncreases(input
            .windowed(3, 1)
            .map { it.sum() }
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").map(String::toInt)
    check(countMeasurementIncreases(testInput) == 7)
    check(countSumOfSlidingWindow3Increases(testInput) == 5)

    val input = readInput("Day01").map(String::toInt)
    println(countMeasurementIncreases(input))
    println(countSumOfSlidingWindow3Increases(input))
}
