package org.olivier.Day15;

import org.olivier.Utils.Coord;
import org.olivier.Utils.Grid;
import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day15 {
    private static final boolean PART_ONE = true;
    private static Grid<Character> grid;
    private static Coord robotCoord;

    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> map;
        if (PART_ONE) {
            map = List.of(Utils.getFileContent("input_d15.txt").split("\n"));
        } else {
            map = List.of(Utils.getFileContent("input_d15.txt").replace("#", "##").replace(".", "..").replace("@", "@.").replace("O", "[]").split("\n"));
        }

        List<List<Character>> result = map.stream()
                .map(s -> s.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toList())).toList();
        String moves = Utils.getFileContent("input_d15_moves.txt").replace("\n", "").replace("\r", "");

        grid = new Grid<>(result);
        System.out.println(grid);
        robotCoord = grid.find('@');
        for (int i = 0; i < moves.length(); i++) {
            char c = moves.charAt(i);
            Coord targetCoord;
            System.out.println(c);
            switch (c) {
                case '<' -> targetCoord = new Coord(robotCoord.r(), robotCoord.c() - 1);
                case '>' -> targetCoord = new Coord(robotCoord.r(), robotCoord.c() + 1);
                case '^' -> targetCoord = new Coord(robotCoord.r() - 1, robotCoord.c());
                case 'v' -> targetCoord = new Coord(robotCoord.r() + 1, robotCoord.c());
                default -> throw new IllegalArgumentException("Invalid char: " + c);
            }
            tryToMove(targetCoord, c);
            System.out.println(grid);
        }

        calculGPS();
    }

    private static void calculGPS() {
        List<Coord> coords = grid.findAll('O');
        long total = 0;
        if (PART_ONE) {
            for (Coord coord : coords) {
                total += coord.v0() * 100 + coord.v1();
            }
            System.out.println("Sommes des coordonnées GPS : " + total);
        } else {
            for (Coord coord : coords) {
                total += coord.v0() * 100 + coord.v1();
            }
            System.out.println("Sommes des coordonnées GPS part 2 : " + total);
        }
    }

    private static void tryToMove(Coord targetCoord, char direction) {
        switch (grid.get(targetCoord)) {
            case '.' -> moveRobotTo(targetCoord);
//          case '#' -> doNothing;
            case 'O' -> {
                Coord caisseSouhaitCoord;
                switch (direction) {
                    case '<' -> caisseSouhaitCoord = new Coord(targetCoord.r(), targetCoord.c() - 1);
                    case '>' -> caisseSouhaitCoord = new Coord(targetCoord.r(), targetCoord.c() + 1);
                    case '^' -> caisseSouhaitCoord = new Coord(targetCoord.r() - 1, targetCoord.c());
                    case 'v' -> caisseSouhaitCoord = new Coord(targetCoord.r() + 1, targetCoord.c());
                    default -> throw new IllegalArgumentException("Invalid char: " + direction);
                }
                if (tryToMoveCaisseTo(targetCoord, caisseSouhaitCoord, direction)) {
                    moveRobotTo(targetCoord);
                }
            }
            case '[' -> {
                //coté gauche container
                Coord coteGaucheContainerApres;
                Coord coteDroitContainerApres;
                switch (direction) {
                    case '<' -> {
//                        coteGaucheContainerApres = new Coord(targetCoord.r(), targetCoord.c() - 2);
//                        coteDroitContainerApres = new Coord(targetCoord.r(), targetCoord.c() - 1);
                        throw new IllegalStateException("Etat impossible");
                    }
                    case '>' -> {
                        coteGaucheContainerApres = new Coord(targetCoord.r(), targetCoord.c() + 1);
                        coteDroitContainerApres = new Coord(targetCoord.r(), targetCoord.c() + 2);
                    }
                    case '^' -> {
                        coteGaucheContainerApres = new Coord(targetCoord.r() - 1, targetCoord.c());
                        coteDroitContainerApres = new Coord(targetCoord.r() - 1, targetCoord.c() + 1);
                    }
                    case 'v' -> {
                        coteGaucheContainerApres = new Coord(targetCoord.r() + 1, targetCoord.c());
                        coteDroitContainerApres = new Coord(targetCoord.r() + 1, targetCoord.c() + 1);
                    }
                    default -> throw new IllegalArgumentException("Invalid char: " + direction);
                }
                Coord coteDroitContainerAvant = new Coord(targetCoord.r(), targetCoord.c() + 1);
                if (tryToMoveContainerTo(targetCoord, coteDroitContainerAvant, coteGaucheContainerApres, coteDroitContainerApres, direction, false)) {
                    moveRobotTo(targetCoord);
                }
            }
            case ']' -> {
                //coté droit container
                Coord coteGaucheContainerApres;
                Coord coteDroitContainerApres;
                switch (direction) {
                    case '<' -> {
                        coteDroitContainerApres = new Coord(targetCoord.r(), targetCoord.c() - 1);
                        coteGaucheContainerApres = new Coord(targetCoord.r(), targetCoord.c() - 2);
                    }
                    case '>' -> {
//                        coteDroitContainerSouhait = new Coord(targetCoord.r(), targetCoord.c() + 1);
                        throw new IllegalStateException("Etat impossible");
                    }
                    case '^' -> {
                        coteDroitContainerApres = new Coord(targetCoord.r() - 1, targetCoord.c());
                        coteGaucheContainerApres = new Coord(targetCoord.r() - 1, targetCoord.c() - 1);
                    }
                    case 'v' -> {
                        coteDroitContainerApres = new Coord(targetCoord.r() + 1, targetCoord.c());
                        coteGaucheContainerApres = new Coord(targetCoord.r() + 1, targetCoord.c() - 1);
                    }
                    default -> throw new IllegalArgumentException("Invalid char: " + direction);
                }
                Coord coteGaucheContainerAvant = new Coord(targetCoord.r(), targetCoord.c() - 1);
                if (tryToMoveContainerTo(coteGaucheContainerAvant, targetCoord, coteGaucheContainerApres, coteDroitContainerApres, direction, false)) {
                    moveRobotTo(targetCoord);
                }
            }
        }
    }

    private static void moveRobotTo(Coord target) {
        grid.set(target, '@');
        grid.set(robotCoord, '.');
        robotCoord = target;
    }

    private static boolean tryToMoveCaisseTo(Coord caisse, Coord targetCoord, char direction) {
        boolean success = false;
        switch (grid.get(targetCoord)) {
            case '.' -> {
                success = true;
            }
//            case '#' -> success = false;
            case 'O' -> {
                Coord caisseSouhaitCoord;
                switch (direction) {
                    case '<' -> caisseSouhaitCoord = new Coord(targetCoord.r(), targetCoord.c() - 1);
                    case '>' -> caisseSouhaitCoord = new Coord(targetCoord.r(), targetCoord.c() + 1);
                    case '^' -> caisseSouhaitCoord = new Coord(targetCoord.r() - 1, targetCoord.c());
                    case 'v' -> caisseSouhaitCoord = new Coord(targetCoord.r() + 1, targetCoord.c());
                    default -> throw new IllegalArgumentException("Invalid char: " + direction);
                }
                success = tryToMoveCaisseTo(targetCoord, caisseSouhaitCoord, direction);
            }
        }

        if (success) {
            moveCaisseTo(caisse, targetCoord);
        }

        return success;
    }

    private static boolean tryToMoveContainerTo(Coord coteGaucheContainerAvant, Coord coteDroitContainerAvant, Coord coteGaucheContainerApres, Coord coteDroitContainerApres, char direction, boolean simulation) {
        boolean success = true;

        switch (direction) {
            //gauche  on peut avoir ., # ou ]
            case '<' -> {
                switch (grid.get(coteGaucheContainerApres)) {
                    case '.' -> {
                        success = true;
                    }
                    case '#' -> {
                        success = false;
                    }
                    case ']' -> {
                        Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() - 1);
                        Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c());
                        Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() - 2);
                        Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() - 1);
                        success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);
                    }
                }
            }
            case '>' -> {
                //on peut avoir ., # ou [
                switch (grid.get(coteDroitContainerApres)) {
                    case '.' -> {
                        success = true;
                    }
                    case '#' -> {
                        success = false;
                    }
                    case '[' -> {
                        Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                        Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 2);
                        Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 2);
                        Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 3);
                        success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);
                    }
                }

            }
            case '^' -> {
                //on peut avoir tous les cas de figure
                switch (grid.get(coteGaucheContainerApres)) {
                    case '.' -> {
                        switch (grid.get(coteDroitContainerApres)) {
                            case '#' -> {
                                success = false;
                            }
                            case '[' -> {
                                Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                                Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 2);
                                Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c() + 1);
                                Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c() + 2);
                                success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);
                            }
                        }
                    }
                    case '#' -> {
                        success = false;
                    }
                    case '[' -> {
                        Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c());
                        Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                        Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c());
                        Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c() + 1);
                        success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);
                    }
                    case ']' -> {
                        Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() - 1);
                        Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c());
                        Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c() - 1);
                        Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c());
                        //vérif que les deux fonctonnnent avant todo
                        success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, true);

                        if (success) {
                            switch (grid.get(coteDroitContainerApres)) {
                                case '#' -> {
                                    success = false;
                                }
                                case '[' -> {
                                    Coord coteGaucheContainerAdjacent2 = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                                    Coord coteDroitContainerAdjacent2 = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 2);
                                    Coord coteGaucheContainerAdjacentSouhaite2 = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c() + 1);
                                    Coord coteDroitContainerAdjacentSouhaite2 = new Coord(coteGaucheContainerApres.r() - 1, coteGaucheContainerApres.c() + 2);
                                    success = tryToMoveContainerTo(coteGaucheContainerAdjacent2, coteDroitContainerAdjacent2, coteGaucheContainerAdjacentSouhaite2, coteDroitContainerAdjacentSouhaite2, direction, simulation);

                                }
                            }
                            if (success) {
                                success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);

                            }

                        }
                    }
                }
            }
            case 'v' -> {
                //on peut avoir tous les cas de figure
                switch (grid.get(coteGaucheContainerApres)) {
                    case '.' -> {
                        switch (grid.get(coteDroitContainerApres)) {
                            case '#' -> {
                                success = false;
                            }
                            case '[' -> {
                                Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                                Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 2);
                                Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c() + 1);
                                Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c() + 2);
                                success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);
                            }
                        }
                    }
                    case '#' -> {
                        success = false;
                    }
                    case '[' -> {
                        Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c());
                        Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                        Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c());
                        Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c() + 1);
                        success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);


                    }
                    case ']' -> {
                        Coord coteGaucheContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() - 1);
                        Coord coteDroitContainerAdjacent = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c());
                        Coord coteGaucheContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c() - 1);
                        Coord coteDroitContainerAdjacentSouhaite = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c());
                        success = tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, true);

                        if (success) {
                            switch (grid.get(coteDroitContainerApres)) {
                                case '.' -> success = true;
                                case '#' -> success = false;
                                case '[' -> {
                                    Coord coteGaucheContainerAdjacent2 = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 1);
                                    Coord coteDroitContainerAdjacent2 = new Coord(coteGaucheContainerApres.r(), coteGaucheContainerApres.c() + 2);
                                    Coord coteGaucheContainerAdjacentSouhaite2 = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c() + 1);
                                    Coord coteDroitContainerAdjacentSouhaite2 = new Coord(coteGaucheContainerApres.r() + 1, coteGaucheContainerApres.c() + 2);
                                    success = tryToMoveContainerTo(coteGaucheContainerAdjacent2, coteDroitContainerAdjacent2, coteGaucheContainerAdjacentSouhaite2, coteDroitContainerAdjacentSouhaite2, direction, simulation);
                                }
                            }
                            if (success) {
                                tryToMoveContainerTo(coteGaucheContainerAdjacent, coteDroitContainerAdjacent, coteGaucheContainerAdjacentSouhaite, coteDroitContainerAdjacentSouhaite, direction, simulation);
                            }
                        }
                    }
                }
            }
        }

        if (success && !simulation) {
            moveContainerTo(coteGaucheContainerAvant, coteGaucheContainerApres);
        }

        return success;
    }

    private static void moveCaisseTo(Coord caisse, Coord targetCoord) {
        grid.set(targetCoord, 'O');
        grid.set(caisse, '.');
    }

    private static void moveContainerTo(Coord containerGaucheAvant, Coord containerGaucheApres) {
        Coord containerDroitAvant = new Coord(containerGaucheAvant.r(), containerGaucheAvant.c() + 1);
        Coord containerDroitApres = new Coord(containerGaucheApres.r(), containerGaucheApres.c() + 1);

        grid.set(containerGaucheAvant, '.');
        grid.set(containerDroitAvant, '.');
        grid.set(containerGaucheApres, '[');
        grid.set(containerDroitApres, ']');

    }
}
