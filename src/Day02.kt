fun main() {
    data class Position(val horizontal: Int, val depth: Int) {
        fun move(pHorizontal: Int, pDepth: Int) = Position(horizontal + pHorizontal, depth + pDepth)
    }
    data class PositionWithAim(val horizontal: Int, val depth: Int, val aim: Int) {
        fun move(pHorizontal: Int) = PositionWithAim(horizontal + pHorizontal, depth + aim * pHorizontal, aim)
        fun pitch(pAim: Int) = PositionWithAim(horizontal, depth, aim + pAim)
    }

    val commandPattern = Regex("""^([a-z]*) (\d*)$""")

    fun calculatePosition(input: List<String>): Int =
        input
            .map { commandPattern.matchEntire(it) ?: throw RuntimeException("Row '${it}' is invalid") }
            .map { it.destructured }
            .fold(Position(0, 0)) { position, (command, value) ->
                when(command) {
                    "forward" -> position.move(value.toInt(), 0)
                    "down"    -> position.move(0, value.toInt())
                    "up"      -> position.move(0, - value.toInt())
                    else -> throw RuntimeException("Invalid command: $command")
                }
            }
            .let { (horizontal, depth) -> horizontal * depth }

    fun calculatePositionWithAim(input: List<String>): Int =
        input
            .map { commandPattern.matchEntire(it) ?: throw RuntimeException("Row '${it}' is invalid") }
            .map { it.destructured }
            .fold(PositionWithAim(0, 0, 0)) { position, (command, value) ->
                when(command) {
                    "forward" -> position.move(value.toInt())
                    "down"    -> position.pitch(value.toInt())
                    "up"      -> position.pitch(- value.toInt())
                    else -> throw RuntimeException("Invalid command: $command")
                }
            }
            .let { (horizontal, depth) -> horizontal * depth }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(calculatePosition(testInput) == 150)
    check(calculatePositionWithAim(testInput) == 900)

    val input = readInput("Day02")
    println(calculatePosition(input))
    println(calculatePositionWithAim(input))
}
