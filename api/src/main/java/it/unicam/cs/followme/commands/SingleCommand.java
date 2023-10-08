package it.unicam.cs.followme.commands;

import java.util.Optional;
import java.util.function.Consumer;

/** A single command consist of the action that will be performed by the entity e.g. MOVE, STOP, SIGNAL ...
 *  and the next command linked to it
 *  When a command is executed it returns the next command in the chain
 *  @param <E> Entity that execute the command
 *  **/

public class SingleCommand<E> implements Command<E>{
    private Consumer<E> actionToPerform;
    private Optional<Command<E>> nextCommand;
    private String commandName = "";

    public SingleCommand(Consumer<E> actionToPerform){
        this.actionToPerform = actionToPerform;
        this.nextCommand = Optional.empty();
    }

    @Override
    public Optional<Command<E>> execute(final E entity) {
        actionToPerform.accept(entity);
        System.out.println("I executed the command "+ commandName);
        return nextCommand;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void setCommandName(final String commandName) {
        this.commandName = commandName;
    }

    /** Add the next command **/
    public void addCommand(Optional<Command<E>> nextCommand){
        this.nextCommand = nextCommand;
    }

    /** Add the next command if not in the Optional form**/
    public void addCommand(final Command<E> nextCommand){
        this.nextCommand = Optional.ofNullable(nextCommand);
    }

    public Optional<Command<E>> getNextCommand(){
        return nextCommand;
    }



}
