import controller.Coder;
import controller.Plot;
import org.jfree.data.xy.XYSeries;
import view.ConsoleView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.max;

public class Main {

    public static void main(String[] args) {
        final List<Integer> polynomial1 = Arrays.asList(0, 2);
        final List<Integer> polynomial2 = Arrays.asList(0, 1, 2);
        final int SNRDb = 4;

        final int memoryLength = max(Arrays.asList(max(polynomial1), max(polynomial2)));

//        smallTest(polynomial1, polynomial2, SNRDb, memoryLength);
//		System.out.println(bigTest(polynomial1, polynomial2, SNRDb, memoryLength, 10000, 100, false));
        graphicTest(polynomial1, polynomial2, memoryLength, 1000, 50, false);
    }

    public static void smallTest(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            final int SNR,
            final int memoryLength
    ) {
        checkPolynomials(polynomial1, polynomial2);

        final ConsoleView consoleView = new ConsoleView();
        final Coder coder = new Coder(polynomial1, polynomial2, memoryLength);

        final List<Integer> sequence = Arrays.asList(0, 0, 0, 1, 1, 0, 0, 0);

        consoleView.print(coder.getStatesMap(), memoryLength);

        System.out.print("Sequence: 			  ");
        System.out.println(sequence);

        List<List<Integer>> encoded = coder.encode(sequence, memoryLength);
        System.out.print("Encoded:  			  ");
        System.out.println(encoded);

        encoded = coder.noise(SNR, encoded);
        System.out.print("Encoded with errors:  ");
        System.out.println(encoded);

        final List<Integer> decoded = coder.decode(encoded, memoryLength);
        System.out.print("Decoded: 			  ");
        System.out.println(decoded);
        System.out.println();
    }

    public static double bigTest(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            final double SNRDb,
            final int memoryLength,
            final int iterations,
            final int codeLength,
            final boolean visualize
    ) {
        checkPolynomials(polynomial1, polynomial2);

        final Coder coder = new Coder(polynomial1, polynomial2, memoryLength);
        double errorProbabilitiesSum = 0;

        final double start = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            if (i % 10000 == 0) {
                System.out.printf("Current iteration: %d\n\n", i);
            }

            List<Integer> sequence = new ArrayList<>();

            for (int j = 0; j < codeLength; j++) {
                sequence.add((int) Math.round(Math.random()));
            }

            if (visualize) {
                System.out.print("Sequence: 			  ");
                System.out.println(sequence);
                System.out.print("Encoded:  			  ");
            }

            List<List<Integer>> encoded = coder.encode(sequence, memoryLength);

            if (visualize) {
                System.out.println(encoded);
            }

            encoded = coder.noise(SNRDb, encoded);

            if (visualize) {
                System.out.print("Encoded with errors:  ");
                System.out.println(encoded);
            }

            List<Integer> decoded = coder.decode(encoded, memoryLength);

            if (visualize) {
                System.out.print("Decoded: 			  ");
                System.out.println(decoded);
                System.out.println();
            }

            errorProbabilitiesSum += ((float) countDifference(sequence, decoded) / codeLength);
        }

        System.out.println("Time: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println();

        return errorProbabilitiesSum / iterations;
    }

    public static void graphicTest(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            final int memoryLength,
            final int iterations,
            final int codeLength,
            final boolean visualize
    ) {
        final XYSeries series = new XYSeries("dependency");
        double errorProbability;

        for (int i = 0; i <= 6; i++) {
            double SNRDb = Math.pow(2, i);
            errorProbability = bigTest(polynomial1, polynomial2, SNRDb, memoryLength, iterations, codeLength, visualize);
            series.add(errorProbability, SNRDb);
        }

        final Plot plot = new Plot("SNR/Error dependency", series);

        plot.setSize(900, 400);
        plot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        plot.setVisible(true);
    }

    private static void checkPolynomials(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2
    ) {
        if (polynomial1.size() <= 1 || polynomial2.size() <= 1) {
            throw new RuntimeException("Incorrect polynomials length");
        }

//		if (max(polynomial1) > 2 || max(polynomial2) > 2) {
//			throw new RuntimeException("One of polynomials max degree is more than 2");
//		}
    }

    private static int countDifference(final List<Integer> inputSeq, final List<Integer> outputSeq) {
        int difference = 0;

        for (int i = 0; i < inputSeq.size() - 1; i++) {
            if (!inputSeq.get(i).equals(outputSeq.get(i))) {
                difference++;
            }
        }

        return difference;
    }

}
