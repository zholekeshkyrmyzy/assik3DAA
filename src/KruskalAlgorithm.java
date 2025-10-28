import java.util.*;

public class KruskalAlgorithm {

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

    static class DisjointSet {
        Map<String, String> parent = new HashMap<>();

        public DisjointSet(List<String> nodes) {
            for (String node : nodes) parent.put(node, node);
        }

        String find(String node) {
            if (!parent.get(node).equals(node))
                parent.put(node, find(parent.get(node)));
            return parent.get(node);
        }

        void union(String a, String b) {
            parent.put(find(a), find(b));
        }
    }

    public static Result kruskalMST(Graph graph) {
        long start = System.nanoTime();
        List<Edge> edges = new ArrayList<>(graph.edges);
        Collections.sort(edges);

        DisjointSet ds = new DisjointSet(graph.nodes);
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        int operations = 0;

        for (Edge edge : edges) {
            operations++;
            String root1 = ds.find(edge.from);
            String root2 = ds.find(edge.to);

            if (!root1.equals(root2)) {
                ds.union(root1, root2);
                mstEdges.add(edge);
                totalCost += edge.weight;
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        return new Result(mstEdges, totalCost, operations, Math.round(timeMs * 100.0) / 100.0);
    }
}
