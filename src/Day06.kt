fun main() {

    fun part1(input: List<String>, days: Int): Int {
        val lanternfish = input.first().split(',').map(String::toInt).toMutableList()

        for(day in 1..days) {
            for(idx in lanternfish.indices) {
                when(lanternfish[idx]) {
                    0 -> { lanternfish.add(8); lanternfish[idx] = 6 }
                    else -> { lanternfish[idx]-- }
                }
            }
        }

        return lanternfish.size
    }

    fun part2(input: List<String>, days: Int): Long {
        var fishAges = input.first()
            //.also { println("Input: $it") }
            .split(',')
            .map(String::toInt)
            .fold(MutableList(9) { 0L }) { acc, it -> acc[it]++; acc }
            //.also { println("Fish ages: $it") }

        for(day in 1..days) {
            val newFishAges = MutableList(9) { 0L }
            val spawning = fishAges[0]
            for(idx in 1..8) {
                newFishAges[idx-1] = fishAges[idx]
            }
            newFishAges[6] += spawning
            newFishAges[8] = spawning
            // println("$fishAges -> $newFishAges")
            fishAges = newFishAges
        }

        return fishAges.fold(0L) { acc, count -> acc + count }
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day06_test")
    // part 1 test
    println(part1(testInput, 80))
    check(part1(testInput, 80) == 5934)

    // part 2 test
    println(part2(testInput, 80))
    check(part2(testInput, 80) == 5934L)

    val testOutput256 = part2(testInput, 256)
    println(testOutput256)
    check(testOutput256 == 26984457539)

    val input = readInput("Day06")
    // part 1
    println(part1(input, 80))
    check(part1(input, 80) == 351188)

    // part 2
    val output256 = part2(input, 256)
    println(output256)
}
