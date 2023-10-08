package it.unicam.cs.followme.model;

import java.util.Set;

/** Classes implementing this interface are used to represent the entities moving in an
 * unlimited space that interact with areas and other entities.
 * The entity is able to move and signal certain conditions**/
public interface Entity {

    /** Returns the velocity of the entity
     * @return velocity of the entity when moving around **/
    double getSpeed();

    /** Returns the direction in which the entity is currently moving **/
    Point getDirection();

    /** Return the conditions of the entity **/
    String getCondition();

    /** Set a new condition **/
    void setCondition(final String condition);

    /** The entity move towards a direction
     *@param direction of the entity
     **/
    void setDirection(Point direction);

    /** Set the speed**/
    void setSpeed(double speed);


}
