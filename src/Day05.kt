fun main() {
    // range with auto direction
    fun span(a: Int, b: Int): IntProgression = if(a < b) a..b else a.downTo(b)

    data class Point(val x: Int, val y: Int) {
        override fun toString() = "($x, $y)"
    }
    data class Line(val start: Point, val end: Point) {
        override fun toString() = "$start -> $end"

        fun diagonal(): Boolean =
            start.x != end.x && start.y != end.y
    }

    class Grid() {
        val rows = mutableListOf<MutableList<Int>>()

        fun put(point: Point): Grid {
            if(rows.size <= point.y) expandRowsTo(point.y + 1)
            if(rows[point.y].size <= point.x) expandColumnsTo(point.x + 1)

            rows[point.y][point.x] += 1

            return this
        }

        override fun toString(): String {
            val output = StringBuilder()

            for(row in rows) {
                for(value in row) {
                    output.append(if(value == 0) '.' else value)
                }
                output.appendLine()
            }

            return output.toString()
        }

        fun dangerousPointsCount(): Int = rows.sumOf { row -> row.count { it >= 2 } }

        private fun expandRowsTo(count: Int) {
            val toAdd = count - rows.size
            rows.addAll((1..toAdd).map { mutableListOf() })
        }
        private fun expandColumnsTo(count: Int) {
            for (row in rows) {
                row.addAll((1..count - row.size).map { 0 })
            }
        }
    }

    val lineRegex = Regex("""(\d*),(\d*) -> (\d*),(\d*)""")

    fun parseLine(line: String): Line {
        val (x1, y1, x2, y2) = lineRegex.matchEntire(line)?.destructured
            ?: throw RuntimeException("Invalid line format: $line")

        return Line(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
    }

    // works only for straight lines
    fun lineToPointsList(line: Line): List<Point> =
        when {
            line.start.x == line.end.x -> span(line.start.y, line.end.y).map { Point(line.start.x, it) }
            line.start.y == line.end.y -> span(line.start.x, line.end.x).map { Point(it, line.start.y) }
            else -> span(line.start.x, line.end.x).zip(span(line.start.y, line.end.y)).map { (x, y) -> Point(x, y) }
        }


    fun part1(input: List<String>): Int {
        val grid = input
            .map(::parseLine)
            // .also { println("Lines: $it") }
            .filterNot(Line::diagonal)
            // .also { println("Not diagonal lines: $it") }
            .flatMap(::lineToPointsList)
            // .also { println(it) }
            .fold(Grid()) { grid, point -> grid.put(point) }

        // println(grid)

        return grid.dangerousPointsCount()
    }

    fun part2(input: List<String>): Int {
        val grid = input
            .map(::parseLine)
            // .also { println("Lines: $it") }
            .flatMap(::lineToPointsList)
            // .also { println(it) }
            .fold(Grid()) { grid, point -> grid.put(point) }

        // println(grid)

        return grid.dangerousPointsCount()
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day05_test")
    // part 1 test
    //println(part1(testInput))
    // check(part1(testInput) == 5)
    // part 2 test
    println(part2(testInput))
    // check(part2(testInput) == 1924)

    val input = readInput("Day05")
    // part 1
    // println(part1(input))
    // part 2
    println(part2(input))
}
