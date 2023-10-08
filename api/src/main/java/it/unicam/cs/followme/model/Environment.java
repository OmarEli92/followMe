package it.unicam.cs.followme.model;

import java.util.Map;

import java.util.Set;

/** The classes that will implement this interface will represent the environment
 * in which the entities will move.
 * Those class will be immutable once created they cannot be modified during the execution
 * In the environment many areas are present therefore its responsibility is to
 * maintain all the areas positions and information.
 * @param <S> Shape</S>**/
public interface Environment<S>{

    /** Returns a set of areas in the environment **/
    Set<S> getAreas();

    /** Returns a Map that represents the areas and their respective positions**/
    Map<S, Point> getAreasAndTheirPositions();

    /** Given a point the method returns the label(condition) of the area if present
     * @param point where to check for a condition**/
    Set<String>getConditionsAtPoint(final Point point);

    /** Returns all the conditions**/
    Set<String> getAreasConditions();

    /** Given a certain point this method returns the set of areas that contain this point
     * @param point point**/
    Set<S> getAreasAtPoint(final Point point);



}
