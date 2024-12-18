package org.olivier.Day6;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day6 {
    //si pas part1 c'est part2
    private final static boolean PART_1 = false;

    public static void main(String[] args) throws IOException {
        String[] input = Utils.getFileContent("input_d6.txt").split("\n");

        String[][] map = new String[input.length][input[0].length()];

        String[][] mapWithPathing = new String[input.length][input[0].length()];
        int guardX = 0;
        int guardY = 0;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                map[i][j] = String.valueOf(input[i].charAt(j));
                mapWithPathing[i][j] = String.valueOf(input[i].charAt(j));
                if (Objects.equals(map[i][j], "^")) {
                    guardY = j;
                    guardX = i;
                }
            }

        }
        String[][] copy = copyArray(map);
        List<String> path = resolveGuardPathing(map, mapWithPathing, guardX, guardY, true);

        if (!PART_1) part2(copy, path, guardX, guardY);
    }

    public static String[][] copyArray(String[][] toBeCopied) {
        String[][] newArray = new String[toBeCopied.length][toBeCopied[0].length];
        for (int i = 0; i < newArray.length; i++) {
            for (int j = 0; j < newArray[0].length; j++) {
                newArray[i][j] = toBeCopied[i][j];
            }

        }
        return newArray;
    }

    private static List<String> resolveGuardPathing(String[][] map, String[][] mapWithPathing, int x, int y, boolean part1) {
        List<String> coordMemory = new ArrayList<>();
        while (true) {
            String direction = map[x][y];
            int lookAtX = x;
            int lookAtY = y;
            switch (direction) {
                case ">" -> lookAtY++;
                case "<" -> lookAtY--;
                case "v" -> lookAtX++;
                case "^" -> lookAtX--;
            }
            String coord = x + "," + y + "," + direction;
            if (coordMemory.contains(coord)) {
                throw new TournenrondException();
            } else {
                coordMemory.add(coord);
            }
            //extérieur
            if (lookAtX < 0 || lookAtX >= map.length || lookAtY < 0 || lookAtY >= map[0].length) {
                mapWithPathing[x][y] = "X";
                break;
                //mur
            } else if (map[lookAtX][lookAtY].equals("#")) {
                map[x][y] = rotate(map, x, y);
            } else {
                map[x][y] = ".";
                mapWithPathing[x][y] = "X";
                x = lookAtX;
                y = lookAtY;
                map[x][y] = direction;
            }
        }
        List<String> path = new ArrayList<>();
        if (part1) {
            int count = 0;
            System.out.println("sortie !");

            for (int i = 0; i < mapWithPathing.length; i++) {
                for (int j = 0; j < mapWithPathing[0].length; j++) {
//                    System.out.print(mapWithPathing[i][j]);
                    if (mapWithPathing[i][j].equals("X")) {
                        count++;
                        path.add(i + "," + j);
                    }
                }
//                System.out.println();
            }
            System.out.println(count);
        }
        return path;

    }

    private static class TournenrondException extends RuntimeException {
        public TournenrondException() {
            super("On tourne en rond capitaine");
        }
    }

    private static void part2(String[][] map, List<String> path, int x, int y) {
        for (String pathTile : path) {
            String[][] array = copyArray(map);
            int tileX = Integer.parseInt(pathTile.split(",")[0]);
            int tileY = Integer.parseInt(pathTile.split(",")[1]);
            if (!(tileX == x && tileY == y)) {


                array[tileX][tileY] = "#";
                String[][] array2 = copyArray(array);
                try {
                    resolveGuardPathing(array, array2, x, y, false);
                } catch (TournenrondException ie) {
                    System.out.println("on continue");
                }
            }
        }
        System.out.println("terminé");
    }

    public static String rotate(String[][] map, int x, int y) {
        return switch (map[x][y]) {
            case ">" -> "v";
            case "<" -> "^";
            case "v" -> "<";
            case "^" -> ">";
            default -> throw new IllegalStateException("valeur impossible " + map[x][y]);
        };
    }
}
