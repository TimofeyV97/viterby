import controller.Coder;
import view.ConsoleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.max;

public class Main {

    public static void main(String[] args) {
        final List<Integer> polynomial1 = Arrays.asList(0);
        final List<Integer> polynomial2 = Arrays.asList(0, 1);

        final int memoryLength = max(Arrays.asList(max(polynomial1), max(polynomial2)));

//        smallTest(polynomial1, polynomial2, memoryLength);
        bigTest(polynomial1, polynomial2, memoryLength, 50, 10, true);
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

        final List<Integer> list = Arrays.asList(0, 1, 0, 1);
//        final List<Integer> list = Arrays.asList(0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1);

//        consoleView.print(coder.getStatesMap(), memoryLength);

        System.out.print("Sequence: ");
        System.out.println(list);

        final List<List<Integer>> encoded = coder.encode(list, memoryLength);
        System.out.print("Encoded:  ");
        System.out.println(encoded);

        final List<Integer> decoded = coder.decode(encoded);
        System.out.print("Decoded:  ");
        System.out.println(decoded);
        System.out.println();
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
            if (i % 10000 == 0) {
                System.out.println(i);
            }

            list = new ArrayList<>();

            for (int j = 0; j < codeLength; j++) {
                list.add((int) Math.round(Math.random()));
            }

            if (visualize) {
                System.out.print("Sequence: ");
                System.out.println(list);
                System.out.print("Encoded:  ");
            }

            final List<List<Integer>> encoded = coder.encode(list, memoryLength);
            final List<Integer> decoded = coder.decode(encoded);

            if (visualize) {
                System.out.println(encoded);
                System.out.print("Decoded:  ");
                System.out.println(decoded);
                System.out.println();
            }
        }

        final double stop = System.currentTimeMillis();

        System.out.println("Time: " + (stop - start) + " ms");
    }

}
