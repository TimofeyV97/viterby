package controller;

import java.util.*;

public class Coder {

    private static final int BITS_OUT = 2;

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
            result.add(statesMap.get(memory).get(bit));
            memory = shiftMemory(bit, memory, memoryLength);
        }

        // clear memory
        for (int i = 0; i < memoryLength; i++) {
            result.add(statesMap.get(memory).get(0));
            memory = shiftMemory(0, memory, memoryLength);
        }

        return result;
    }

    public void decode(final List<Integer> codeSequence) {

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

    private int calcHammingDistance(final int firstValue, final int secondValue) {
        int distance = 0;

        // traverse value as binary
        for (int i = (BITS_OUT - 1); i >= 0; i--) {
            if (getBit(firstValue, BITS_OUT) != getBit(secondValue, BITS_OUT)) {
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
