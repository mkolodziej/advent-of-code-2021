class Board(private val fields: List<List<Field>>) {
    data class Field(var value: Int, var marked: Boolean = false)
    data class Bingo(val score: Int, val lastNumber: Int, val board: Board)
    data class Pos(val row: Int, val col: Int)

    private val markedFieldsByRows = MutableList<Int>(fields.size) { 0 }
    private val markedFieldsByColumns = MutableList<Int>(fields.size) { 0 }

    fun mark(value: Int): Bingo? {
        val markedIdx = findFieldByValue(value) { pos, field ->
            field.marked = true
            markedFieldsByColumns[pos.col]++
            markedFieldsByRows[pos.row]++
        }

        return if(markedIdx != null && bingo(markedIdx)) Bingo(score(value), value, this) else null
    }

    private fun bingo(markedIdx: Pos) = columnFull(markedIdx.col) || rowFull(markedIdx.row)
    private fun columnFull(colIdx: Int): Boolean = markedFieldsByColumns[colIdx] == fields.size
    private fun rowFull(rowIdx: Int): Boolean = markedFieldsByRows[rowIdx] == fields.size
    private fun score(lastMarked: Int): Int =
        fields.fold(0) { sum, row ->
            sum + row.filterNot { it.marked }.sumOf { it.value }
        } * lastMarked

    private fun findFieldByValue(value: Int, lambda: (pos: Pos, field: Field) -> Unit): Pos? {
        for((rowIdx, row) in fields.withIndex()) {
            for ((colIdx, field) in row.withIndex()) {
                if(field.value == value) {
                    val pos = Pos(rowIdx, colIdx)
                    lambda(pos, field)
                    return pos
                }
            }
        }

        return null
    }
}


fun main() {

    fun createBoard(boardData: List<String>): Board =
        Board(boardData.map { line ->
            line.trim().split(Regex(""" +""")).map { value -> Board.Field(value.toInt()) }
        })

    fun createBoards(input: List<String>): List<Board> = input.drop(1)
        .fold(mutableListOf<MutableList<String>>()) { boardsData, line ->
            if (line.isBlank()) {
                boardsData.add(mutableListOf())
            } else {
                boardsData.last().add(line)
            }
            boardsData
        }
        .map { boardData -> createBoard(boardData) }

    fun playBingo(input: List<String>): Int {
        val numbers = input[0].split(',')
        val boards = createBoards(input)

        for(number in numbers) {
            for(board in boards) {
                val bingo = board.mark(number.toInt())
                if(bingo != null) {
                    return bingo.score
                }
            }
        }

        return -1
    }
    fun playBingoToLose(input: List<String>): Int {
        val numbers = input[0].split(',')
        val boards = createBoards(input).toMutableList()

        for(number in numbers) {
            val currBoards = boards.toMutableList()
            for(board in currBoards) {
                val bingo = board.mark(number.toInt())
                if(bingo != null) {
                    boards.remove(board)
                    if(boards.size == 0) return bingo.score
                }
            }
        }

        return -1
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day04_test")
    // part 1 test
    println(playBingo(testInput))
    check(playBingo(testInput) == 4512)
    // part 2 test
    println(playBingoToLose(testInput))
    check(playBingoToLose(testInput) == 1924)

    val input = readInput("Day04")
    // part 1
    println(playBingo(input))
    // part 2
    println(playBingoToLose(input))
}
