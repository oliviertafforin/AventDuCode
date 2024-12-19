package org.olivier.Day19;

import javafx.util.Pair;
import org.olivier.Utils.Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    static Map<Pair<String, List<String>>, Boolean> cache = new HashMap<>();
    static Map<Pair<String, List<String>>, BigInteger> cache2 = new HashMap<>();
    static BigInteger total_part2 = BigInteger.ZERO;

    public static void main(String[] args) throws IOException {
        String input = Utils.getFileContent("input_d19.txt").replace("\r", "");

        List<String> towelPatterns = List.of(input.split("\n\n")[0].replace(" ", "").split(","));

        List<String> designs = List.of(input.split("\n\n")[1].split("\n"));

        System.out.println(part1(towelPatterns, designs));
        cache.clear();
        part2(towelPatterns, designs);
        System.out.println("part2 : "+total_part2);
    }

    private static int part1(List<String> towelPatterns, List<String> designs) {
        int count = 0;
        for (String design : designs) {
            if (findPattern(design, towelPatterns)) {
                count++;
            }
        }
        return count;
    }

    private static void part2(List<String> towelPatterns, List<String> designs) {
        for (String design : designs) {
           total_part2 = total_part2.add(findPattern2(design, towelPatterns));
        }
    }

    private static boolean findPattern(String design, List<String> towelPatterns) {
        var cacheId = new Pair<>(design, towelPatterns);
        if (cache.containsKey(cacheId)) return cache.get(cacheId);

        if (design.length() == 0) {
            cache.put(cacheId, true);
            return true;
        }

        List<String> possiblePatterns = towelPatterns.stream().filter(design::startsWith).toList();
        for (String possiblePattern : possiblePatterns) {
            String newDesign = design.substring(possiblePattern.length());
            if (findPattern(newDesign, towelPatterns)) {
                cache.put(cacheId, true);
                return true;
            }
        }
        cache.put(cacheId, false);
        return false;
    }

    private static BigInteger findPattern2(String design, List<String> towelPatterns) {
        BigInteger num = BigInteger.ZERO;
        var cacheId = new Pair<>(design, towelPatterns);
        if (cache2.containsKey(cacheId)) return cache2.get(cacheId);

        if (design.length() == 0) {
            cache2.put(cacheId, BigInteger.ONE);
            return BigInteger.ONE;
        }

        List<String> possiblePatterns = towelPatterns.stream().filter(design::startsWith).toList();
        for (String possiblePattern : possiblePatterns) {
            String newDesign = design.substring(possiblePattern.length());
            BigInteger results = findPattern2(newDesign, towelPatterns);
                cache2.put(cacheId, results);
                num = num.add(results);
            }

        cache2.put(cacheId, num);
        return num;
    }


}
