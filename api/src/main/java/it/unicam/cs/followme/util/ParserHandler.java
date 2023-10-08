package it.unicam.cs.followme.util;

import it.unicam.cs.followme.commands.*;
import it.unicam.cs.followme.utilities.FollowMeParserHandler;

import java.util.Optional;
import java.util.Stack;

/** This class implements the FollowMeParserHandler interface and generate a program
 * that will be executed by the entities
 * @param <E>**/
public class ParserHandler<E> implements FollowMeParserHandler {
    private final Interpreter<E> interpreter;
    private Command<E> firstCommand;
    private Command<E> latestCommand;                        // The last command added in the chain of commands
    private Stack<IterativeCommand<E>> iterativeCommands;    // a stack that contains conditional commands
    private boolean isDoneParsing;                           // flag that notify if the parsing process is Done
    private int numberOfCommandsAdded = 0;

    public ParserHandler(final Interpreter<E> interpreter){
        if(interpreter == null)
            throw new NullPointerException("Can't pass a null value, an interpreter object is required");
        this.interpreter = interpreter;
        firstCommand = new SingleCommand<>((entity) -> {}); // the starting point for all the commands in the chain
        latestCommand = firstCommand;

    }

    @Override
    public void parsingStarted() {
        iterativeCommands = new Stack<>();
        isDoneParsing = false;
    }


    @Override
    public void parsingDone() {
        isDoneParsing = true;
    }

    /* A direction is valid if at most one of the two coordinates is equal to 0,
     and both direction must be in the range [-1,1]*/
    private boolean isDirectionValid(final double x, final double y){
        if(x == 0 && y == 0) return false;
        if(x > 1 || x < -1) return false;
        if(y > 1 || y < -1) return false;
        return true;
    }



    /* Check if the latest command is a Conditional or a Single command.
     * If it is a single one then the next command will simply be added in the chain,
     * however if it is a conditional type, it will be checked
     * if the next command should be added in the branch where the condition is still respected
     * or in the branch where the condition is no longer respected i.e. outside the iteration
     * Commands will be added in the branch where the condition is still respected if and only if
     * the latest command added is the same command on the top of the stack, so until the condition
     * is still TRUE that branch will be executed*/
    private void checkCommandType(final Command<E> command, final String commandName){
        if(latestCommand instanceof SingleCommand<E> singleCommand ) {
            command.setCommandName(commandName);
            singleCommand.addCommand(Optional.ofNullable(command));
        }
        else if(latestCommand instanceof IterativeCommand<E> iterativeCommand) {
            command.setCommandName(commandName);
            // Branch where the condition is still TRUE
            if(!iterativeCommands.isEmpty() && iterativeCommand.equals(iterativeCommands.peek()))
                iterativeCommand.addNextCommandIfTrue(command);
            else
            // Branch where the condition is not satisfied anymore
                iterativeCommand.addNextCommandIfFalse(command);
        }
        // Update the latest command added
        latestCommand = command;
    }

    /* Add a new command in the chain of commands after is checked */
    private void addCommand(final Command<E> command, final String commandName){
        checkCommandType(command,commandName);
        if(command instanceof IterativeCommand<E> iterativeCommand)
            iterativeCommands.push(iterativeCommand);
        numberOfCommandsAdded++;
    }

    @Override
    public void moveCommand(final double[] args) {
        System.out.println("ADDED A MOVE COMMAND");
        addCommand(interpreter.move(args),"MOVE");
    }


    @Override
    public void moveRandomCommand(final double[] args) {
        System.out.println("ADDED A MOVE RANDOM COMMAND");
        addCommand(interpreter.moveRandom(args),"MOVE RANDOM");
    }

    @Override
    public void signalCommand(final String label) {
        System.out.println("ADDED A SIGNAL COMMAND");
        addCommand(interpreter.signal(label),"SIGNAL");
    }

    @Override
    public void unsignalCommand(final String label) {
        System.out.println("ADDED A UNSIGNAL COMMAND ");
        addCommand(interpreter.unsignal(label),"UNSIGNAL");
    }

    @Override
    public void followCommand(final String label, double[] args) {
        System.out.println("ADDED A FOLLOW COMMAND");
        addCommand(interpreter.follow(label,args),"FOLLOW");
    }

    @Override
    public void stopCommand() {
        System.out.println("ADDED A STOP COMMAND");
        addCommand(interpreter.stop(),"STOP");
    }

    @Override
    public void continueCommand(final int s) {
        for(int i = 0; i < s ; i++){
            System.out.println("ADDED A CONTINUE COMMAND");
            addCommand(interpreter.Continue(),"CONTINUE");
        }
    }

    @Override
    public void repeatCommandStart(final int n) {
        System.out.println("ADDED A REPEAT COMMAND");
       addCommand(interpreter.repeat(n),"REPEAT");
    }

    @Override
    public void untilCommandStart(final String label) {
        System.out.println("ADDED AN UNTIL COMMAND");
        addCommand(interpreter.until(label),"UNTIL");
    }

    @Override
    public void doForeverStart() {
        System.out.println("ADDED A DO FOREVER COMMAND");
        addCommand(interpreter.doForever(),"DO FOREVER");
    }

    /* The DONE method works only when the conditional stack isn't empty because that means
     * there are no conditional commands being executed. Checks if the
     */
    @Override
    public void doneCommand() {
        IterativeCommand<E> command = iterativeCommands.pop();
        if(command.getCounter() > 0 ) {
            command.decrementRepeatCounter();
        }
        checkCommandType(command,command.getCommandName());
    }

    public Command<E> getProgram()throws Exception {
        if(isDoneParsing)
            return firstCommand;
        throw new Exception("The program isn't complete");
    }

    public int getNumberOfCommandsAdded() {
        return numberOfCommandsAdded;
    }
}
