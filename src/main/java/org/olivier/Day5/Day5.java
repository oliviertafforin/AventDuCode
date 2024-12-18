package org.olivier.Day5;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        String[] input = Utils.getFileContent("input_d5.txt").split("\n\n");
        List<String> rules = List.of(input[0].split("\n"));
        List<String> updates = List.of(input[1].split("\n"));

        long total = 0;
        for (String update : updates) {
            if (checkRules(update, rules)) {
                total += getMiddleNumber(update);
            }
        }
        System.out.println(total);

        //PART2
        List<String> incorrectUpdates = updates.stream().filter(u -> !checkRules(u, rules)).toList();
        var total2 = 0;
        for (String update : incorrectUpdates) {
            List<String> specRules = rules.stream().filter(r -> incorrectUpdates.stream().allMatch(u -> {
                int first = update.indexOf(r.split("\\|")[0]);
                int second = update.indexOf(r.split("\\|")[1]);
                return first != -1 && second != -1;
            })).toList();

            int index = 2;
            List<String> upList = new ArrayList<>(List.of(update.split(",")));
            List<String> temp = new ArrayList<>();
            while (index <= upList.size()) {
                temp = upList.subList(0, index);
//                System.out.println(temp);

                var tmp = 0;
                var lastNum = temp.get(temp.size() - 1);
                while (!checkRules(String.join(",", temp), specRules)) {
                    temp.remove(lastNum);
                    temp.add(tmp, lastNum);
                    tmp++;
                }
                index++;
            }
            total2 += getMiddleNumber(String.join(",", temp));

        }
        System.out.println(total2);
    }

    private static long getMiddleNumber(String update) {
        String[] print = update.split(",");
        int middleIndex = (print.length - 1) / 2;
        return Integer.parseInt(print[middleIndex]);
    }

    private static boolean checkRules(String update, List<String> rules) {
        return rules.stream().allMatch(r -> {
            int first = update.indexOf(r.split("\\|")[0]);
            int second = update.indexOf(r.split("\\|")[1]);
            return (first == -1 || second == -1) || first < second;
        });
    }
}
