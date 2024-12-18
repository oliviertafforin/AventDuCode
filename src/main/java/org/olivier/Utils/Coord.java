package org.olivier.Utils;

public class Coord extends Tuple.Pair<Integer, Integer> implements Comparable<Coord>{
    double gCost; // Coût pour atteindre ce nœud
    double hCost; // Heuristique (distance estimée à l'arrivée)
    double fCost; // Coût total
    Coord parent; // Parent pour reconstruire le chemin
    long score;
    Direction direction; // Direction de provenance (0 = haut, 1 = droite, 2 = bas, 3 = gauche)


    public Coord(int r, int c) {
        super(r, c);
    }

    public Coord(int r, int c, Direction direction) {
        super(r, c);
        this.direction = direction;
    }

    public int r() { return this.v0(); }
    public int c() { return this.v1(); }

    public Coord relative(Direction d, int n) {
        return new Coord(r() + (n * d.Δr()), c() + (n * d.Δc()));
    }
    public Coord relative(Direction d) { return relative(d, 1); }

    public Direction distance(Coord other) {
        return new Direction(other.r() - r(), other.c() - c());
    }

    @Override
    public int compareTo(Coord other) {
        return Double.compare(this.fCost, other.fCost);
    }

    public double getgCost() {
        return gCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public double getfCost() {
        return fCost;
    }

    public void setfCost(double fCost) {
        this.fCost = fCost;
    }

    public Coord getParent() {
        return parent;
    }

    public void setParent(Coord parent) {
        this.parent = parent;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}