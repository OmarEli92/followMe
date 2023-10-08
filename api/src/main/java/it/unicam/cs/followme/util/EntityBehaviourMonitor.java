package it.unicam.cs.followme.util;

import it.unicam.cs.followme.model.Entity;
import it.unicam.cs.followme.model.Environment;
import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Shape;
import java.util.*;
import java.util.stream.Collectors;

/** The EntityPositionMonitor class provides the implementation for a monitor that keeps tracking of all the entities
 * movements and register all the conditions that are being signaled by those entities.
 **/

public class EntityBehaviourMonitor<E extends Entity,S extends Shape> implements BehaviourMonitor<E,S> {
    private Map<E,Point> entities;
    private Set<S> areas;
    private Map<String,Map.Entry<E,Point>> signaledConditions;

    public EntityBehaviourMonitor(Environment<S> environment){
        this.areas = environment.getAreas();
        this.signaledConditions = new HashMap<>();
        this.entities = new HashMap<>();
    }


    public EntityBehaviourMonitor(Environment<S> environment, Map<E,Point> entities){
        this(environment);
        this.entities = entities;
    }
    @Override
    public Map<E, Point> getEntitiesAndTheirPositions() {
        return entities;
    }

    @Override
    public Set<E> getEntities() {
        return entities.keySet();
    }

    @Override
    public void updateEntitiesPositions(int time) {
        entities.entrySet()
                .forEach((entity)-> {
                    Point direction = entity.getKey().getDirection();
                    Point currentPosition = entity.getValue();
                    double speed = entity.getKey().getSpeed();
                    double newXposition = currentPosition.getX() + direction.getX() * speed * time;
                    double newYposition = currentPosition.getY() + direction.getY() * speed * time;
                    entity.setValue(new Point(newXposition,newYposition));
                }
                );
    }

    @Override
    public boolean doesEntityExist(E entity) {
        return entities.containsKey(entity);
    }

    @Override
    public void addEntity(E entity, Point position) {
        if(!doesEntityExist(entity))
            entities.put(entity,position);
    }

    @Override
    public Optional<Point> getEntityPosition(E entity) {
        if(doesEntityExist(entity))
            return Optional.ofNullable(entities.get(entity));
        return Optional.empty();
    }

    @Override
    public Set<String> getAllSignaledConditions() {
        signaledConditions = entities.entrySet()
                .stream()
                .filter(entry-> !entry.getKey().getCondition().equals(""))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getCondition(),
                        entry -> entry
                ));
        return signaledConditions.keySet();
    }

    /** Returns the set of all entities that are signaling conditions**/
    @Override
    public Set<E> getSignalingEntities(){
        return entities
                .entrySet()
                .stream()
                .filter(entry -> !(entry.getKey().getCondition().equals("")))
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }

    /*This method returns the position of all the entities who are signaling the given condition */
    private Set<Point> getPositionOfSignalingEntities(final String condition) {
        if(this.isConditionStillSignaled(condition)){
            return entities.entrySet()
                    .stream()
                    .filter((entities) -> entities.getKey().getCondition().equals(condition))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public Set<Point> getSignaledConditionsWithinDistance(final String condition,final Point actualPosition
                                                          , final double distance){
        Set<Point> signalingConditions = getPositionOfSignalingEntities(condition);
        return signalingConditions.stream()
                .filter((position) -> position.getDistanceFromPoint(actualPosition) <= distance)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isConditionStillSignaled(final String condition) {
        return signaledConditions.containsKey(condition);
    }

    @Override
    public Set<String> getAllConditionsInTheEnvironment() {
        return areas
                .stream()
                .map((area) -> area.getCondition())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<S> getAreasWithCondition(String condition) {
        return areas
                .stream()
                .filter((areas) -> areas.getCondition().equals(condition))
                .collect(Collectors.toSet());
    }

    @Override
    public void removeEntity(E entity) {
        if(doesEntityExist(entity))
            entities.remove(entity);
    }

    @Override
    public void setUpdatedAreas(final Set<S> areas){
        this.areas = areas;
    }
}
