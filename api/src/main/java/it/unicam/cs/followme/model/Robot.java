package it.unicam.cs.followme.model;

import java.util.HashSet;
import java.util.Set;

/** The robot class that implements the interface Entity and moves in the space
 * and interact with objects **/
public class Robot implements Entity{
    private double speed;
    private Point direction;
    private String condition;


    public Robot(Point direction, double speed){
        this.direction = direction;
        this.speed = speed;
        this.condition = "";
    }
    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public Point getDirection() {
        return direction;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public void setCondition(final String condition) {
        this.condition = condition;
    }

    @Override
    public void setDirection(final Point direction) {
        this.direction = direction;
    }

    @Override
    public void setSpeed(final double speed) {
        this.speed = speed;
    }

}
