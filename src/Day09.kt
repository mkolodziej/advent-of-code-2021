import java.util.Stack

typealias Grid = List<List<Int>>
typealias Coords = Pair<Int, Int>

fun main() {

    fun neighbours(grid: Grid, pR: Int, pC: Int) =
        listOf(
            Coords(pR+1, pC),
            Coords(pR, pC+1),
            Coords(pR-1, pC),
            Coords(pR, pC-1)
        ).filter { (r, c) -> r >=0 && r < grid.size && c >=0 && c < grid[r].size }

    fun neighboursValues(grid: Grid, pR: Int, pC: Int) =
        neighbours(grid, pR, pC)
            .map { (r, c) -> grid[r][c]}

    fun isLowPoint(grid: Grid, r: Int, c: Int) =
        grid[r][c] < neighboursValues(grid, r, c).minOrNull() ?: 0

    fun coords(rows: Int, cols: Int) =
        (0 until rows).flatMap { row ->
            (0 until cols).map { col ->
                Pair(row, col)
            }
        }

    fun part1(input: List<String>): Int {
        val grid = input
            .map { str -> str.toList().map(Character::getNumericValue) }

        val rows = grid.size
        val cols = grid[0].size

        return coords(rows, cols)
            .filter { (r, c) -> isLowPoint(grid, r, c) }
            //.also { println(it) }
            .sumOf { (r, c) -> 1 + grid[r][c] }
    }

    // BFS from given coords
    fun basinSize(grid: Grid, coords: Coords): Int {
        val visited = mutableSetOf<Coords>()
        val stack = Stack<Coords>()
        stack.push(coords)

        while(!stack.empty()) {
            val currentCoords = stack.pop()
            visited.add(currentCoords)

            val (r, c) = currentCoords
            neighbours(grid, r, c)
                .filterNot { visited.contains(it) } // filter out visited
                .filter { (nR, nC) -> grid[r][c] < grid[nR][nC] && grid[nR][nC] < 9 } // keep only larger, but not 9
                .map { stack.push(it) }
        }

        return visited.size
    }

    fun part2(input: List<String>): Int {
        val grid = input
            .map { str -> str.toList().map(Character::getNumericValue) }

        val rows = grid.size
        val cols = grid[0].size

        return coords(rows, cols)
            .asSequence() // https://youtrack.jetbrains.com/issue/KTIJ-6827
            .filter { (r, c) -> isLowPoint(grid, r, c) }
            .map { coords -> basinSize(grid, coords) }
            .sortedDescending()
            .take(3)
            .reduce { acc, size -> acc * size }
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day09_test")

    // part 1 test
    val testOutput = part1(testInput)
    println("Test part 1: $testOutput")
    check(testOutput == 15)

    // part 2 test
    val testOutput2 = part2(testInput)
    println("Test part 2: $testOutput2")
    check(testOutput2 == 1134)

    val input = readInput("Day09")
    // part 1
    val output = part1(input)
    println("Part 1: $output")
    // check(output == 349812)

    // part 2
    val output2 = part2(input)
    println("Part 2: $output2")
    check(output2 == 1564640)
}
