package org.olivier.Day20;

import javafx.util.Pair;
import org.olivier.Utils.Coord;
import org.olivier.Utils.Direction;
import org.olivier.Utils.Grid;
import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {
    private static final boolean PART_ONE = true;
    private static Coord startCoord;
    private static Direction direction;
    private static Coord endCoord;
    private static int baseTime;

    public static final int nanoseconds = 1024;

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        List<String> map = List.of(Utils.getFileContent("input_d20.txt").split("\n"));

        List<List<Character>> result = map.stream()
                .map(s -> s.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toList())).toList();

        Grid<Character> grid = new Grid<>(result);
        System.out.println(grid);

        startCoord = grid.find('S');
        direction = Direction.E;
        endCoord = grid.find('E');
        startCoord.setDirection(direction);

        List<Coord> path = findPath(grid);
        baseTime = (int) path.get(path.size() - 1).getfCost();
        System.out.println("Partie 1 : le temps de base (sans triche) est de " + baseTime + " picosecondes");

        //On regarde le parcours pour des coordonnées proches qui permettraient d'effectuer un raccourci
        List<Pair<Coord, Coord>> shortcuts = IntStream.range(0, path.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, path.size())
                        .mapToObj(j -> new Pair<>(path.get(i), path.get(j))))
                .filter(pair -> pair.getKey().isNearby(pair.getValue()))
                .filter(pair -> grid.get((pair.getKey().v0() + pair.getValue().v0()) / 2, (pair.getKey().v1() + pair.getValue().v1()) / 2) == '#')
                .distinct()
                .toList();
        List<Pair<Coord, Coord>> raccourcisEfficaces = new ArrayList<>();
        // Afficher les résultats
        shortcuts.forEach(pair -> {
            int indexA = path.indexOf(pair.getKey());
            int indexB = path.indexOf(pair.getValue());
            int bToEnd = path.size() - indexB;
            int shortcutLength = indexA + 1 + bToEnd;
            var tempsGagne = (baseTime - shortcutLength);
            if (tempsGagne >= 100) {
                raccourcisEfficaces.add(pair);
            }
        });
        System.out.println("Total de raccourcis efficaces : " + raccourcisEfficaces.size());
        long endTime = System.nanoTime();
        // obtenir la différence entre les deux valeurs de temps nano
        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);

        //PARTIE 2
        //TODO
    }


    //on reprend le jour 16
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

    private static boolean isValid(int x, int y, Grid<Character> grid) {
        return x >= 0 && y >= 0 && x < grid.getWidth() && y < grid.getHeight() && grid.get(x, y) != '#';
    }

    public static List<Coord> findPath(Grid<Character> grid) {
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

                if (!isValid(newX, newY, grid)) continue;//vérifier que la case résultat est valide

                double tentativeGCost = current.getgCost() + 1;

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
