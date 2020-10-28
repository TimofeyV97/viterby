package controller;

import model.Graph;
import view.ConsoleView;

import java.util.*;

import static java.lang.Math.pow;
import static model.Constants.*;

public class Coder {

    private final Map<Integer, Map<Integer, List<Integer>>> statesMap;

    private final ConsoleView consoleView;

    public Coder(final List<Integer> polynomial1, final List<Integer> polynomial2, int memoryLength) {
        statesMap = new HashMap<>();
        consoleView = new ConsoleView();

        for (int i = 0; i < pow(2, memoryLength); i++) {
            final Map<Integer, List<Integer>> values = new HashMap<>();
            values.put(0, countPolynomial(polynomial1, polynomial2, memoryLength, i, 0));
            values.put(1, countPolynomial(polynomial1, polynomial2, memoryLength, i, 1));
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

    public List<Integer> decode(final List<List<Integer>> codeSequence, final int memoryLength) {
        final List<Integer> decoded = new ArrayList<>();
        final Graph graph;
        final int vertexNum;
        int index = 0;

        if (memoryLength == 1) {
            vertexNum = (codeSequence.size() * 2) + 2;
            graph = new Graph(vertexNum);
            List<Integer> word = codeSequence.get(0);

            for (int i = 1; i <= 2; i++) {
                graph.addVertex("t" + i + "_0", 0);
                graph.addVertex("t" + i + "_1", 1);
            }

            graph.addEdge(index, 2, calcHammingDistance(statesMap.get(0).get(0), word));
            graph.addEdge(index, 3,  calcHammingDistance(statesMap.get(0).get(1), word));
            index = 2;

            for (int i = 1; i < codeSequence.size(); i++) {
                word = codeSequence.get(i);

                graph.addVertex("t" + (i + 2) + "_0", 0);
                graph.addVertex("t" + (i + 2) + "_1", 1);

                for (int j = 0; j <= 1; j++) {
                    graph.addEdge(index, (i + 1) * 2, calcHammingDistance(statesMap.get(j).get(0), word));
                    graph.addEdge(index, (i + 1) * 2 + 1,  calcHammingDistance(statesMap.get(j).get(1), word));
                    index++;
                }
            }
        } else {
            vertexNum = (codeSequence.size() * 4) + 4;
            graph = new Graph(vertexNum);
            List<Integer> word = codeSequence.get(0);

            for (int i = 1; i <= 3; i++) {
                graph.addVertex("t" + i + "_0", 0);
                graph.addVertex("t" + i + "_1", 0);
                graph.addVertex("t" + i + "_2", 1);
                graph.addVertex("t" + i + "_3", 1);
            }

            graph.addEdge(index, 4, calcHammingDistance(statesMap.get(0).get(0), word));
            graph.addEdge(index, 6,  calcHammingDistance(statesMap.get(0).get(1), word));

            word = codeSequence.get(1);
            index = 4;
            graph.addEdge(index, 8, calcHammingDistance(statesMap.get(0).get(0), word));
            graph.addEdge(index, 10,  calcHammingDistance(statesMap.get(0).get(1), word));
            index = 6;
            graph.addEdge(index, 9, calcHammingDistance(statesMap.get(2).get(0), word));
            graph.addEdge(index, 11,  calcHammingDistance(statesMap.get(2).get(1), word));
            index = 8;

            for (int i = 2; i < codeSequence.size(); i++) {
                word = codeSequence.get(i);
                graph.addVertex("t" + (i + 2) + "_0", 0);
                graph.addVertex("t" + (i + 2) + "_1", 0);
                graph.addVertex("t" + (i + 2) + "_2", 1);
                graph.addVertex("t" + (i + 2) + "_3", 1);

                for (int j = 0; j <= 1; j++) {
                    graph.addEdge(index, (i + 1) * 4, calcHammingDistance(statesMap.get(j).get(0), word));
                    graph.addEdge(index, (i + 1) * 4 + 2,  calcHammingDistance(statesMap.get(j).get(1), word));
                    index++;
                }

                for (int j = 2; j <= 3; j++) {
                    graph.addEdge(index, ((i + 1) * 4) + 1, calcHammingDistance(statesMap.get(j).get(0), word));
                    graph.addEdge(index, ((i + 1) * 4 + 2) + 1,  calcHammingDistance(statesMap.get(j).get(1), word));
                    index++;
                }
            }
        }

        final List<Integer> path = findShortestPath(vertexNum, graph.getAdjMatrix(), memoryLength);
        final Graph.Vertex[] vertexList = graph.getVertexList();

        for (int i = path.size() - 2; i >= 0; i--) {
            final Graph.Vertex vertex = vertexList[path.get(i)];
            decoded.add(vertex.getDirection());
        }

        return decoded;
    }

    public List<List<Integer>> noise(final int SNR, final List<List<Integer>>  encoded) {
        if (SNR <= 0) {
            return encoded;
        }

        final List<List<Integer>> noised = new ArrayList<>(encoded);
        final int ratio = 100 / SNR;

        noised.addAll(encoded);
        return noised;
    }

    private List<Integer> findShortestPath(int vertexNum, int [][] graph, final int memoryLength) {
        final List<Integer> stack = new ArrayList<>();
        boolean [] used = new boolean [vertexNum];
        int [] dist = new int [vertexNum];
        int [] prev = new int [vertexNum];

        Arrays.fill(prev, -1);
        Arrays.fill(dist, INF);

        dist[0] = 0;

        while (true) {
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

        for (int i = 2; i <= pow(2, memoryLength); i++) {
            if (dist[dist.length - i] < min) {
                min = dist[dist.length - i];
                index = dist.length - i;
            }
        }

        for (int v = index; v != -1; v = prev[v]) {
            stack.add(v);
        }

        return stack;
    }

    private List<Integer> countPolynomial(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            int memoryLength,
            int memory,
            int incomingBit
    ) {
        final List<Integer> output = new ArrayList<>();
        final int firstPolynomialDegree = polynomial1.get(0);
        final int secondPolynomialDegree = polynomial2.get(0);
        int sum = 0;

        if (polynomial1.size() == 1) {
            output.add(firstPolynomialDegree == 0 ? incomingBit : getBit(memory, memoryLength - firstPolynomialDegree));

            for (final int degree : polynomial2) {
                sum ^= degree == 0 ? incomingBit : getBit(memory, memoryLength - degree);
            }

            output.add(sum);
        } else if (polynomial2.size() == 1) {
            for (final int degree : polynomial1) {
                sum ^= degree == 0 ? incomingBit : getBit(memory, memoryLength - degree);
            }

            output.add(sum);

            output.add(secondPolynomialDegree == 0 ? incomingBit : getBit(memory, memoryLength - secondPolynomialDegree));
        }

        return output;
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
