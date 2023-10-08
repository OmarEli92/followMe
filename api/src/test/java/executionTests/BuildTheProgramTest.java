package executionTests;
import it.unicam.cs.followme.commands.Command;
import it.unicam.cs.followme.model.Environment;
import it.unicam.cs.followme.model.Robot;
import it.unicam.cs.followme.model.Shape;
import it.unicam.cs.followme.model.exploration.ExplorationEnvironment;
import it.unicam.cs.followme.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuildTheProgramTest {
    Environment<Shape> environment = new ExplorationEnvironment<>();
    BehaviourMonitor<Robot,Shape> monitor = new EntityBehaviourMonitor<>(environment);
    Interpreter<Robot> interpreter = new EntityInterpreter<>(monitor);
    ParserHandler<Robot> handler = new ParserHandler<Robot>(interpreter);




    public void generateFirstProgram(){
        handler.parsingStarted();
        handler.moveCommand(new double[]{0,0.3,4});
        handler.signalCommand("SAFE");
        handler.unsignalCommand("SAFE");
        handler.moveRandomCommand(new double[]{0.4,1,0.1,1,4});
        handler.parsingDone();
    }

    @Test
    public void checkFirstProgramValidity() {
        this.generateFirstProgram();
        try {
            Command<Robot> program = handler.getProgram();
            assertEquals(handler.getNumberOfCommandsAdded(),4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateSecondProgram(){
        handler = new ParserHandler<>(interpreter);
        handler.parsingStarted();
        handler.signalCommand("NOT SAFE");
        handler.unsignalCommand("NOT SAFE");
        handler.signalCommand("WATER");
        handler.moveCommand(new double[]{0.6,0.5,24});
        handler.moveRandomCommand(new double[]{0.1,0.5,-0.1,-1,15});
        handler.continueCommand(4);
        handler.untilCommandStart("HOME");
        handler.doneCommand();
        handler.parsingDone();
    }

    @Test
    public void checkSecondProgramValidity() {
        this.generateSecondProgram();
        try {
            Command<Robot> program = handler.getProgram();
            assertEquals(handler.getNumberOfCommandsAdded(), 10);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generateThirdProgram(){
        handler = new ParserHandler<>(interpreter);
        handler.parsingStarted();
        handler.repeatCommandStart(3);
        handler.signalCommand("WATER");
        handler.unsignalCommand("WATER");
        handler.repeatCommandStart(2);
        handler.signalCommand("FIRE");
        handler.unsignalCommand("FIRE");
        handler.repeatCommandStart(2);
        handler.moveCommand(new double[]{0.3,-1,5});
        handler.doneCommand();
        handler.doneCommand();
        handler.doneCommand();
        handler.parsingDone();
    }

    @Test
    public void checkThirdProgramValidity() {
        this.generateThirdProgram();
        try {
            Command<Robot> program = handler.getProgram();
            assertEquals(handler.getNumberOfCommandsAdded(),8);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
