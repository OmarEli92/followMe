package it.unicam.cs.followme.model;

import java.util.Objects;

/** This class represent a point in a two-dimensional space**/
public class Point {
    private final double x ;
    private final double y;

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /** Get the distance from a given point B
     * @param B point to check**/
    public double getDistanceFromPoint(Point B) {
        return Math.sqrt(Math.pow(x - B.getX(),2) + Math.pow(y - B.getY(),2));
    }
}
