package it.unicam.cs.followme.util;

import it.unicam.cs.followme.model.Environment;
import it.unicam.cs.followme.model.Point;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Classes that implements this interface are classes which are responsible for keep tracking of
 * entities movements and signaled conditions in the environment.
 * @param <E> entities
 **/
public interface BehaviourMonitor<E,S> {



    /** Returns the position of all the entities**/
    Map<E, Point> getEntitiesAndTheirPositions();

    /** Returns the set of entities that are tracked**/
    Set<E> getEntities();

    /** Move all the entities for a certain amount of time
     * according to their respective direction and speed
     * @param time the simulation time **/
    void updateEntitiesPositions(int time);

    /** Check if the entity exists**/
    boolean doesEntityExist(E entity);

    /**Add entity if it doesn't already exist
     * @param entity the entity to add
     * @param position position of the entity**/
    void addEntity(E entity, Point position);
    /** Return the entity position
     * @param entity**/
    Optional<Point> getEntityPosition(E entity);

    /** Return the set of all the conditions currently being signaled by the entities**/
    Set<String> getAllSignaledConditions();

    /** Returns the set of all entities that are signaling conditions**/
    Set<E> getSignalingEntities();

    /** Returns a set that contains all the entities positions that are signaling the condition
     * within the given distance
     * @param condition
     * @param position
     * @param distance**/
    Set<Point> getSignaledConditionsWithinDistance(String condition, Point position, double distance);

    /** Check if a condition is still being signaled by some entity**/
    boolean isConditionStillSignaled(String condition);

    /** Returns a set that contains all the conditions inside the areas**/
    Set<String> getAllConditionsInTheEnvironment();

    /** Returns all the Areas that have the given condition
     * @param condition**/
    Set<S> getAreasWithCondition(String condition);

    /** Remove entity**/
    void removeEntity(E entity);

    /** Update the environment with the new added areas*/
    void setUpdatedAreas(Set<S> areas);
}
