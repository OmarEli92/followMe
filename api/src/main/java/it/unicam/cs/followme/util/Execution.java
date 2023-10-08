package it.unicam.cs.followme.util;

import it.unicam.cs.followme.commands.Command;



import java.util.Optional;


/** This class represent a program execution.
 * Each entity is associated with a program to execute.
 * @param <E> entity that will execute the program**/
public class Execution<E>{
    /* Chain of commands */
    private Optional<Command<E>> currentCommand;
    private Optional<Command<E>> previousCommand;
    private final E entity;


    public Execution(final Command<E> firstCommand, final E entity){
        this.currentCommand = Optional.of(firstCommand);
        this.previousCommand = currentCommand;
        this.entity = entity;

    }

    /** Execute the next command in the program if it is present**/
    public void executeNextCommand() {
        if(currentCommand.isPresent()) {
            previousCommand = currentCommand;
            currentCommand = currentCommand.get().execute(entity);
        }
    }

    /** Returns true if all the commands have been executed**/
    public boolean isDone(){
        return currentCommand.isEmpty();
    }

}
