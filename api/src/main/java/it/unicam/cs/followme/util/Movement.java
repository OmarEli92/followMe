package it.unicam.cs.followme.util;

import it.unicam.cs.followme.model.Point;

import java.util.Random;
import java.util.Set;

/** This class is responsible for providing methods that calculate a direction
 * and updatedPosition **/
public class Movement {

    /** This  method returns the direction from a given position
     * @param initialPosition of the object
     * @param destination
     * @return Point that represent the direction between [-1,1] for both coordinates **/
    public Point getDirection(final Point initialPosition, final Point destination){
        double lengthDistanceX = destination.getX() - initialPosition.getX();
        double lengthDistanceY = destination.getY() - initialPosition.getY();
        double directionX = lengthDistanceX / Math.sqrt(Math.pow(lengthDistanceX,2)+
                                                        Math.pow(lengthDistanceY,2));
        double directionY = lengthDistanceY / Math.sqrt((Math.pow(lengthDistanceX,2)+
                                              Math.pow(lengthDistanceY,2)));
        return new Point(directionX,directionY);
    }

    /** This method calculate and returns a new Position
     * @param initialPosition
     * @param direction between [-1,1] for both coordinates
     * @param speed meter for second
     * @return Point new robot's position**/
    public Point calculateNewPosition(final Point initialPosition, final Point direction, final double speed){
        double newPositionX = initialPosition.getX() + direction.getX() * speed;
        double newPositionY = initialPosition.getY() + direction.getY() * speed;
        return new Point(newPositionX,newPositionY);
    }

    /** This method returns a point in the range [x1,x2] , [y1,y2]
     * @param x1 first x coordinate
     * @param x2 second x coordinate
     * @param y1 first y coordinate
     * @param y2 second y coordinate**/
    public Point randomPointInRange(double x1, double x2, double y1, double y2) {
        Random random = new Random();
        double randomX = getRandomValueInRange(x1, x2, random);
        double randomY = getRandomValueInRange(y1, y2, random);
        return new Point(randomX, randomY);
    }

    /* Return the range*/
    private double getRandomValueInRange(double minValue, double maxValue, Random random) {
        if (minValue > maxValue) {
            return maxValue + (minValue - maxValue) * random.nextDouble();
        } else {
            return minValue + (maxValue - minValue) * random.nextDouble();
        }
    }

    /** Calculate the distance between point A and point B
     * @param A starting point
     * @param B ending point**/
    public double getDistanceFromPoint(Point A, Point B) {
        return Math.sqrt(Math.pow(A.getX() - B.getX(),2) + Math.pow(A.getY() - B.getY(),2));
    }

    /**This method calculate the average of the positions
     * @param positions set of positions**/
    public Point getMeanPosition(Set<Point> positions){
        double averageX = positions.stream()
                .mapToDouble((point) -> point.getX())
                .average()
                .orElse(Double.NaN);
        double averageY = positions.stream()
                .mapToDouble((point) -> point.getY())
                .average()
                .orElse(Double.NaN);
        return new Point(averageX,averageY);
    }
}
