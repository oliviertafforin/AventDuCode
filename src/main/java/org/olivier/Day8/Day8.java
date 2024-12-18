package org.olivier.Day8;

import org.olivier.Utils.Utils;
import org.olivier.Utils.Vector2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public class Day8 {

    public static void main(String[] args) throws IOException {
        List<String> input = List.of(Utils.getFileContent("input_d8.txt").split("\n"));
        solve1(input);
        solve2(input);
    }

    public static void solve1(List<String> lines) {
        var grid = makeCharGrid(lines);

        final var antennas = findAntennas(grid);

        var antinodes = antennas.values().stream().flatMap((positions) -> {
            return positions.stream().flatMap(p1 -> {
                return positions.stream().filter(p2 -> !p2.equals(p1)).map(p2 -> {
                    var betweenPoints = new Vector2D(p2.x() - p1.x(), p2.y() - p1.y());
                    return p2.add(betweenPoints);
                });
            }).filter(possiblePosition -> !isOutOfBounds(grid, possiblePosition));
        }).collect(Collectors.toSet());

        System.out.println(antinodes);
        System.out.println(antinodes.size());


    }

    public static void solve2(List<String> lines) {
        var grid = makeCharGrid(lines);
        final var antennas = findAntennas(grid);

        var antinodes = antennas.values().stream().flatMap((positions) -> {
            return positions.stream().flatMap(p1 -> {
                return positions.stream().filter(p2 -> !p2.equals(p1)).flatMap(p2 -> {
                    List<Vector2D> points = new ArrayList<>();

                    int dx = p2.x() - p1.x();
                    int dy = p2.y() - p1.y();

                    var currentPoint = p2.copy();

                    while (true) {
                        var point = new Vector2D(currentPoint.x() + dx, currentPoint.y() + dy);

                        if (isOutOfBounds(grid, point)) {
                            break;
                        }

                        points.add(point);
                        currentPoint = point;
                    }

                    //Add antennas itself(?)
                    points.add(p1);
                    points.add(p2);

                    return points.stream();
                });
            }).filter(possiblePosition -> !isOutOfBounds(grid, possiblePosition));
        }).collect(Collectors.toSet());
        System.out.println(antinodes.size());

    }


    private static HashMap<Character, List<Vector2D>> findAntennas(char[][] grid) {
        final var antennas = new HashMap<Character, List<Vector2D>>();

        iterateGrid(grid, (position, token) -> {
            if (token == '.') {
                return;
            }

            antennas.putIfAbsent(token, new ArrayList<>());
            antennas.computeIfPresent(token, (key, positions) -> {
                positions.add(position);
                return positions;
            });
        });
        return antennas;
    }

    protected static char[][] makeCharGrid(List<String> lines) {
        return lines.stream().map(String::toCharArray).toArray(char[][]::new);
    }

    protected static void iterateGrid(char[][] grid, BiConsumer<Vector2D, Character> action) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                action.accept(new Vector2D(x, y), grid[y][x]);
            }
        }
    }

    protected static boolean isOutOfBounds(char[][] grid, Vector2D pos) {
        return pos.y() < 0 || pos.y() >= grid.length ||
                pos.x() < 0 || pos.x() >= grid[pos.y()].length;
    }
}
