package view;

import java.util.Map;

public class ConsoleView {

//    public void print(final Map<Integer, Map<Integer, String>> map, final int bitLength) {
//        map.forEach((key, value) -> value.forEach((key1, value1) -> {
//            System.out.print(
//                    String.format("%" + bitLength + "s", Integer.toBinaryString(key)).replaceAll(" ", "0")
//            );
//
//            System.out.print("->");
//            System.out.printf("%d/%s\n", key1, value1);
//        }));
//    }

    public void print(final Map<String, Map<Integer, String>> map) {
        System.out.println(map);
    }

}
