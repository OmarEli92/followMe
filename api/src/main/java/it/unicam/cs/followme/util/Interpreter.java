package it.unicam.cs.followme.util;

import it.unicam.cs.followme.commands.Command;


/** An Interpreter is an object that interprets a received command parsed from the handler and supplies
 * a suitable implementation.
 * It is possible to add more commands
 * @param <E> Entity that execute the commands
**/
public interface Interpreter<E> {

    /** Implementation for CONTINUE**/
     Command<E> Continue();

    /** Implementation for DO FOREVER**/
     Command<E> doForever();

    /** Implementation for FOLLOW**/
     Command<E> follow(String label, double[] args);

    /** Implementation for MOVE**/
     Command<E> move(double[] args);

    /** Implementation for MOVE RANDOM**/
     Command<E> moveRandom(double[] args);

    /** Implementation for REPEAT**/
     Command<E> repeat(int n);

    /** Implementation for SIGNAL**/
     Command<E> signal(String label);

    /** Implementation for STOP**/
     Command<E> stop();

    /** Implementation for UNSIGNAL**/
     Command<E> unsignal(String label);

    /** Implementation for UNTIL**/
     Command<E> until(String label);




}
