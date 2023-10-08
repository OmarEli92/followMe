package it.unicam.cs.followme.commands;

import java.util.Optional;
import java.util.function.Predicate;

/** This class represent a loop type command that execute a set of commands
 * until a certain condition is still valid.
 * The iterative command can be represented as a decision tree,
 * if the condition is met commands of this side of the branch will be executed,
 * if not, the command that will be executed will be from the branch that it doesn't respect the condition.
 * This helps in the management of nested iterative commands,if A is an iterativ command and is the last command
 * added in the chain , and we want to add B another iterative command. B will be added in the branch where
 * the condition is still TRUE
 * @param <E>  the entity that execute the command**/
public class IterativeCommand<E> implements Command<E> {
    private final Predicate<E> condition;
    private Optional<Command<E>> nextCommandIfConditionIsTrue;
    private Optional<Command<E>> nextCommandIfConditionIsFalse;
    private int counter = 0;
    private String commandName;

    public IterativeCommand(final Predicate<E> condition){
        this.condition = condition;
        this.nextCommandIfConditionIsFalse = Optional.empty();
        this.nextCommandIfConditionIsTrue = Optional.empty();
        this.commandName = "";
    }

    public IterativeCommand(final Predicate<E> condition, final int counter){
        this(condition);
        this.counter = counter;
    }

    @Override
    /** First check if this is a REPEAT command , in this case it checks if the counter
     * is smaller than 0 that means there are no more iterations left.
     * If it isn't a REPEAT command, check if the condition is still respected and if there is a command
     * in the isTrue branch, otherwise execute the command in isFalse branch where the condition
     * isn't met anymore.
     * @param entity the entity that execute the command**/
    public Optional<Command<E>> execute(final E entity) {
        if(counter > 0) {
            counter--;
            return nextCommandIfConditionIsTrue.get().execute(entity);
        }
        if(commandName.equals("REPEAT"))
            return nextCommandIfConditionIsFalse.get().execute(entity);
        if(condition.test(entity))
                return nextCommandIfConditionIsTrue.get().execute(entity);
        else if(!condition.test(entity))
            return nextCommandIfConditionIsFalse.get().execute(entity);
        else
            return Optional.empty();
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(final String commandName){
        this.commandName = commandName;
    }

    /** Add the next command if the condition is still respected
     * @param nextCommand command to add if the condition is still true **/
    public void addNextCommandIfTrue(final Optional<Command<E>> nextCommand){
        this.nextCommandIfConditionIsTrue = nextCommand;
    }

    /** Add the next command if the condition is still respected
      * @param nextCommand >**/
    public void addNextCommandIfTrue(final Command<E> nextCommand){
        this.addNextCommandIfTrue(Optional.of(nextCommand));
    }

    /** Add next command to the branch where it doesn't respect the condition**/
    public void addNextCommandIfFalse(final Optional<Command<E>> nextCommand){
        this.nextCommandIfConditionIsFalse = nextCommand;
    }

    /** Add next command to the branch where it doesn't respect the condition**/
    public void addNextCommandIfFalse(final Command<E> nextCommand){
        this.addNextCommandIfFalse(Optional.of(nextCommand));
    }

    /** Decrement the value of the counter that is used to keep track of how many iterations
     * are left**/
    public void decrementRepeatCounter(){
        this.counter--;
    }

    /** Returns the counter that is used to keep track of how many iyterations are left**/
    public int getCounter(){
        return this.counter;
    }
}
