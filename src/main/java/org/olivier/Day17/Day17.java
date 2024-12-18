package org.olivier.Day17;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day17 {
    static int A;
    static int B;
    static int C;

    public static void main(String[] args) throws IOException {
        List<String> input = List.of(Utils.getFileContent("input_d17.txt").replace("\r", "").split("\n"));
        List<Integer> out = new ArrayList<>();
        final int registreA;
        final int registreB;
        final int registreC;
        final List<Integer> instructions;

        // Extraire les valeurs
        registreA = Integer.parseInt(input.get(0).split("Register A: ")[1]);
        registreB = Integer.parseInt(input.get(1).split("Register B: ")[1]);
        registreC = Integer.parseInt(input.get(2).split("Register C: ")[1]);
        instructions = Stream.of(input.get(4).split("Program: ")[1].split(",")).map(Integer::parseInt).toList();

        A = registreA;
        B = registreB;
        C = registreC;

        compute(instructions, out);
        System.out.println(String.join(",", out.stream().map(Long::toString).toList()));
    }

    static long getCombo(int value) {
        return switch (value % 8) {
            case 0, 1, 2, 3 -> value;
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            case 7 -> Long.MIN_VALUE;
            default -> throw new IllegalStateException("cas impossible");
        };
    }

    private static void compute(List<Integer> opCodes, List<Integer> out) {
        int i = 0;
        while (i < opCodes.size()) {
            int litOp = opCodes.get(i + 1);
            long comboOpCode = getCombo(opCodes.get(i + 1));
            boolean skip = false;
            switch (opCodes.get(i)) {
                case 0 -> {
                    long den = (long) Math.pow(2, comboOpCode);
                    A = (int) (A / den);
                }
                case 1 -> B = (int) (B ^ ((long) litOp));
                case 2 -> B = (int) (comboOpCode % 8);
                case 3 -> {
                    if (A != 0) {
                        i = litOp;
                        skip = true;
                    }
                }
                case 4 -> B = B ^ C;
                case 5 -> out.add((int) (comboOpCode % 8L));
                case 6 -> {
                    long den = (long) Math.pow(2, comboOpCode);
                    B = (int) (A / den);
                }
                case 7 -> {
                    long den = (long) Math.pow(2, comboOpCode);
                    C = (int) (A / den);
                }
            }
            if (!skip) {
                i += 2;
            }
        }
    }
}
