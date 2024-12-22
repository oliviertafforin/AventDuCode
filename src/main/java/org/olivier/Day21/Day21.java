package org.olivier.Day21;

import org.olivier.Utils.Coord;
import org.olivier.Utils.Direction;
import org.olivier.Utils.Grid;

import java.util.*;

public class Day21 {

    public static void main(String[] args) {
        List<String> codes = List.of(new String[]{"340A", "149A", "582A", "780A", "463A"});
        List<String> codesExample = List.of(new String[]{"029A", "980A", "179A", "456A", "379A"});
        List<Character> row1 = List.of(new Character[]{'7', '8', '9'});
        List<Character> row2 = List.of(new Character[]{'4', '5', '6'});
        List<Character> row3 = List.of(new Character[]{'1', '2', '3'});
        List<Character> row4 = List.of(new Character[]{'x', '0', 'A'});
        List<List<Character>> digicodeArray = new ArrayList<>();
        digicodeArray.add(row1);
        digicodeArray.add(row2);
        digicodeArray.add(row3);
        digicodeArray.add(row4);
        Grid<Character> digicode = new Grid<>(digicodeArray);

        row1 = List.of(new Character[]{'x', '^', 'A'});
        row2 = List.of(new Character[]{'<', 'v', '>'});
        List<List<Character>> remoteArray = new ArrayList<>();
        remoteArray.add(row1);
        remoteArray.add(row2);
        Grid<Character> remote = new Grid<>(remoteArray);
        int total = 0;
        for (String code : codesExample) {
            //first robot sequence
            String sequence = getSequenceForString(code, digicode, remote);
            System.out.println(sequence);

            //second robot sequence
            String sequence2 = getSequenceForString(sequence, remote, remote);
            System.out.println(sequence2);

            //third robot sequence
            String sequence3 = getSequenceForString(sequence2, remote, remote);
            System.out.println(sequence3);

            System.out.println("lenght:" + sequence3.length());
            int nombre = Integer.parseInt(code.replaceAll("[^0-9]", ""));
            System.out.println("nombre : " + nombre);
            total += (nombre * sequence3.length());
        }
        System.out.println("partie 1 : " + total);
    }

    private static String getSequenceForString(String code, Grid<Character> grid, Grid<Character> remote) {
        //pour chaque caractère du code, trouver l'input
        //d'un point de départ jusqu'à l'arrivée

        StringBuilder sequence = new StringBuilder();

        Coord start = grid.find('A');

        for (int i = 0; i < code.length(); i++) {
            sequence.append(getSequenceForChar(code.charAt(i), grid, remote, start)).append('A');
            //on ajuste la nouvelle pos
            start = grid.find(code.charAt(i));
        }

        return sequence.toString();

    }

    private static String getSequenceForChar(char key, Grid<Character> grid, Grid<Character> remote, Coord start) {
        String sequence = "";

        Coord end = grid.find(key);

        List<Coord> visited = new ArrayList<>();
        Map<Coord, Coord> cameFrom = new HashMap<>();
        List<Direction> pathDirections = new ArrayList<>();
        Queue<Coord> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        Direction[] directions = {
                Direction.E, Direction.S, Direction.W, Direction.N  // Right, Down, Left, Up
        };

        while (!queue.isEmpty()) {
            Coord current = queue.poll();

            if (current.r() == end.r() && current.c() == end.c()) {
                List<Coord> path = reconstructPath(cameFrom, start, end);
                StringBuilder keypadInputs = new StringBuilder();
                for (int i = 0; i < path.size() - 1; i++) {
                    Direction dir = path.get(i).distance(path.get(i + 1));
                    switch (dir.toString()) {
                        case "(0,-1)" -> keypadInputs.append('<');
                        case "(0,1)" -> keypadInputs.append('>');
                        case "(1,0)" -> keypadInputs.append('v');
                        case "(-1,0)" -> keypadInputs.append('^');
                        default -> throw new IllegalStateException("input incomprise : " + dir);
                    }
                }
                return keypadInputs.toString();
            }

            for (Direction dir : directions) {
                int newX = current.r() + dir.r();
                int newY = current.c() + dir.c();

                if (isValid(new Coord(newX, newY), grid, visited)) {
                    Coord neighbor = new Coord(newX, newY);
                    queue.add(neighbor);
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    pathDirections.add(dir);
                }
            }
        }

        return null;  // No path found
    }

    private static boolean isValid(Coord coord, Grid<Character> grid, List<Coord> visited) {
        return coord.r() >= 0 && coord.r() < grid.getHeight() && coord.c() >= 0 && coord.c() < grid.getWidth() && grid.get(coord) != 'x' &&
                !visited.contains(coord);
    }

    private static List<Coord> reconstructPath(Map<Coord, Coord> cameFrom, Coord start, Coord end) {
        List<Coord> path = new LinkedList<>();
        for (Coord at = end; at != null; at = cameFrom.get(at)) {
            path.add(0, at);
        }
        return path;
    }
}
