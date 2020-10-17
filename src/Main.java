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

        consoleView.print(coder.getStatesMap());

        for (int i = 0; i< 100000; i++) {
            final List<Integer> list = new ArrayList<>();

            for (int j = 0; j < 100; j++) {
                list.add((int) Math.round(Math.random()));
            }

            System.out.print("Seq: ");
            System.out.println(list);
            System.out.print("Coded: ");
            String res = coder.encode(list, memoryLength);
            System.out.println(res);
            System.out.println();
            System.out.println();
        }
    }

}
