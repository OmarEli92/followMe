package it.unicam.cs.followme.model;
/** Classes implementing the Shape interface will represent areas
 * in the unlimited space having a certain condition **/
public interface Shape {

    /** Returns the label of the shape **/
    String getCondition();

    /** Returns True if a given Point(x,y) is inside the area of the shape
     * @param point
     * @return True if the point is inside the area**/
    boolean isPointInside(Point point);

    /** Returns the point that represents the center of the shape**/
    Point getCenter();
}
