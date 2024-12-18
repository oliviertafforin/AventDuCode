package org.olivier.Day2;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        String input = Utils.getFileContent("input_d2.txt");
        List<String> reports = List.of(input.split("\n"));
        List<List<Integer>> unsafeReports = new ArrayList<>();
        List<String> safeReports = new ArrayList<>(reports.stream().filter(r -> {

            List<Integer> report = Arrays.stream(r.split(" ")).map(Integer::parseInt).toList();
            HashSet<Integer> set = new HashSet<>();
            boolean hasDuplicates = report.stream().anyMatch(num -> !set.add(num));
            if (hasDuplicates) {
                unsafeReports.add(report);
                return false;
            }

            boolean superieur = report.get(0) > report.get(1);
            int unsafetyCounter = 0;
            for (int i = 0; i + 1 < report.size(); i++) {
                if (superieur) {
                    if (report.get(i) < report.get(i + 1)) {
                        unsafetyCounter++;

                        continue;
                    }
                } else {
                    if (report.get(i) > report.get(i + 1)) {
                        unsafetyCounter++;
                        continue;
                    }
                }
                if (Math.abs(report.get(i) - report.get(i + 1)) > 3) {
                    unsafetyCounter++;
                    continue;
                }
                if (Math.abs(report.get(i + 1) - report.get(i)) > 3) {
                    unsafetyCounter++;
                    continue;
                }
            }
            if (unsafetyCounter > 0) {
                unsafeReports.add(report);
            }
            return unsafetyCounter <= 0;
        }).toList());
        var count = 0;
        for (List<Integer> unsafeReport : unsafeReports) {
            List<List<Integer>> clonedReports = new ArrayList<>();
            for (int i = 0; i < unsafeReport.size(); i++) {
                List<Integer> clonedReport = new ArrayList<>(unsafeReport);
                clonedReport.remove(i);
                clonedReports.add(clonedReport);
            }

            boolean tolerated = clonedReports.stream().anyMatch(Day2::checkReport);

            if (tolerated) {
//                System.out.println(unsafeReport+" toléré");
//                safeReports.add("");
                count++;
            } else {
                System.out.println(unsafeReport + " non toléré");
            }
        }

        System.out.println(safeReports.size());
        System.out.println(count + safeReports.size());
    }


    public static boolean checkReport(List<Integer> report) {

        int supCount = 0;
        int infCount = 0;
        int unsafetyCounter = 0;
        for (int i = 0; i + 1 < report.size(); i++) {

            if (report.get(i) < report.get(i + 1)) {
                infCount++;
                if (supCount > 0) {
                    unsafetyCounter++;
                    continue;
                }
            } else if (report.get(i) > report.get(i + 1)) {
                supCount++;
                if (infCount > 0) {
                    unsafetyCounter++;
                    continue;
                }
            } else {
                unsafetyCounter++;
                continue;
            }

            if (Math.abs(report.get(i) - report.get(i + 1)) > 3) {
                unsafetyCounter++;
                continue;
            }
            if (Math.abs(report.get(i + 1) - report.get(i)) > 3) {
                unsafetyCounter++;
                continue;
            }
        }
        return unsafetyCounter <= 0;
    }
}
