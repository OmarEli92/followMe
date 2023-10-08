package it.unicam.cs.followme.model.exploration;

import it.unicam.cs.followme.model.*;

import java.util.*;
import java.util.stream.Collectors;

/** This class is an implementation of the interface environment and represents the environment where the entites move
 * and interact with each other and with their surroundings .
 * @param <S> shapes</S>**/
public final class ExplorationEnvironment<S extends Shape> implements Environment<S> {
    private final Set<S> areas;

    public ExplorationEnvironment(){
        this(new HashSet<S>() );
    }

    public ExplorationEnvironment(Set<S> areas){
        this.areas = areas;
    }



    @Override
    public Set<S> getAreas() {
        return areas;
    }

    @Override
    public Map<S, Point> getAreasAndTheirPositions() {
        return areas.stream()
                .collect(Collectors.toMap(shape -> shape, shape -> shape.getCenter()));
    }


    @Override
    public Set<String> getAreasConditions() {
        Set<String> labels = areas
                .stream()
                .map(Shape::getCondition)
                .collect(Collectors.toSet());
        return labels;
    }

    @Override
    public Set<String> getConditionsAtPoint(final Point point) {
        Set<String> labels = areas.stream()
                .filter(s -> s.isPointInside(point))
                .map(Shape::getCondition)
                .collect(Collectors.toSet());
        return labels;
    }


    @Override
    public Set<S> getAreasAtPoint(final Point point) {
        return areas.stream().
                filter(s -> s.isPointInside(point))
                .collect(Collectors.toSet());
    }


}
