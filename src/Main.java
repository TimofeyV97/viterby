import controller.Coder;
import view.ConsoleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.max;

public class Main {

    public static void main(String[] args) {
        final ConsoleView consoleView = new ConsoleView();
        final List<Integer> polynomial1 = Arrays.asList(0);
        final List<Integer> polynomial2 = Arrays.asList(0, 2);

        if (polynomial1.size() > 1) {
            System.out.println("Error: polynomial 1 max degree is not 0");
            return;
        }

        final int memoryLength = max(Arrays.asList(max(polynomial1), max(polynomial2)));
        final Coder coder = new Coder(polynomial1, polynomial2, memoryLength);

        consoleView.print(coder.getStatesMap(), memoryLength);

//        String res = coder.encode(Arrays.asList(1, 0, 1, 0, 1), memoryLength);
////		String res = coder.encode(Arrays.asList(0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1), memoryLength);
//        System.out.println(res);

        runTest(1000000, 100);
    }

    public static void runTest(final int iterations, final int codeLength) {
        final List<Integer> polynomial1 = Arrays.asList(0);
        final List<Integer> polynomial2 = Arrays.asList(0, 1);
        final int memoryLength = max(Arrays.asList(max(polynomial1), max(polynomial2)));
        final Coder coder = new Coder(polynomial1, polynomial2, memoryLength);
        List<Integer> list;

        final double start = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            list = new ArrayList<>();

            for (int j = 0; j < codeLength; j++) {
                list.add((int) Math.round(Math.random()));
            }

//            System.out.print("Seq: ");
//            System.out.println(list);
//            System.out.print("Coded: ");
            String res = coder.encode(list, memoryLength);
//            System.out.println(res);
//            System.out.println();
//            System.out.println();
        }

        final double stop = System.currentTimeMillis();

        System.out.println("Time: " + (stop - start) + " ms");
    }

}
