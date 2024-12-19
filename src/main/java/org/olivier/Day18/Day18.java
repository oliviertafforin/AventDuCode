package org.olivier.Day18;

import org.olivier.Utils.*;

import java.io.IOException;
import java.util.*;

public class Day18 {
    private static final boolean PART_ONE = true;
    private static Grid<Character> grid;
    private static Coord startCoord;
    private static Direction direction;
    private static Coord endCoord;

    public static final int WIDTH = 71;
    public static final int HEIGTH = 71;
    public static final int nanoseconds = 1024;

    public static void main(String[] args) throws IOException {
        List<String> input = List.of(Utils.getFileContent("input_d18.txt").replace("\r", "").split("\n"));
        List<Coord> coords = input.stream().map(s -> new Coord(Integer.parseInt(s.split(",")[1]), Integer.parseInt(s.split(",")[0]))).toList();

        List<List<Character>> array = new ArrayList<>();
        for (int i = 0; i < HEIGTH; i++) {
            array.add(new ArrayList<>());
            for (int j = 0; j < WIDTH; j++) {
                array.get(i).add('.');
            }
        }
        grid = new Grid<>(array);
        for(int i = 0; i < coords.size() && i < nanoseconds; i++){
            grid.set(coords.get(i),'#');
        }
        System.out.println(grid);

        startCoord = new Coord(0,0);
        direction = Direction.E;
        endCoord = new Coord(HEIGTH-1,WIDTH-1);
        startCoord.setDirection(direction);

        List<Coord> path = findPath();
        System.out.println("partie 1 : "+path.get(path.size() - 1).getfCost());
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
