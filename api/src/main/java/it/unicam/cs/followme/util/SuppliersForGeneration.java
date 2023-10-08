package it.unicam.cs.followme.util;

import it.unicam.cs.followme.model.Circle;
import it.unicam.cs.followme.model.Rectangle;
import it.unicam.cs.followme.model.Robot;
import it.unicam.cs.followme.model.Point;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** This class provides various suppliers that can be passed to the object generator**/
public class SuppliersForGeneration {
    /* Constants */
    private final int MAX_VALUE = 1000 ;        // width , height
    private final int MAX_VALUE_POSITION = 500;
    private final int MAX_RADIUS = 500;
    private final String characters = "123456789ABCDEFGHIJKLMOPQRSTUVXYWZ_";
    private Random random = new Random();

    private Supplier<Double> value = () -> Math.random() * MAX_VALUE;
    /* Returns a new position */
    private Supplier<Point> position = () -> new Point(random.nextDouble()*(MAX_VALUE_POSITION*2+1) - MAX_VALUE_POSITION,
                                                      random.nextDouble()*(MAX_VALUE_POSITION*2+1) - MAX_VALUE_POSITION);
    /* Returns a condition */
    private Supplier<String> condition = () -> IntStream.range(0, (characters).length())
            .mapToObj(characters::charAt)
            .map(String::valueOf)
            .collect(Collectors.joining());

    private Supplier<Point> direction = () -> {
        double x = 0;
        double y = 0;
        while(x == 0 && y == 0){
            x = - 1 + (random.nextDouble() * 2);
            y = - 1 + (random.nextDouble() * 2);
        }
        return new Point(x,y);
    };

    /* Returns a Circle object**/
    private Supplier<Circle> circle = () -> new Circle(position.get(),Math.random() * MAX_RADIUS,condition.get());
    /* Returns a Robot object**/
    private Supplier<Robot> robot = () -> new Robot(direction.get(),20);
    /* Returns a rectangle object**/
    private Supplier<Rectangle> rectangle = () -> new Rectangle(value.get(),value.get(),position.get(),condition.get());
    public Supplier<Double> getValue() {
        return value;
    }

    public Supplier<Point> getPosition() {
        return position;
    }

    public Supplier<Point> getDirection() {return direction;}
    public Supplier<String> getCondition() {
        return condition;
    }

    public Supplier<Circle> getCircle() {
        return circle;
    }

    public Supplier<Robot> getRobot() {
        return robot;
    }

    public Supplier<Rectangle> getRectangle() {
        return rectangle;
    }
}
