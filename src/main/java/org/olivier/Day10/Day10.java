package org.olivier.Day10;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    static final int[] dx = {0, 0, -1, 1}; // Directions: haut, bas, gauche, droite
    static final int[] dy = {-1, 1, 0, 0};
    static List<String> trailheadsDiscovered = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        String[] input = Utils.getFileContent("input_d10.txt").split("\n");
        char[][] map = new char[input.length][input[0].length()];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                map[i][j] = input[i].charAt(j);
            }
        }
        solve(map, true);
        trailheadsDiscovered.clear();
        solve(map, false);
    }

    private static void solve(char[][] map, boolean part1) {

        List<List<int[]>> paths = findPaths(map, part1);
        System.out.println("Paths:" + paths.size());
//        for (List<int[]> path : paths) {
//            for (int[] cell : path) {
//                System.out.print("(" + cell[0] + "," + cell[1] + ") -> ");
//            }
//            System.out.println("END");
//        }
    }

    public static List<List<int[]>> findPaths(char[][] grid, boolean part1) {
        List<List<int[]>> result = new ArrayList<>();
        int rows = grid.length, cols = grid[0].length;

        // toutes les positions de départ
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '0') {
                    boolean[][] visited = new boolean[rows][cols];
                    List<int[]> currentPath = new ArrayList<>();
                    dfs(grid, i, j, 0, visited, currentPath, result, part1);
                }
            }
        }

        return result;
    }

    private static void dfs(char[][] grid, int x, int y, int target, boolean[][] visited,
                            List<int[]> currentPath, List<List<int[]>> result, boolean part1) {
        int rows = grid.length, cols = grid[0].length;

        if (grid[x][y] - '0' != target) return;

        //ajout de la pos actuelle
        currentPath.add(new int[]{x, y});
        visited[x][y] = true;

        //on save le path
        if (target == 9) {
            String representationUnique = currentPath.get(0)[0] + "|" + currentPath.get(0)[1] + "|" + currentPath.get(9)[0] + "|" + currentPath.get(9)[1];
            boolean found = false;
            for (String trail : trailheadsDiscovered) {
                if (trail.equals(representationUnique)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                //dans la partie 1 les chemins doivent avoir un couple départ/arrivée unique, dans la partie 2 au contraire on veut la somme de tous les chemins
                if (part1) {
                    trailheadsDiscovered.add(representationUnique);
                }

                result.add(new ArrayList<>(currentPath));
            }
        } else {
            //on continue l'explo
            for (int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir], ny = y + dy[dir];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && !visited[nx][ny]) {
                    dfs(grid, nx, ny, target + 1, visited, currentPath, result, part1);
                }
            }
        }

        //retour arrière
        currentPath.remove(currentPath.size() - 1);
        visited[x][y] = false;
    }
}

