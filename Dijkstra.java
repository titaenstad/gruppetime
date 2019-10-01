import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

class Dijkstra {

    public static void main(String[] args) {
        DijGraph graph = DijGraph.buildGraph(4);
        graph.printVertices();
        graph.printEdges();
        graph.printDijkstra("3");
    }
}

class DijGraph {
        HashMap<String, Vertex> vertices;

        private DijGraph() {
            vertices = new HashMap<String, Vertex>();
        }

        private void addVertex(String id){
            vertices.put(id, new Vertex(id));
        }

        private void addEdge(String from, String to, int weight) {
            Vertex fromVertex = vertices.get(from);
            Vertex toVertex = vertices.get(to);

            if (fromVertex == null || toVertex == null) {
                System.err.printf(" %s : %s",
                "DijGraph.addEdge", "could not find both nodes\n");
                return;
            }
            if (fromVertex == toVertex) {
                System.out.println("Adds self-loop");
            }
            fromVertex.addEdge(toVertex, weight);
        }

        public static DijGraph buildDirGraph(int size) {
            DijGraph g = new DijGraph();
            Random r = new Random();
            for (int i=0; i<size; i++) {
                g.addVertex(""+i);
            }
            for (int i=0; i<2*size; i++) {
                int from = r.nextInt(size);
                int to = r.nextInt(size);
                while (from == to) {
                    to = r.nextInt(size);
                }
                g.addEdge(""+from, ""+to, r.nextInt(10));
            }
            return g;
        }

        public static DijGraph buildGraph(int size) {
            DijGraph g = new DijGraph();
            Random r = new Random();
            for (int i=0; i<size; i++) {
                g.addVertex(""+i);
            }
            for (int i=0; i<2*size; i++) {
                int from = r.nextInt(size);
                int to = r.nextInt(size);
                int weight = r.nextInt(10);
                while (from == to) {
                    to = r.nextInt(size);
                }
                g.addEdge(""+from, ""+to, weight);
                g.addEdge(""+to, ""+from, weight);
            }
            return g;
        }

        public void printEdges() {
            for (Vertex n : vertices.values()) {
                for (Vertex neigh : n.neighbours.keySet()) {
                    System.out.println("(" + n.id + ", " + neigh.id + ")" + " weight: " + n.neighbours.get(neigh));
                }
            }
        }

        public void printVertices() {
            for (Vertex n : vertices.values()) {
                String str = "";
                str += n + "\n Neighbours: ";
                for (Vertex neigh : n.neighbours.keySet()) {
                    str += neigh.id + " ";
                }
                System.out.println(str);
                System.out.println("");
            }
        }

        // DIJKSTRA
        // Works on weighted graphs with positive weights.
        // distance = Integer.MAX_VALUE for unconnected vertices
        public void printDijkstra(String startId) {
            Vertex start = vertices.get(startId);
            for (Vertex n : vertices.values()) {
                n.distance = Integer.MAX_VALUE;
            }
            start.distance = 0;

            PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(vertices.values());
            while (!queue.isEmpty()) {
                Vertex v = queue.poll();
                for (Vertex n : v.neighbours.keySet()) {
                    if (queue.contains(n)) {
                        if (n.distance > v.distance + v.neighbours.get(n)) {
                            n.distance = v.distance + v.neighbours.get(n);
                            queue.remove(n);
                            queue.add(n);
                        }
                    }
                }
             }

             System.out.println("Start node:" + start);
             for (Vertex n : vertices.values()) {
                 System.out.println(n + ", distance from start: " + n.distance);
             }
        }

        class Vertex implements Comparable<Vertex> {
            String id;
            HashMap<Vertex, Integer> neighbours;
            boolean visited;
            int distance;

            Vertex(String id){
                this.id = id;
                neighbours = new HashMap<Vertex, Integer>();
                visited = false;
            }

            public void addEdge(Vertex to, int weight){
                neighbours.put(to, weight);
            }

            public HashMap<Vertex, Integer> getNeighbours(){
                return neighbours;
            }

            public String toString(){
                return " Vertex id: " + id;
            }

            public int compareTo(Vertex other) {
                if (other.distance > distance) {
                    return -1;
                }
                else if (other.distance < distance) {
                    return 1;
                }
                return 0;
            }
        }
    }
