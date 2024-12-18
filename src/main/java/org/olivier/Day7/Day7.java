package org.olivier.Day7;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {

    public static void main(String[] args) throws IOException {
        List<String> input = List.of(Utils.getFileContent("input_d7.txt").split("\n"));
        solve(input, true);
        solve(input, false);
    }

    public static void solve(List<String> lines, boolean part1) {
        Operator[] operators = new Operator[]{Operator.COMBINE, Operator.MULTIPLY, Operator.PLUS};
        long result = 0L;

        for (String line : lines) {
            String[] split = line.split(":");
            long resultValue = Long.parseLong(split[0].trim());
            List<Long> values = Arrays.stream(split[1].trim().split(" ")).map(Long::parseLong).toList();

            List<List<Operator>> operatorCombinations = generateOperators(operators, values.size() - 1);

            for (List<Operator> combination : operatorCombinations) {
                Long solution = values.get(0);

                for (int j = 0; j < combination.size(); j++) {
                    Operator op = combination.get(j);
                    Long number = values.get(j + 1);

                    if (op == Operator.PLUS) {
                        solution += number;
                    } else if (op == Operator.MULTIPLY) {
                        solution *= number;
                    }
                    //on ajoute la concatenation sur la part2
                    else if (!part1) {
                        solution = Long.parseLong(solution.toString() + number.toString());
                    }

                    if (solution > resultValue) {
                        break;
                    }
                }

                if (solution.equals(resultValue)) {
                    result += resultValue;
                    break;
                }
            }
        }

        System.out.println(result);
    }

    private static List<List<Operator>> generateOperators(Operator[] operators, int length) {
        List<List<Operator>> result = new ArrayList<>();

        result.add(new ArrayList<>());

        for (int i = 0; i < length; i++) {
            List<List<Operator>> newCombinations = new ArrayList<>();
            for (List<Operator> combination : result) {
                for (Operator operator : operators) {
                    List<Operator> newCombination = new ArrayList<>(combination);
                    newCombination.add(operator);
                    newCombinations.add(newCombination);
                }
            }
            result = newCombinations;
        }

        return result;
    }


    enum Operator {
        PLUS,
        MULTIPLY,
        COMBINE
    }
}
