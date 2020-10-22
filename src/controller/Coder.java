package controller;

import java.util.*;

public class Coder {

    private final Map<Integer, Map<Integer, String>> statesMap;

    public Coder(final List<Integer> polynomial1, final List<Integer> polynomial2, int memoryLength) {
        statesMap = new HashMap<>();

        for (int i = 0; i < Math.pow(2, memoryLength); i++) {
            final Map<Integer, String> values = new HashMap<>();

            values.put(0, countPolynomial(polynomial1, polynomial2, memoryLength, i, 0));
            values.put(1, countPolynomial(polynomial1, polynomial2, memoryLength, i, 1));
//            statesMap.put(String.format("%" + memoryLength + "s", Integer.toBinaryString(i)).replaceAll(" ", "0"), values);
            statesMap.put(i, values);
        }
    }

    public Map<Integer, Map<Integer, String>> getStatesMap() {
        return statesMap;
    }

    public String encode(final List<Integer> codeSequence, final int memoryLength) {
        final StringBuilder result = new StringBuilder();
        // init memory with 0
        int memory = 0;

        // encode
        for (final int bit : codeSequence) {
            result.append(encodeBit(bit, memory));
            memory = shiftMemory(bit, memory, memoryLength);
        }

        // clear memory
        for (int i = 0; i < memoryLength; i++) {
            result.append(encodeBit(0, memory));
            memory = shiftMemory(0, memory, memoryLength);
        }

        return result.toString();
    }

    public void decode() {

    }

    private String encodeBit(final int bit, int memory) {
        final StringBuilder result = new StringBuilder();
        final String word = statesMap.get(memory).get(bit);

        result.append(word);
        result.append(" ");

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
            sum ^= degree == 0 ? incomingBit : getBit(memory, memoryLength - degree);
        }

        result.append(sum);

        return result.toString();
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
