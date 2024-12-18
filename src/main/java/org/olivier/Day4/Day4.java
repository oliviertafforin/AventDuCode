package org.olivier.Day4;

import org.olivier.Utils.Utils;

import java.io.IOException;

public class Day4 {
    public static void main(String[] args) throws IOException {
        String[] input = Utils.getFileContent("input_d4.txt").split("\n");

        System.out.println(countXmax(input));

        System.out.println(countX_Mas(input));
    }

    // Part 1
    public static int countXmax(String[] input) {
        int result = 0;
        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input[x].length(); y++) {
                result += searchXmax(input, 'X', x, y, 1, 0);
                result += searchXmax(input, 'X', x, y, 0, 1);
                result += searchXmax(input, 'X', x, y, -1, 0);
                result += searchXmax(input, 'X', x, y, 0, -1);

                result += searchXmax(input, 'X', x, y, 1, 1);
                result += searchXmax(input, 'X', x, y, 1, -1);
                result += searchXmax(input, 'X', x, y, -1, 1);
                result += searchXmax(input, 'X', x, y, -1, -1);
            }
        }
        return result;
    }

    public static int searchXmax(String[] input, char toMatch, int x, int y, int dx, int dy) {
        if (toMatch == 'S' && input[x].charAt(y) == toMatch) {
            return 1;
        }
        if (input[x].charAt(y) != toMatch) {
            return 0;
        }
        int nx = x + dx;
        int ny = y + dy;
        if (nx >= input.length || nx < 0) {
            return 0;
        }
        if (ny >= input[nx].length() || ny < 0) {
            return 0;
        }
        return searchXmax(input, switch (toMatch) {
            case 'S' -> 'X';
            case 'X' -> 'M';
            case 'M' -> 'A';
            case 'A' -> 'S';
            default -> throw new IllegalArgumentException("Illegal argument: " + toMatch);
        }, nx, ny, dx, dy);
    }

    // Part 2
    public static int countX_Mas(String[] input) {
        int result = 0;
        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input[x].length(); y++) {
                char current = input[x].charAt(y);
                if (current != 'A') {
                    continue;
                }
                int tx = x - 1;
                int bx = x + 1;
                int ly = y - 1;
                int ry = y + 1;
                if (x < 1 || x > input.length - 2) {
                    continue;
                }
                if (y < 1 || y > input[x].length() - 2) {
                    continue;
                }
                if ((input[tx].charAt(ly) == 'M' && input[bx].charAt(ry) == 'S'
                        || input[tx].charAt(ly) == 'S' && input[bx].charAt(ry) == 'M')
                        && (input[bx].charAt(ly) == 'M' && input[tx].charAt(ry) == 'S'
                        || input[bx].charAt(ly) == 'S' && input[tx].charAt(ry) == 'M')
                ) {
                    result++;
                }
            }
        }
        return result;
    }
}
