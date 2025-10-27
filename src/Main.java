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
                PrimAlgorithm.Result result = PrimAlgorithm.primMST(graph);

                JSONObject graphResult = new JSONObject();
                graphResult.put("graph_id", id);

                JSONObject inputStats = new JSONObject();
                inputStats.put("vertices", nodes.size());
                inputStats.put("edges", edges.size());
                graphResult.put("input_stats", inputStats);

                JSONObject primResult = new JSONObject();
                JSONArray mstEdgesArray = new JSONArray();
                for (Edge e : result.mstEdges) {
                    JSONObject edgeJson = new JSONObject();
                    edgeJson.put("from", e.from);
                    edgeJson.put("to", e.to);
                    edgeJson.put("weight", e.weight);
                    mstEdgesArray.add(edgeJson);
                }

                primResult.put("mst_edges", mstEdgesArray);
                primResult.put("total_cost", result.totalCost);
                primResult.put("operations_count", result.operations);
                primResult.put("execution_time_ms", result.executionTimeMs);
                graphResult.put("prim", primResult);

                resultsArray.add(graphResult);

                System.out.println("Graph ID: " + id);
                System.out.println("MST edges: " + result.mstEdges);
                System.out.println("Total cost: " + result.totalCost);
                System.out.println("Operations: " + result.operations);
                System.out.println("Time (ms): " + result.executionTimeMs);
                System.out.println("-----------------------------");
            }

            JSONObject output = new JSONObject();
            output.put("results", resultsArray);

            try (FileWriter file = new FileWriter("output.json")) {
                file.write(output.toJSONString());
                file.flush();
            }

            System.out.println("Results saved to output.json");

        } catch (IOException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}