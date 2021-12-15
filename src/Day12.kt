import kotlinx.collections.immutable.*

data class Node(val name: String) {
    // probably unnecessary for a data class?
    override fun hashCode(): Int = name.hashCode()
    override fun equals(other: Any?): Boolean = other is Node && name == other.name
}
typealias Path = List<Node>

fun String.isLowerCase() = this.toList().all { it.isLowerCase() }

class Graph(private val isVisited: (Node, Map<Node, Int>, Node) -> Boolean) {
    private val nodeSet = mutableSetOf<Node>()
    private val adjList = mutableMapOf<Node, MutableList<Node>>()

    fun addEdge(n1: Node, n2: Node): Graph {
        nodeSet.addAll(listOf(n1, n2))
        adjList.getOrPut(n1) { mutableListOf() }.add(n2)
        adjList.getOrPut(n2) { mutableListOf() }.add(n1) // bi-directional edge
        return this
    }

    fun paths(start: Node, end: Node): List<Path> {
        val isVisited = persistentMapOf<Node, Int>()
        val path = persistentListOf(start)
        val allPaths = mutableListOf<List<Node>>()

        nodeSet
            .filter { it.name.isLowerCase() }
            .forEach { luckySmallCave ->
                pathsRecursive(start, end, isVisited, path, allPaths, luckySmallCave)
            }

        return allPaths.distinct()
    }

    override fun toString(): String =
        adjList.map { "${it.key} -> ${it.value}" }.joinToString("\n")

    private fun pathsRecursive(start: Node, end: Node, isVisited: Map<Node, Int>, path: Path, allPaths: MutableList<Path>, luckySmallCave: Node) {
        if(start == end) {
            allPaths.add(path)
            return
        }

        for(node in adjList[start] ?: emptyList()) {
            if(!isVisited(node, isVisited, luckySmallCave)) {
                pathsRecursive(node, end, isVisited.plus(start to isVisited.getOrDefault(start, 0) + 1), path + node, allPaths, luckySmallCave)
            }
        }
    }
}

fun main() {

    fun loadGraph(input: List<String>, isVisited: (Node, Map<Node, Int>, luckySmallCave: Node) -> Boolean) = input
        .map { it.split('-') }
        .fold(Graph(isVisited)) { graph, (n1, n2) -> graph.addEdge(Node(n1), Node(n2)) }

    fun isVisited1(node: Node, isVisited: Map<Node, Int>, luckySmallCave: Node): Boolean {
        val isSmallCave = node.name.isLowerCase()
        val visitedCount = isVisited.getOrDefault(node, 0)

        return isSmallCave && visitedCount > 0
    }

    fun part1(input: List<String>): Int {
        val graph = loadGraph(input, ::isVisited1)

        return graph.paths(Node("start"), Node("end"))
            .also { println(it.joinToString("\n") { path -> path.joinToString(",") { n -> n.name } }) }
            .size
    }

    fun isVisited2(node: Node, isVisited: Map<Node, Int>, luckySmallCave: Node): Boolean {
        val isSmallCave = node.name.isLowerCase()
        val visitedCount = isVisited.getOrDefault(node, 0)

        return ((node.name == "start" || node.name == "end") && visitedCount > 0) || // "start" and "end" caves can be visited once
            (isSmallCave && luckySmallCave.name == node.name && visitedCount > 1) || // a single small cave can be visited twice
            (isSmallCave && luckySmallCave.name != node.name && visitedCount > 0) // other small caves can be visited once
    }

    fun part2(input: List<String>): Int {
        val graph = loadGraph(input, ::isVisited2)

        return graph.paths(Node("start"), Node("end"))
            .also { println(it.joinToString("\n") { path -> path.joinToString(",") { n -> n.name } }) }
            .size
    }

    // test if implementation meets criteria from the description

    // part 1 test 0
    val testInput = readInput("Day12_test")
//    val testOutput = part1(testInput)
    //println("Test 0 part 1: $testOutput")
//    check(testOutput == 10)
    // part 2 test 0
    val testOutput_2 = part2(testInput)
    println("Test 0 part 2: $testOutput_2")
    check(testOutput_2 == 36)

    // part 1 test 1
//    val testInput1 = readInput("Day12_test1")
//    val testOutput1 = part1(testInput1)
    //println("Test 1 part 1: $testOutput1")
//    check(testOutput1 == 19)
    // part 2 test 1
//    val testOutput1_2 = part2(testInput1)
//    println("Test 1 part 2: $testOutput1_2")
//    check(testOutput1_2 == 103)

    // part 1 test 2
//    val testInput2 = readInput("Day12_test2")
//    val testOutput2 = part1(testInput2)
//    //println("Test 2 part 1: $testOutput2")
//    check(testOutput2 == 226)


    val input = readInput("Day12")
    // part 1
//    val output = part1(input)
    //println("Part 1: $output")
    // check(output == 349812)
//
//    // part 2
    val output2 = part2(input)
    println("Part 2: $output2")
    check(output2 == 1564640)
}
