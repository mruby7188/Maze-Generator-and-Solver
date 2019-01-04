package misc.graphs;

import datastructures.concrete.DoubleLinkedList;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import datastructures.concrete.ChainedHashSet;
import misc.exceptions.NoPathExistsException;
import misc.Searcher;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    private IDictionary<V, IList<E>> graph;
    private IList<E> edges;
    private IDisjointSet<V> vertices;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.graph = new ChainedHashDictionary<V, IList<E>>();
        this.edges = Searcher.topKSort(edges.size(), edges);
        this.vertices = new ArrayDisjointSet<V>();
        for (V vertex : vertices) {
            this.graph.put(vertex, new DoubleLinkedList<E>());
            this.vertices.makeSet(vertex);
        }
        for (E edge : edges) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            if (!graph.containsKey(edge.getVertex1()) || !graph.containsKey(edge.getVertex2())) {
                throw new IllegalArgumentException();
            }
            graph.get(edge.getVertex1()).add(edge);
            if (!edge.getVertex1().equals(edge.getVertex2())) {
                graph.get(edge.getVertex2()).add(edge);
            }
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return graph.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> minEdges = new ChainedHashSet<E>();
        IDisjointSet<V> vertexSet = this.vertices;
        for (E edge : this.edges) {
            V vertex = edge.getVertex1();
            if (vertexSet.findSet(vertex) != vertexSet.findSet(edge.getOtherVertex(vertex))) {
                minEdges.add(edge);
                vertexSet.union(vertex, edge.getOtherVertex(vertex));
            }
        }
        return minEdges;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IList<E> output = new DoubleLinkedList<>();
        // mark all as unprocessed
        ISet<V> unprocessed = new ChainedHashSet<V>();
        IDictionary<V, E> predecessor = new ChainedHashDictionary<V, E>();
        IPriorityQueue<EdgeDistance> minDist = new ArrayHeap<EdgeDistance>();
        IDictionary<V, EdgeDistance> distances = new ChainedHashDictionary<V, EdgeDistance>();
        for (KVPair<V, IList<E>> vertex : graph) {
            // initialize distances to infinity, 
            EdgeDistance curr = new EdgeDistance(vertex.getKey(), Double.POSITIVE_INFINITY);
            // mark source as distance 0 and add to minHeap
            if (vertex.getKey().equals(start)) {
                curr.setWeight(0.0);
                minDist.insert(curr); 
            } 
            unprocessed.add(vertex.getKey());
            distances.put(vertex.getKey(), curr);
        }
        while (!minDist.isEmpty()) {
            EdgeDistance u = minDist.removeMin();
            if (unprocessed.contains(u.vertex)) {
                if (u.vertex.equals(end)) {
                    V curr = u.vertex;
                    // inserts predecessors into output list
                    while (curr != start) {
                        output.insert(0, predecessor.get(curr));
                        curr = predecessor.get(curr).getOtherVertex(curr);
                    }
                    return output;
                }
                for (E edge : graph.get(u.getVertex())) {
                    EdgeDistance v = distances.get(edge.getOtherVertex(u.getVertex()));
                    if (unprocessed.contains(v.getVertex())) {
                        if (u.getWeight() + edge.getWeight() < v.getWeight()) {
                            v.setWeight(u.getWeight() + edge.getWeight());
                            minDist.insert(v);
                            predecessor.put(v.getVertex(), edge);
                        }
                    }
                }
                unprocessed.remove(u.vertex);
            }
        }        
        throw new NoPathExistsException();
    }

    private class EdgeDistance implements Comparable<EdgeDistance> {
        private V vertex;
        private double weight; // total distance

        public EdgeDistance(V vertex, double weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        public V getVertex() {
            return this.vertex;
        }

        public void setVertex(V vertex) {
            this.vertex = vertex;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        /**
         * Returns the numerical weight of this edge.
         */
        public double getWeight() {
            return this.weight;
        }

        /**
         * Given a vertex that is a part of this edge, returns the other vertex.
         *
         * Note: this method exists mainly because when doing graph traversals, it's
         * relatively common to have a vertex and an edge and want to find the other
         * vertex that's in the edge so we can continue traversing.
         *
         * This method lets us do that relatively cleanly.
         *
         * @throws IllegalArgumentException  if the vertex is null
         * @throws IllegalArgumentException  if the given vertex is not a part of this edge
         */

        public int compareTo(EdgeDistance other) {
            return Double.compare(this.weight, other.weight);
        }
    }
}
