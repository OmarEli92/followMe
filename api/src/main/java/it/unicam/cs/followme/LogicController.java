package it.unicam.cs.followme;

import it.unicam.cs.followme.util.RobotLoader;
import it.unicam.cs.followme.commands.Command;
import it.unicam.cs.followme.commands.IterativeCommand;
import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.model.exploration.ExplorationEnvironment;
import it.unicam.cs.followme.util.*;
import it.unicam.cs.followme.utilities.*;

import java.io.File;
import java.io.IOException;
import java.util.*;


/** Controller used to generate all the necessary elements for the simulation,
 * environment,robots,program..
 * It contains all the necessary tools
 **/
public class LogicController {
    // We assume that the default time required to execute a command is 1 second
    private static final int DEFAULT_COMMAND_EXECUTION_TIME = 1;
    private int commandExecutionTime;
    private final FollowMeParser parser;
    private final ParserHandler<Robot> handler;
    private BehaviourMonitor<Robot,Shape> robotMonitor;
    private Command<Robot> program;
    private Environment<Shape> environment;
    private Set<Execution<Robot>> executions;
    private EnvironmentCostructor environmentCostructor;
    // The simulation can be executed for a certain amount of time
    private int simulationTime;
    // Simulation time passed during execution
    private int passedTime;
    private int commandExecuted = 0;
    /* Flags */
    private boolean isProgramParsed;
    private boolean areRobotsAdded;
    private boolean isEnvironmentAdded;

    public LogicController() {
        this.environmentCostructor = new EnvironmentCostructor();
        this.environment = new ExplorationEnvironment<>();
        this.robotMonitor = new EntityBehaviourMonitor<>(environment);
        this.handler = new ParserHandler<>(new EntityInterpreter<>(robotMonitor));
        this.parser = new FollowMeParser(handler);
        this.program = new IterativeCommand<>((entity) -> false);     // In case no commands was parsed
        this.commandExecutionTime = DEFAULT_COMMAND_EXECUTION_TIME;
        this.executions = new HashSet<>();
    }


    /** Load an environment from a file
     * @param file  environment file
     * @throws IOException,FollowMeParserException**/
    public void loadEnvironment(final File file)throws IOException,FollowMeParserException{
        if(checkIfIsTextFile(file)) {
            List<ShapeData> shapes = parser.parseEnvironment(file);
            System.out.println("Parsed from the file "+ shapes.size()+" figures");
            for (ShapeData shape : shapes) {
                environmentCostructor.addShape(shape.args(), shape.label());
            }
            environment = new ExplorationEnvironment<>(environmentCostructor.getEnvironment());
            robotMonitor.setUpdatedAreas(environment.getAreas());
            isEnvironmentAdded = true;
        }
    }

    /** Load robots from a file
     * @param file robot file
     * **/
    public void loadRobotsFromFile(final File file){
        try {
            if (checkIfIsTextFile(file)) {
                RobotLoader loader = new RobotLoader();
                Map<Robot, Point> robots = loader.read(file);
                for (Map.Entry<Robot, Point> entry : robots.entrySet()) {
                    robotMonitor.addEntity(entry.getKey(), entry.getValue());
                }
                areRobotsAdded = true;
            }
        }
        catch(IOException e){
            e.printStackTrace();
            }
    }

    /** Load program from a file**/
    public void loadProgram(final File file)throws Exception{
        if(checkIfIsTextFile(file)) {
            parser.parseRobotProgram(file);
            program = handler.getProgram();
            isProgramParsed = true;
        }
    }

    /* This method check if the extension of the given file is .txt*/
    private boolean checkIfIsTextFile(final File file){
        return file.getName().toLowerCase().endsWith(".txt");
    }

    /** Add robots generated in random positions
     * @param n number of robots to add**/
    public void addRobotsInRandomPositions(final int n){
        Map<Robot,Point> robots = generateRobotsInRandomPositions(n);
        addRobots(robots);
        System.out.println("Robots added in random positions: "+robotMonitor.getEntities().size());
        robots.entrySet()
                .stream()
                .forEach(entry -> System.out.println("Position X: "+entry.getValue().getX()+" Y: "+ entry.getValue().getY()));
        areRobotsAdded = true;
    }

    /* Generate robots  in random positions
     * @param n number of robots to generate*/
    public Map<Robot,Point> generateRobotsInRandomPositions(final int n){
        ObjectGenerator generator = new ObjectGenerator();
        SuppliersForGeneration supplier = new SuppliersForGeneration();
        return generator.generateRobots(supplier.getRobot(), supplier.getPosition(),n);
    }

    /** Add robot
     * @param robot robt
     * @param position position of the robot**/
    public void addRobot(Robot robot,Point position){
        robotMonitor.addEntity(robot,position);
    }

    /** Add robots
     * @param robots  maps of robots**/
    public void addRobots(final Map<Robot,Point> robots){
        robots.forEach((key, value) -> addRobot(key,value));
    }

    /** Associate each robot with the program **/
    public boolean associateProgramWithRobots(){
        if(robotMonitor.getEntities().isEmpty())
            return false;
        Set<Execution<Robot>> executions = new HashSet<>();
        for(Robot robot : robotMonitor.getEntities()){
            executions.add(new Execution<>(program,robot));
        }
        this.executions = executions;
        return true;
    }

    /** Set for how long the simulation should run
     * @param time simulation time**/
    public void setSimulationTime(final int time){
        if(time > 0)
            this.simulationTime = time;
        else
            throw new IllegalArgumentException("Simulation time must be at least 1 second");
    }

    /** Set the time that is needed for a command to complete its execution
     * if the given time is invalid , the default command execution time will be used**/
    public void setCommandExecutionTime(final int time){
        if(time <= 0){
            commandExecutionTime = DEFAULT_COMMAND_EXECUTION_TIME;
        }
        else {
            commandExecutionTime = time;
        }
    }

    /** Run the simulation for the amount of given time**/
    public void startSimulation()throws Exception{
        if(isSimulationReady()) {
            if (simulationTime > 0) {
                passedTime = 0;
                while (passedTime <= simulationTime) {
                    robotMonitor.updateEntitiesPositions(1);
                    if(passedTime % commandExecutionTime == 0)
                        executions.forEach(Execution::executeNextCommand);
                    passedTime += 1;
                    commandExecuted++;
                }
            }
            else
                throw new Exception("Illegal simulation time provided");
        }
    }

    /** Execute one command at time, the simulation advance step by step
     *  and update the robots position, the method make it easier to manage
     *  the simulation if integrated with a timeline **/
    public void nextStepInTheSimulation() {
        robotMonitor.updateEntitiesPositions(commandExecutionTime);
        executions.forEach(Execution::executeNextCommand);
        commandExecuted++;
    }

    /** Return true if the environment,the program and the robots were added
     * and associate each robot with the program**/
    public boolean isSimulationReady(){
        if(areRobotsAdded && isEnvironmentAdded && isProgramParsed){
            associateProgramWithRobots();
            return true;
        }
        return false;
    }

    /** Reset the environment**/
    public void resetEnvironment(){
        environment = new ExplorationEnvironment<>();
        isEnvironmentAdded = false;
    }

    /** Delete all Robots **/
    public void deleteRobots(){
        robotMonitor = new EntityBehaviourMonitor<>(environment);
        areRobotsAdded = false;
    }

    /** Delete Program**/
    public void deleteProgram(){
        program = new IterativeCommand<>((robot) -> false);
        isProgramParsed = false;
    }

    /** Returns the robots**/
    public Map<Robot,Point> getRobots(){
        return robotMonitor.getEntitiesAndTheirPositions();
    }

    /** Returns the areas**/
    public Set<Shape> getAreas(){
        return environment.getAreas();
    }

    public Set<Robot> getSignalingRobots(){
        return robotMonitor.getSignalingEntities();
    }
    public double getCommandExecutionTime() {
        return commandExecutionTime;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public double getPassedTime() {
        return passedTime;
    }

    public void setProgramParsed(final boolean programParsed) {
        isProgramParsed = programParsed;
    }

    public void setAreRobotsAdded(final boolean areRobotsAdded) {
        this.areRobotsAdded = areRobotsAdded;
    }

    public void setEnvironmentAdded(final boolean environmentAdded) {
        isEnvironmentAdded = environmentAdded;
    }
}
