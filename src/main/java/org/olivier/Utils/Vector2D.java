package org.olivier.Utils;

import java.util.Objects;

public class Vector2D {

    private final int x;
    private final int y;


    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D direction) {
        return new Vector2D(this.x + direction.x, this.y + direction.y);
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public Vector2D rotate(int angleInDegrees) {
        double angleInRadians = Math.toRadians(angleInDegrees);

        // Calculate new coordinates
        int newX = (int) Math.round(x * Math.cos(angleInRadians) - y * Math.sin(angleInRadians));
        int newY = (int) Math.round(x * Math.sin(angleInRadians) + y * Math.cos(angleInRadians));

        return new Vector2D(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}