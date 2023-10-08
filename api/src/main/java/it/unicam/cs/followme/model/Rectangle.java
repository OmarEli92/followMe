package it.unicam.cs.followme.model;

import java.util.Objects;
/** Implementation of the Shape interface, represent a Rectangle**/
public class Rectangle implements Shape{

    private final double width;
    private final double height;
    private final String condition;
    private final Point center;

    public Rectangle(final double width, final double height,
                     final Point center, final String condition){
        this.width = width;
        this.height = height;
        this.condition = condition;
        this.center = center;
    }


    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public boolean isPointInside(final Point point) {
        /* Only the left bottom and the right upper vertices are needed */
        double leftBottomVertexX = center.getX() - width/2;
        double leftBottomVertexY = center.getY() - height/2;
        double rightUpperVertexX = center.getX() + width/2;
        double rightUpperVertexY = center.getY() + height/2;
        return verify(new double[]{leftBottomVertexX,leftBottomVertexY},
                      new double[]{rightUpperVertexX,rightUpperVertexY},
                point);
    }

    private boolean verify(final double[]leftBottomVertex, final double[] rightUpperVertex,final Point point){
        return(leftBottomVertex[0] <= point.getX()  &&
               point.getX() <= rightUpperVertex[0]  &&
                leftBottomVertex[1] <= point.getY() &&
                point.getY() <= rightUpperVertex[1]);
    }

    @Override
    public Point getCenter() {
        return center;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(rectangle.width, width) == 0 && Double.compare(rectangle.height, height) == 0 && condition.equals(rectangle.condition) && center.equals(rectangle.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, condition, center);
    }
}
