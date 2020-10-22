import controller.Coder;
import view.ConsoleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.max;

public class Main {

    public static void main(String[] args) {
        final List<Integer> polynomial1 = Arrays.asList(0);
        final List<Integer> polynomial2 = Arrays.asList(0, 2);

        final int memoryLength = max(Arrays.asList(max(polynomial1), max(polynomial2)));

//        smallTest(Arrays.asList(0), Arrays.asList(0, 2), memoryLength);
        bigTest(Arrays.asList(0), Arrays.asList(0, 2), memoryLength, 1000000, 100, false);
    }

    public static void smallTest(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            final int memoryLength
    ) {
        final ConsoleView consoleView = new ConsoleView();
        final Coder coder = new Coder(polynomial1, polynomial2, memoryLength);

        if (polynomial1.size() > 1) {
            System.out.println("Error: polynomial 1 max degree is not 0");
            return;
        }

        consoleView.print(coder.getStatesMap(), memoryLength);

        final List<List<Integer>> result = coder.encode(Arrays.asList(0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1), memoryLength);
        System.out.println(result);
    }

    public static void bigTest(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            final int memoryLength,
            final int iterations,
            final int codeLength,
            final boolean visualize
    ) {
        final ConsoleView consoleView = new ConsoleView();
        final Coder coder = new Coder(polynomial1, polynomial2, memoryLength);
        List<Integer> list;

        if (polynomial1.size() > 1) {
            System.out.println("Error: polynomial 1 max degree is not 0");
            return;
        }

        consoleView.print(coder.getStatesMap(), memoryLength);

        final double start = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            list = new ArrayList<>();

            for (int j = 0; j < codeLength; j++) {
                list.add((int) Math.round(Math.random()));
            }

            if (visualize) {
                System.out.print("Seq: ");
                System.out.println(list);
                System.out.print("Coded: ");
            }

            List<List<Integer>> res = coder.encode(list, memoryLength);

            if (visualize) {
                System.out.println(res);
                System.out.println();
                System.out.println();
            }
        }

        final double stop = System.currentTimeMillis();

        System.out.println("Time: " + (stop - start) + " ms");
    }

}
