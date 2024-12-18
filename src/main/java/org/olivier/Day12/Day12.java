package org.olivier.Day12;

import org.olivier.Utils.*;

import java.io.IOException;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> input = List.of(Utils.getFileContent("input_d12.txt").split("\n"));
        Grid<Character> g = new Grid<>(input, new Divider.Char());

        Map<Character, List<Region>> regions = new HashMap<>();

        for (int r = 0; r < g.getHeight(); r++) {
            for (int c = 0; c < g.getWidth(); c++) {
                char type = g.get(r, c);
                if (!regions.containsKey(type)) {
                    regions.put(type, new ArrayList<>());
                }

                boolean found = false;
                // on garde toutes les régions adjacentes
                List<Region> matched = new ArrayList<>();

                for (Region region : regions.get(type)) {
                    if (region.adjacent(new Coord(r, c))) {
                        //pour chaque région adjacente on la garde
                        matched.add(region);
                        if (!found) {
                            region.plots.add(new Coord(r, c));
                            found = true;
                        }
                    }
                }

                if (!found) {
                    Region reg = new Region();
                    reg.plots.add(new Coord(r, c));
                    regions.get(type).add(reg);
                } else if (matched.size() > 1) {
                    //on fusionne les régions adjacentes
                    Region region = matched.get(0);
                    for (int i = 1; i < matched.size(); i++) {
                        region.plots.addAll(matched.get(i).plots);
                        regions.get(type).remove(matched.get(i));
                    }
                }
            }
        }

        long price = 0, bulk_price = 0;
        for (Character plant : regions.keySet()) {
            for (Region r : regions.get(plant)) {
                price += (long) r.area() * r.perimetre(g);
                bulk_price += (long) r.area() * r.sides(g);
            }
        }

        System.out.println("Day 12:");
        System.out.println(price);
        System.out.println(bulk_price);
    }

    static class Region {
        Set<Coord> plots = new HashSet<>();

        public boolean adjacent(Coord other) {
            for (Coord c : plots) {
                if (c.distance(other).magnitude() == 1.0) {
                    return true;
                }
            }
            return false;
        }

        public int area() {
            return plots.size();
        }

        //on utilise la methode radialSearch bien pratique
        public int perimetre(Grid<Character> g) {
            int result = 0;
            for (Coord c : plots) {
                result += 4;
                for (List<Character> side : g.radialSearch(c, 1, Direction.CARDINAL_DIRECTIONS)) {
                    if (g.get(c) == side.get(0)) {
                        result--;
                    }
                }
            }
            return result;
        }

        public int sides(Grid<Character> g) {
            List<Edge> sides = new ArrayList<>();

            for (Coord c : plots) {
                for (Direction d : Direction.CARDINAL_DIRECTIONS) {
                    Coord edge = c.relative(d);
                    if (g.isValid(edge) && g.get(edge) == g.get(c)) {
                        continue;
                    }

                    boolean found = false;
                    List<Edge> matched = new ArrayList<>();
                    for (Edge e : sides) {
                        if (e.adjacent(c, d)) {
                            matched.add(e);
                            if (!found) {
                                e.cells.add(c);
                                found = true;
                            }
                        }
                    }

                    if (!found) {
                        Edge e = new Edge(d);
                        e.cells.add(c);
                        sides.add(e);
                    } else if (matched.size() > 1) {
                        Edge e = matched.get(0);
                        for (int i = 1; i < matched.size(); i++) {
                            e.cells.addAll(matched.get(i).cells);
                            sides.remove(matched.get(i));
                        }
                    }
                }
            }

            return sides.size();
        }

        static class Edge {
            Set<Coord> cells = new HashSet<>();
            Direction d;

            public Edge(Direction d) {
                this.d = d;
            }

            public boolean adjacent(Coord c, Direction d) {
                if (!this.d.equals(d)) {
                    return false;
                }
                for (Coord cell : cells) {
                    if (cell.relative(Direction.left90(d)).equals(c) ||
                            cell.relative(Direction.right90(d)).equals(c)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

}
