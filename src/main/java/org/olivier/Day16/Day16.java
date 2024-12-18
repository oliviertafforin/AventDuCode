package org.olivier.Day16;

import org.olivier.Utils.Coord;
import org.olivier.Utils.Direction;
import org.olivier.Utils.Grid;
import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day16 {
    private static final boolean PART_ONE = false;
    private static Grid<Character> grid;
    private static Coord renneCoord;
    private static Coord startCoord;
    private static Direction direction;
    private static Coord endCoord;


    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> map = List.of(Utils.getFileContent("input_d16.txt").split("\n"));

        List<List<Character>> result = map.stream()
                .map(s -> s.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toList())).toList();

        grid = new Grid<>(result);
        System.out.println(grid);

        startCoord = grid.find('S');
        renneCoord = grid.find('S');
        direction = Direction.E;
        endCoord = grid.find('E');
        startCoord.setDirection(direction);

        List<Coord> path = findPath();
        System.out.println(path.get(path.size() - 1).getfCost());
    }

    private static double calculateHeuristic(Coord a, Coord b) {
        return Math.abs(a.r() - b.r()) + Math.abs(a.c() - b.c()); // Distance de Manhattan
    }

    private static List<Coord> reconstructPath(Coord node) {
        List<Coord> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node);
            node = node.getParent();
        }
        return path;
    }

    private static boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < grid.getWidth() && y < grid.getHeight() && grid.get(x, y) != '#';
    }

    public static List<Coord> findPath() {
        PriorityQueue<Coord> openSet = new PriorityQueue<>();
        Set<String> closedSet = new HashSet<>();

        startCoord.setgCost(0);
        startCoord.sethCost(calculateHeuristic(startCoord, endCoord));
        startCoord.setfCost(startCoord.getgCost() + startCoord.gethCost());
        openSet.add(startCoord);

        while (!openSet.isEmpty()) {
            Coord current = openSet.poll();

            if (current.r() == endCoord.r() && current.c() == endCoord.c()) {
                return reconstructPath(current);
            }

            String currentKey = current.r() + "," + current.c() + "," + current.getDirection();
            closedSet.add(currentKey);
            // Directions : haut, droite, bas, gauche
            Direction[] directions = {current.getDirection(), Direction.left90(current.getDirection()), Direction.right90(current.getDirection())};
            for (Direction dir : directions) {
                int newX = current.r() + dir.r();//TODO reclaculer les coord
                int newY = current.c() + dir.c();

                if (!isValid(newX, newY)) continue;//vérifier que la case résultat est valide

                int turnCost = current.getDirection().equals(dir) ? 0 : 1000;
                double tentativeGCost = current.getgCost() + 1 + turnCost;

                Coord neighbor = new Coord(newX, newY);

                String neighborKey = neighbor.r() + "," + neighbor.c() + "," + neighbor.getDirection();
                if (closedSet.contains(neighborKey)) continue;

                neighbor.setDirection(dir);
                neighbor.setgCost(tentativeGCost);
                neighbor.sethCost(calculateHeuristic(neighbor, endCoord));
                neighbor.setfCost(neighbor.getgCost() + neighbor.gethCost());
                neighbor.setParent(current);

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

            }
        }

        return new ArrayList<>(); // Aucun chemin trouvé
    }
}
