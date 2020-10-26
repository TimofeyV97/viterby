package controller;

import model.Graph;
import java.util.*;

import static model.Constants.*;

public class Coder {

    private final Map<Integer, Map<Integer, List<Integer>>> statesMap;

    public Coder(final List<Integer> polynomial1, final List<Integer> polynomial2, int memoryLength) {
        statesMap = new HashMap<>();

        for (int i = 0; i < Math.pow(2, memoryLength); i++) {
            final Map<Integer, List<Integer>> values = new HashMap<>();

            values.put(0, countPolynomial(polynomial1, polynomial2, memoryLength, i, 0));
            values.put(1, countPolynomial(polynomial1, polynomial2, memoryLength, i, 1));
//            statesMap.put(String.format("%" + memoryLength + "s", Integer.toBinaryString(i)).replaceAll(" ", "0"), values);
            statesMap.put(i, values);
        }
    }

    public Map<Integer, Map<Integer, List<Integer>>> getStatesMap() {
        return statesMap;
    }

    public List<List<Integer>> encode(final List<Integer> codeSequence, final int memoryLength) {
        final List<List<Integer>> result = new ArrayList<>();
        // init memory with 0
        int memory = 0;

        // encode
        for (final int bit : codeSequence) {
            result.add(new ArrayList<>(statesMap.get(memory).get(bit)));
            memory = shiftMemory(bit, memory, memoryLength);
        }

        // clear memory
        for (int i = 0; i < memoryLength; i++) {
            result.add(new ArrayList<>(statesMap.get(memory).get(0)));
            memory = shiftMemory(0, memory, memoryLength);
        }

        return result;
    }

    public List<Integer> decode(final List<List<Integer>> codeSequence) {
        final int vertexNum = (codeSequence.size() * 2) + 2;
        final Graph graph = new Graph(vertexNum);
        int index = 0;

        if (codeSequence.size() != 1) {
            final List<Integer> word = codeSequence.get(0);
            graph.addVertex("t" + 1 + "_0", 0);
            graph.addVertex("t" + 1 + "_1", 1);
            graph.addVertex("t" + 2 + "_0", 0);
            graph.addVertex("t" + 2 + "_1", 1);

            int dist_0 = calcHammingDistance(statesMap.get(0).get(0), word);
            int dist_1 = calcHammingDistance(statesMap.get(0).get(1), word);

            graph.addEdge(index, 2, dist_0);
            graph.addEdge(index, 3,  dist_1);
        }

        index = 2;

        for (int i = 1; i < codeSequence.size(); i++) {
            final List<Integer> word = codeSequence.get(i);
            final int x_coord = (i + 1) * 2;
            final int x_coord_2 = (i + 1) * 2 + 1;

            graph.addVertex("t" + (i + 2) + "_0", 0);
            graph.addVertex("t" + (i + 2) + "_1", 1);
            int dist_0 = calcHammingDistance(statesMap.get(0).get(0), word);
            int dist_1 = calcHammingDistance(statesMap.get(0).get(1), word);

            graph.addEdge(index, x_coord, dist_0);
            graph.addEdge(index, x_coord_2,  dist_1);
            index++;

            int dist_00 = calcHammingDistance(statesMap.get(1).get(0), word);
            int dist_11 = calcHammingDistance(statesMap.get(1).get(1), word);
            graph.addEdge(index, x_coord, dist_00);
            graph.addEdge(index, x_coord_2, dist_11);
            index++;
        }

        final int [] path = findShortestPath(0, vertexNum - 1, vertexNum, graph.getAdjMatrix());
        final List<Integer> decoded = new ArrayList<>();

        for (int i = 1; i < path.length; i++) {
            Graph.Vertex vertex = graph.getVertexList()[path[i]];
            int direction = vertex.getDirection();
            decoded.add(direction);
        }

        return decoded;
    }

    private int [] findShortestPath(int start, int end, int vertexNum, int [][] graph) {
        final List<Integer> stack = new ArrayList<>();
        boolean [] used = new boolean [vertexNum];
        int [] dist = new int [vertexNum];
        int [] prev = new int [vertexNum];

        Arrays.fill(prev, -1);
        Arrays.fill(dist, INF);

        dist[start] = 0;

        for (;;) {
            int v = -1;

            for (int nv = 0; nv < vertexNum; nv++) {
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv])) {
                    v = nv;
                }
            }

            if (v == -1) {
                break;
            }

            used[v] = true;

            for (int i = 0; i < vertexNum; i++) {
                if (!used[i] && graph[v][i] < INF) {
                    if (dist[i] > dist[v] + graph[v][i]) {
                        dist[i] =  dist[v] + graph[v][i];
                        prev[i] = v;
                    }
                }
            }
        }

        int min = dist[dist.length - 1];
        int index = dist.length - 1;

        for (int i = 2; i <= 2; i++) {
            if (dist[dist.length - i] < min) {
                min = dist[dist.length - i];
                index = dist.length - i;
            }
        }

        end = index;

        for (int v = end; v != -1; v = prev[v]) {
            stack.add(v);
        }

        final int[] path = new int[stack.size()];

        for (int i = 0; i < path.length; i++) {
            path[i] = stack.get(stack.size() - (i + 1));
        }

        return path;
    }

    private List<Integer> countPolynomial(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            int memoryLength,
            int memory,
            int incomingBit
    ) {
        final List<Integer> result = new ArrayList<>();
        final int firstPolynomialDegree = polynomial1.get(0);
        int sum = 0;

        result.add(firstPolynomialDegree == 0 ? incomingBit : getBit(memory, memoryLength - firstPolynomialDegree));

        for (final int degree : polynomial2) {
            sum ^= degree == 0 ? incomingBit : getBit(memory, memoryLength - degree);
        }

        result.add(sum);

        return result;
    }

    private int calcHammingDistance(final List<Integer> firstValue, final List<Integer> secondValue) {
        int distance = 0;

        // traverse value as binary
        for (int i = (BITS_OUT - 1); i >= 0; i--) {
            if (!firstValue.get(i).equals(secondValue.get(i))) {
                distance++;
            }
        }

        return distance;
    }

    private int shiftMemory(final int bit, int memory, final int memoryLength) {
        memory >>= 1;
        return setBit(memory, bit, memoryLength - 1);
    }

    private int getBit(final int number, final int position) {
        return (number >> position) & 1;
    }

    private int setBit(int myByte, final int bit, final int position) {
        return bit == 1 ? (myByte | (1 << position)) : (myByte & ~(1 << position));
    }

}
