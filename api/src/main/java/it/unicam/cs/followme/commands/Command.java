package it.unicam.cs.followme.commands;

import java.util.Optional;

/** Classes that implement this interface represent the various commands
 * that the entity will execute.
 * Each command is bind to the next if present like a chain of commands.
 * The execution of the command return the next command if present otherwise an Optional Command<E>
 * @param <E> The entity that will execute the command**/

public interface Command<E> {

    /** Execute the command
     * @return Optional<Command<e>> the next command if present</e>**/
    Optional<Command<E>> execute(E entity);

    String getCommandName();

    void setCommandName(String commandName);

}
