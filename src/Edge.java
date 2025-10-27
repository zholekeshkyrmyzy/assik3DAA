public class Edge implements Comparable<Edge> {
    String from;
    String to;
    int weight;

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("(%s - %s : %d)", from, to, weight);
    }
}