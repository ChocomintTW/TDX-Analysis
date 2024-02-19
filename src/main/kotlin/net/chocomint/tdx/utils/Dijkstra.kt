package net.chocomint.tdx.utils

@Credit(
    author = "Trygve Matland Amundsen",
    url = "https://gist.github.com/trygvea/6067a744ee67c2f0447c3c7f5b715d62"
)
object Dijkstra {

    interface Node

    data class Edge(val node1: Node, val node2: Node, val distance: Double)

    /**
     * See https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     */
    fun findShortestPath(edges: List<Edge>, source: Node, target: Node): ShortestPathResult {

        // Note: this implementation uses similar variable names as the algorithm given do.
        // We found it more important to align with the algorithm than to use possibly more sensible naming.

        val dist = mutableMapOf<Node, Double>()
        val prev = mutableMapOf<Node, Node?>()
        val q = findDistinctNodes(edges)

        q.forEach { v ->
            dist[v] = Double.POSITIVE_INFINITY
            prev[v] = null
        }
        dist[source] = 0.0

        while (q.isNotEmpty()) {
            val u = q.minByOrNull { dist[it] ?: 0.0 }
            q.remove(u)

            if (u == target) {
                break // Found the shortest path to target
            }
            edges
                .filter { it.node1 == u }
                .forEach { edge ->
                    val v = edge.node2
                    val alt = (dist[u] ?: 0.0) + edge.distance
                    if (alt < (dist[v] ?: 0.0)) {
                        dist[v] = alt
                        prev[v] = u
                    }
                }
        }

        return ShortestPathResult(prev, dist, source, target)
    }

    private fun findDistinctNodes(edges: List<Edge>): MutableSet<Node> {
        val nodes = mutableSetOf<Node>()
        edges.forEach {
            nodes.add(it.node1)
            nodes.add(it.node2)
        }
        return nodes
    }

    /**
     * Traverse result
     */
    class ShortestPathResult(val prev: Map<Node, Node?>, val dist: Map<Node, Double>, val source: Node, val target: Node) {

        fun shortestPath(from: Node = source, to: Node = target, list: List<Node> = emptyList()): List<Node> {
            val last = prev[to] ?: return if (from == to) {
                list + to
            } else {
                emptyList()
            }
            return shortestPath(from, last, list) + to
        }

        fun shortestDistance(): Double? {
            val shortest = dist[target]
            if (shortest == Double.POSITIVE_INFINITY) {
                return null
            }
            return shortest
        }
    }
}
