package it.unicam.cs.followme.util;

import it.unicam.cs.followme.model.Circle;
import it.unicam.cs.followme.model.Rectangle;
import it.unicam.cs.followme.model.Robot;
import it.unicam.cs.followme.model.Point;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** The ObjectGenerator class is designed to provide methods that allow
 *  to create Entities and Shapes at random positions  for the simulation**/
public class ObjectGenerator{

    /** Returns the Map of the entities and their position
     * @param robot a supplier<Robot>
     * @param position a Supplier<Point>
     * @param n number of entities**/
    public Map<Robot, Point> generateRobots(final Supplier<Robot> robot,final Supplier<Point> position, int n){
        return IntStream.range(0,n)
                .mapToObj(i -> Map.entry(robot.get(),position.get()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    /** Returns a set of circles
     * @param circle a supplier<Circle>
     * @param n number of circles to generate**/
    public Set<Circle> generateCircles(final Supplier<Circle> circle,final int n){
        return IntStream.range(0,n)
                .mapToObj(i -> circle.get())
                .collect(Collectors.toSet());
    }

    /** Returns a set of rectangles
     * @param rectangle a supplier for rectangles
     * @param n number of rectangles to generate
      **/
    public Set<Rectangle> generateRectangles(final Supplier<Rectangle> rectangle,final int n){
        return IntStream.range(0,n)
                .mapToObj(i -> rectangle.get())
                .collect(Collectors.toSet());
    }

}
