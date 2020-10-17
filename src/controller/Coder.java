package controller;

import java.util.*;

public class Coder {

    private final Map<String, Map<Integer, String>> statesMap;

    public Coder(final List<Integer> polynomial1, final List<Integer> polynomial2, int memoryLength) {
        statesMap = new HashMap<>();

        for (int i = 0; i < Math.pow(2, memoryLength); i++) {
            final Map<Integer, String> values = new HashMap<>();

            values.put(0, countPolynomial(polynomial1, polynomial2, memoryLength, i, 0));
            values.put(1, countPolynomial(polynomial1, polynomial2, memoryLength, i, 1));
            statesMap.put(String.format("%" + memoryLength + "s", Integer.toBinaryString(i)).replaceAll(" ", "0"), values);
        }
    }

    public Map<String, Map<Integer, String>> getStatesMap() {
        return statesMap;
    }

    public String encode(final List<Integer> codeSequence, final int memoryLength) {
        final StringBuilder result = new StringBuilder();
        final String [] memory = new String [memoryLength];

        for (int i = 0; i < memoryLength; i++) {
            memory[i] = "0";
        }

        for (final int bit : codeSequence) {
            result.append(encodeBit(bit, memory, memoryLength));
        }

        result.append(encodeBit(0, memory, memoryLength));

        return result.toString();
    }

    public void decode() {

    }

    private String encodeBit(final int bit, final String [] memory, final int memoryLength) {
        final StringBuilder result = new StringBuilder();
        final String word = statesMap.get(String.join("", memory)).get(bit);

        result.append(word);
        result.append(" ");

        if (memoryLength > 1) {
            System.arraycopy(memory, 0, memory, 1, memoryLength - 1);
        }

        memory[0] = String.valueOf(bit);

        return result.toString();
    }

    private String countPolynomial(
            final List<Integer> polynomial1,
            final List<Integer> polynomial2,
            int memoryLength,
            int memory,
            int incomingBit
    ) {
        final StringBuilder result = new StringBuilder();
        final int firstPolynomialDegree = polynomial1.get(0);
        int sum = 0;

        result.append(firstPolynomialDegree == 0 ? incomingBit : getBit(memory, memoryLength - firstPolynomialDegree));

        for (final int degree : polynomial2) {
            if (degree == 0) {
                sum ^= incomingBit;
            } else {
                sum ^= getBit(memory, memoryLength - degree);
            }
        }

        result.append(sum);

        return result.toString();
    }

    private int getBit(int number, int position) {
        return (number >> position) & 1;
    }

}
