package it.unicam.cs.followme.util;

import it.unicam.cs.followme.commands.Command;
import it.unicam.cs.followme.commands.IterativeCommand;
import it.unicam.cs.followme.commands.SingleCommand;
import it.unicam.cs.followme.model.Entity;
import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Shape;
import java.util.Optional;
import java.util.Set;

/** This class provides the implementations for the commands that the entities can execute
 * @param <E> entities that execute commands **/

public class EntityInterpreter<E extends Entity, S extends Shape> implements Interpreter<E> {
    /*Instance of Movement class contains several utility methods */
    private final Movement movementTool = new Movement();
    private final BehaviourMonitor<E,S> monitor;

    public EntityInterpreter(BehaviourMonitor<E,S> monitor){
        if(monitor == null)
            throw new NullPointerException("An Entity Position monitor is necessary");
        this.monitor = monitor;
    }

    /** CONTINUE command allows the entity to keep moving
     * in the same direction with the same speed.**/
    @Override
    public Command<E> Continue() {

        return new SingleCommand<>((entity) -> {});
    }

    /** DO FOREVER command allow the entity to do the commands forever**/
    @Override
    public Command<E> doForever() {
        return new IterativeCommand<>((entity) -> true);
    }


    /** FOLLOW command allows the entity to move towards a certain condition signaled
     * by other entities within a certain distance.If the distance of the condition which
     * is calculated from the average of the sums of the entities positions is less or equal
     * than the given distance, than the entity will follow it otherwise the direction will be
     * calculated within the range [-dist][dist]
     * @param label the condition to follow
     * @args distance and speed**/
    @Override
    public Command<E> follow(final String label, final double[] args) {
        return new SingleCommand<>((entity) -> {
            Optional<Point> entityPosition = monitor.getEntityPosition(entity);
            Set<Point> signalingPositions = monitor.getSignaledConditionsWithinDistance(label,entityPosition.get(),args[0]);
            if(!signalingPositions.isEmpty())
                entity.setDirection(movementTool.getMeanPosition(signalingPositions));
            else
                entity.setDirection(movementTool.randomPointInRange(- args[0], args[0], -args[0], args[0]));
            entity.setSpeed(args[1]);
        }
        );
    }

    /** MOVE command allows the entity to move to a designed direction at a certain speed
     * which is within the range [-1][1].
     * (0,0) is not a valid direction
     * @param args  direction and speed**/
    @Override
    public Command<E> move(final double[] args) {
        return new SingleCommand<>((entity) -> {
            entity.setDirection(new Point(args[0], args[1]));
            entity.setSpeed(args[2]);
            }
        );
    }

    /**MOVE RANDOM command allows the entity to move in a random direction within
     * a given range [-dist][dist] at a certain speed
     * @param args direction range and speed**/
    @Override
    public Command<E> moveRandom(final double[] args) {
        return new SingleCommand<>((entity) -> {
            Point direction = movementTool.randomPointInRange(args[0],args[1],args[2],args[3]);
            entity.setDirection(direction);
            entity.setSpeed(args[4]);
            System.out.println("I am moving towards direction x: "+direction.getX()+" y: "
            +direction.getY());
        }
        );
    }

    /** REPEAT command is used to repeat a set of commands for a certain amount of time.
     * @param n number of time the repeat command it should be executed
     * A map that keep tracks of how many iterations are left is used **/
    @Override
    public Command<E> repeat(final int n) {
        return new IterativeCommand<>((entity) -> true,n);
    }

    /**SIGNAL command allows the entity to signal a given condition.
     * @param label condition to signal**/
    @Override
    public Command<E> signal(final String label) {
        return new SingleCommand<>((entity) -> {
            entity.setCondition(label);
            System.out.println("I AM SIGNALING: "+entity.getCondition());});
    }

    /**STOP command allows the entity to stop moving.**/
    @Override
    public Command<E> stop() {
        return new SingleCommand<>((entity) -> entity.setSpeed(0));
    }

    /** UNSIGNAL command allows the entity to stop signaling the condition.
     * @param label condition that the entity will stop signaling**/
    @Override
    public Command<E> unsignal(final String label) {
        return new SingleCommand<>((entity) -> entity.setCondition(""));
    }

    /**UNTIL command allows the entity to execute a set of commands until it steps inside
     * the area that has that certain condition.
     * @param label condition**/
    @Override
    public Command<E> until(final String label) {
        return new IterativeCommand<>((entity) -> {
            Set<S> areas = monitor.getAreasWithCondition(label);
            Optional<Point> entityPosition = monitor.getEntityPosition(entity);
            return  areas.stream()
                    .anyMatch((area) -> !area.isPointInside(entityPosition.get()));
        });
    }
}
