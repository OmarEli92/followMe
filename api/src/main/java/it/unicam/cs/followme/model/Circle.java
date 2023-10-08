package it.unicam.cs.followme.model;

import java.util.Objects;
/** This class is an implementation of the shape interface and represents a cirlce object **/
public class Circle implements Shape{
    private final Point center;
    private final double radius;
    private final String condition;

    public Circle(final Point center, final double radius, final String condition) {
        this.center = center;
        this.radius = radius;
        this.condition = condition;
    }

    public double getRadius() {
        return radius;
    }
    @Override
    public Point getCenter() {
        return center;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public boolean isPointInside(Point point) {
        // sqrt((point.x - center.x)^2 +(point.y - center.y)^2)) <= radius
        return Math.sqrt(Math.pow(point.getX() - center.getX(),2)
                         + Math.pow(point.getY() - center.getY(),2)) <= radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Double.compare(circle.radius, radius) == 0 &&
                    Objects.equals(center, circle.center) &&
                    Objects.equals(condition, circle.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, radius, condition);
    }
}
