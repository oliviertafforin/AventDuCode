package org.olivier.Day11;

import javafx.util.Pair;
import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day11 {
    public static long divisions = 0;
    static Map<Pair<Long, Integer>, Long> cache = new HashMap<>();


    public static void main(String[] args) throws IOException {
        var input = Utils.getFileContent("input_d11.txt");
        var stones = Arrays.stream(input.split(" ")).mapToLong(Long::parseLong).toArray();
        long sum = 0;
        for (var stone : stones) {
            sum += blink(stone, 25);
        }
        System.out.println(sum);
        sum = 0;
        for (var stone : stones) {
            sum += blink(stone, 75);
        }
        System.out.println(sum);
    }

    private static long blink(long stone, int nbIterations) {
        var cacheId = new Pair<>(stone, nbIterations);
        if (cache.containsKey(cacheId)) return cache.get(cacheId);

        if (nbIterations == 0) {
            cache.put(cacheId, 1L);
            return 1;
        }
        var nbChiffres = (long) Math.floor(Math.log10(stone) + 1);
        if (stone == 0) {
            var res = blink(1, nbIterations - 1);
            cache.put(cacheId, res);
            return res;
        } else if (nbChiffres > 0 && nbChiffres % 2 == 0) {
            long[] stones = splitNumber(stone);
            divisions++;
            var res = blink(stones[0], nbIterations - 1) + blink(stones[1], nbIterations - 1);
            cache.put(cacheId, res);
            return res;
        }
        var res = blink(stone * 2024, nbIterations - 1);
        cache.put(cacheId, res);
        return res;
    }

    public static long[] splitNumber(long num) {
        int length = (int) Math.log10(num) + 1;

        int moitie = length / 2;
        long diviseur = (long) Math.pow(10, moitie);

        long firstH = num / diviseur;
        long secondH = num % diviseur;

        return new long[]{firstH, secondH};
    }
}
