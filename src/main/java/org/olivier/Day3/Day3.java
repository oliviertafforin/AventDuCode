package org.olivier.Day3;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.regex.Pattern;

public class Day3 {
    public static void main(String[] args) throws IOException {
        String input = Utils.getFileContent("input_d3.txt").replace("\n", "");
        System.out.println(input);
        System.out.println("1: " + multiply(input));

        input = input.replaceAll("don't\\(\\).*?(do\\(\\)|$)", "#####");
        System.out.println(input);
        System.out.println("2: " + multiply(input));
    }

    public static int multiply(String s) {
        return Pattern.compile("mul\\((\\d+),(\\d+)\\)").matcher(s)
                .results()
                .mapToInt(match -> Integer.parseInt(match.group(1)) * Integer.parseInt(match.group(2)))
                .sum();
    }

}
