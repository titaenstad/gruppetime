import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

class BellmanFord {
    public static void main(String[] args) {
        BFGraph graph = BFGraph.buildGraph(4);
        graph.printGraph();
        System.out.println("");
        graph.printBellmanFord("3");
    }
}

class BFGraph {
    HashMap<String, Vertex> vertices;
    HashSet<Edge> edges;

    BFGraph() {
        vertices = new HashMap<String, Vertex>();
        edges = new HashSet<Edge>();
    }

    private void addEdge(String from, String to, int weight) {
        Vertex fromVertex = vertices.get(from);
        Vertex toVertex = vertices.get(to);

        if (fromVertex == null || toVertex == null) {
            System.err.printf(" %s : %s",
            "Graph.addEdge", "could not find both vertices\n");
            return;
        }
        if (fromVertex == toVertex) {
            System.out.println("Adds self-loop");
        }
        Edge e = new Edge(fromVertex, toVertex, weight);
        edges.add(e);
    }

    private void addVertex(String id) {
        vertices.put(id, new Vertex(id));
    }

    public static BFGraph buildGraph(int size) {
        BFGraph g = new BFGraph();
        Random r = new Random();
        for (int i=0; i<size; i++) {
            g.addVertex(""+i);
        }
        for (int i=0; i<2*size; i++) {
            int from = r.nextInt(size);
            int to = r.nextInt(size);
            int weight = r.nextInt(50);
            if (i < size/2) {
                weight = -weight;
            }
            while (from == to) {
                to = r.nextInt(size);
            }
            g.addEdge(""+from, ""+to, weight);
        }
        return g;
    }

    public void printGraph() {
        for (Edge e : edges) {
            System.out.println(e);
        }
    }

    // Bellman-Ford
    // Works on directed graphs with positive/negative weights, as long as there are no negative cycles.
    // distance = Integer.MAX_VALUE-50 for unconnected vertices
    public void printBellmanFord(String startId) {
        Vertex startVertex = vertices.get(startId);

        for (Vertex v : vertices.values()) {
            v.distance = Integer.MAX_VALUE-50;
        }
        startVertex.distance = 0;
        for (int i=0; i< vertices.size()-1; i++) {
            for (Edge e: edges) {
                if (e.destination.distance > (e.origin.distance + e.weight)) {
                    e.destination.distance = e.origin.distance + e.weight;
                }
            }
        }
        for (Edge e: edges) {
            if (e.destination.distance > (e.origin.distance + e.weight)) {
                System.err.println("Graph has a cycle of negative weights");
                return;
            }
        }
        System.out.println("Start vertex: " + startVertex);
        for (Vertex v : vertices.values()) {
            System.out.println(v + ", Distance to start vertex: " + v.distance);
        }
    }

    class Vertex {
        String id;
        int distance;

        Vertex(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    class Edge {
        Vertex origin, destination;
        int weight;

        Edge(Vertex v1, Vertex v2, int weight) {
            origin = v1;
            destination = v2;
            this.weight = weight;
        }

        public String toString() {
            return "(" + origin + ", " + destination + "), weight: " + weight;
        }
    }
}