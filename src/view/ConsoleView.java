package view;

import java.util.List;
import java.util.Map;

public class ConsoleView {

    public void print(final Map<Integer, Map<Integer, List<Integer>>> map, final int memoryLength) {
        map.forEach((key, value) -> value.forEach((key1, value1) -> {
            System.out.print(
                    String.format("%" + memoryLength + "s", Integer.toBinaryString(key)).replaceAll(" ", "0")
            );

            System.out.print("->");
            System.out.printf("%d/%s\n", key1, value1);
        }));
    }

    public void print(final Map<Integer, Map<Integer, List<Integer>>> map) {
        System.out.println(map);
    }

    public void printAdjMatrix(final int [][] matrix, final int vertexNumber) {
        for (int i = 0; i < vertexNumber; i++) {
            for (int j = 0; j < vertexNumber; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }

            System.out.println();
        }
    }

}
