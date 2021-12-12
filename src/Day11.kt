import java.util.LinkedList
import java.util.Queue

typealias OGrid = List<MutableList<Int>>

fun main() {
    fun neighbours(grid: Grid, pR: Int, pC: Int) =
        listOf(
            Coords(pR+1, pC),
            Coords(pR, pC+1),
            Coords(pR-1, pC),
            Coords(pR, pC-1),
            // diagonals
            Coords(pR-1, pC-1),
            Coords(pR+1, pC+1),
            Coords(pR+1, pC-1),
            Coords(pR-1, pC+1),
    ).filter { (r, c) -> r >=0 && r < grid.size && c >=0 && c < grid[r].size }

    fun printGrid(grid: OGrid) {
        for(row in grid) {
            for(c in row) {
                print("$c ")
            }
            println()
        }
        println()
    }

    fun allFlashed(grid: Grid) =
        grid.sumOf(List<Int>::sum) == 0

    fun performStep(grid: OGrid, step: Int): Int {
        val flashes: Queue<Pair<Int, Int>> = LinkedList()
        val flashed = mutableSetOf<Pair<Int, Int>>()

        for ((rIdx, row) in grid.withIndex()) {
            for (cIdx in row.indices) {
                grid[rIdx][cIdx] += 1
                if (grid[rIdx][cIdx] == 10) flashes.add(rIdx to cIdx)
            }
        }

        while (flashes.isNotEmpty()) {
            val (r, c) = flashes.poll()
            for ((nR, nC) in neighbours(grid, r, c)) {
                grid[nR][nC] += 1
                if (grid[nR][nC] == 10) flashes.add(nR to nC)
            }
            flashed.add(r to c)
        }

        flashed.forEach { (r, c) ->
            grid[r][c] = 0
        }

        if(allFlashed(grid)) throw RuntimeException("All flashed at $step")

        return flashed.size
    }

    fun part1(input: List<String>, steps: Int): Int {
        val grid: OGrid = input.map { it.toList().map(Character::getNumericValue).toMutableList() }

        return (1..steps).fold(0) { flashCount, step ->
            flashCount + performStep(grid, step)
        }
    }

    fun part2(input: List<String>): String {
        try {
            part1(input, 1000)
        } catch (ex: RuntimeException) {
            return ex.message ?: "message missing"
        }

        return "no sync"
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day11_test")

    // part 1 test
    val testOutput = part1(testInput, 100)
    println("Test part 1: $testOutput")
    check(testOutput == 1656)

    // part 2 test
    val testOutput2 = part2(testInput)
    println("Test part 2: $testOutput2")
    check(testOutput2 == "All flashed at 195")


    val input = readInput("Day11")
    // part 1
    val output = part1(input, 100)
    println("Part 1: $output")
    check(output == 1661)

    // part 2
    val output2 = part2(input)
    println("Part 2: $output2")
    check(output2 == "All flashed at 334")
}
