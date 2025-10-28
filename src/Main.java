import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("input.json"));
            JSONObject inputData = (JSONObject) obj;
            JSONArray graphsArray = (JSONArray) inputData.get("graphs");

            JSONArray resultsArray = new JSONArray();

            for (Object g : graphsArray) {
                JSONObject graphJson = (JSONObject) g;
                long id = (Long) graphJson.get("id");

                List<String> nodes = new ArrayList<>();
                JSONArray jsonNodes = (JSONArray) graphJson.get("nodes");
                for (Object node : jsonNodes) nodes.add((String) node);

                List<Edge> edges = new ArrayList<>();
                JSONArray jsonEdges = (JSONArray) graphJson.get("edges");
                for (Object e : jsonEdges) {
                    JSONObject edgeObj = (JSONObject) e;
                    String from = (String) edgeObj.get("from");
                    String to = (String) edgeObj.get("to");
                    int weight = ((Long) edgeObj.get("weight")).intValue();
                    edges.add(new Edge(from, to, weight));
                }

                Graph graph = new Graph(nodes, edges);

                PrimAlgorithm.Result primResult = PrimAlgorithm.primMST(graph);
                KruskalAlgorithm.Result kruskalResult = KruskalAlgorithm.kruskalMST(graph);

                JSONObject graphResult = new JSONObject();
                graphResult.put("graph_id", id);

                JSONObject inputStats = new JSONObject();
                inputStats.put("vertices", nodes.size());
                inputStats.put("edges", edges.size());
                graphResult.put("input_stats", inputStats);

                // --- Prim Result ---
                JSONObject primJson = new JSONObject();
                JSONArray primEdgesArray = new JSONArray();
                for (Edge e : primResult.mstEdges) {
                    JSONObject edgeJson = new JSONObject();
                    edgeJson.put("from", e.from);
                    edgeJson.put("to", e.to);
                    edgeJson.put("weight", e.weight);
                    primEdgesArray.add(edgeJson);
                }
                primJson.put("mst_edges", primEdgesArray);
                primJson.put("total_cost", primResult.totalCost);
                primJson.put("operations_count", primResult.operations);
                primJson.put("execution_time_ms", primResult.executionTimeMs);
                graphResult.put("prim", primJson);

                // --- Kruskal Result ---
                JSONObject kruskalJson = new JSONObject();
                JSONArray kruskalEdgesArray = new JSONArray();
                for (Edge e : kruskalResult.mstEdges) {
                    JSONObject edgeJson = new JSONObject();
                    edgeJson.put("from", e.from);
                    edgeJson.put("to", e.to);
                    edgeJson.put("weight", e.weight);
                    kruskalEdgesArray.add(edgeJson);
                }
                kruskalJson.put("mst_edges", kruskalEdgesArray);
                kruskalJson.put("total_cost", kruskalResult.totalCost);
                kruskalJson.put("operations_count", kruskalResult.operations);
                kruskalJson.put("execution_time_ms", kruskalResult.executionTimeMs);
                graphResult.put("kruskal", kruskalJson);

                // --- Compare ---
                boolean sameCost = primResult.totalCost == kruskalResult.totalCost;
                graphResult.put("same_total_cost", sameCost);

                resultsArray.add(graphResult);

                System.out.println("Graph ID: " + id);
                System.out.println("Prim MST cost: " + primResult.totalCost + " | Kruskal MST cost: " + kruskalResult.totalCost);
                System.out.println("Prim Ops: " + primResult.operations + " | Kruskal Ops: " + kruskalResult.operations);
                System.out.println("Prim Time: " + primResult.executionTimeMs + " ms | Kruskal Time: " + kruskalResult.executionTimeMs + " ms");
                System.out.println("Same cost? " + (sameCost ? "✅ YES" : "❌ NO"));
                System.out.println("-----------------------------");
            }

            JSONObject output = new JSONObject();
            output.put("results", resultsArray);

            try (FileWriter file = new FileWriter("output.json")) {
                file.write(output.toJSONString());
                file.flush();
            }

            System.out.println("✅ Results saved to output.json");

        } catch (IOException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}