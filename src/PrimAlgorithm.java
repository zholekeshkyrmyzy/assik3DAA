import java.util.*;

public class PrimAlgorithm {

    public static class Result {
        List<Edge> mstEdges;
        int totalCost;
        int operations;
        double executionTimeMs;

        public Result(List<Edge> mstEdges, int totalCost, int operations, double executionTimeMs) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.operations = operations;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static Result primMST(Graph graph) {
        long start = System.nanoTime();

        Map<String, List<Edge>> adj = graph.buildAdjacencyList();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        int operations = 0;

        String startNode = graph.nodes.get(0);
        visited.add(startNode);
        pq.addAll(adj.get(startNode));

        while (!pq.isEmpty() && visited.size() < graph.nodes.size()) {
            Edge edge = pq.poll();
            operations++;

            if (!visited.contains(edge.to)) {
                visited.add(edge.to);
                mstEdges.add(edge);
                totalCost += edge.weight;

                for (Edge next : adj.get(edge.to)) {
                    if (!visited.contains(next.to)) {
                        pq.offer(next);
                        operations++;
                    }
                }
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        return new Result(mstEdges, totalCost, operations, Math.round(timeMs * 100.0) / 100.0);
    }
}