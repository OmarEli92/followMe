package executionTests;

import it.unicam.cs.followme.LogicController;
import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;



public class LogicControllerTest {
    LogicController controller = new LogicController();
    String absolutePath = System.getProperty("user.dir");
    String relativePath = "app\\src\\main\\resources\\defaultSimulation/";
    String fullPath = absolutePath.replace("api","") + relativePath;
    @Test
    public void testLoadEnvironment() throws IOException, FollowMeParserException {
        File environmentFile = new File(fullPath+"environment.txt");
        System.out.println(environmentFile.getAbsolutePath());
        controller.loadEnvironment(environmentFile);
        assertFalse(controller.getAreas().isEmpty());
        assertFalse(controller.isSimulationReady());
    }

    @Test
    public void testLoadRobotsFromFile() {
        File robotsFile = new File(fullPath+"robot.txt");
        controller.loadRobotsFromFile(robotsFile);
        assertFalse(controller.getRobots().isEmpty());
        assertFalse(controller.isSimulationReady());
    }

    @Test
    public void testLoadProgram() throws Exception {
        File programFileA = new File(fullPath+"programA.txt");
        assertDoesNotThrow(() -> controller.loadProgram(programFileA));
        assertFalse(controller.isSimulationReady());
        File programFileB = new File(fullPath+"programB.txt");
        assertDoesNotThrow(() -> controller.loadProgram(programFileB));
        assertFalse(controller.isSimulationReady());
        File programFileC = new File(fullPath+"programC.txt");
        assertDoesNotThrow(() -> controller.loadProgram(programFileC));
        assertFalse(controller.isSimulationReady());
    }

    @Test
    public void testAddRobotsInRandomPositions() {
        controller.addRobotsInRandomPositions(5);
        assertFalse(controller.getRobots().isEmpty());
        assertEquals(5, controller.getRobots().size());
    }

    @Test
    public void testGenerateRobotsInRandomPositions() {
        Map<Robot, Point> robots = controller.generateRobotsInRandomPositions(3);
        assertFalse(robots.isEmpty());
        assertEquals(3, robots.size());
        Map<Robot, Point> negativeRobots = controller.generateRobotsInRandomPositions(-2);
        assertTrue(negativeRobots.isEmpty());
    }

    @Test
    public void testAssociateProgramWithRobots() throws Exception {
        File programFile = new File(fullPath+"programA.txt");
        controller.loadProgram(programFile);
        Map<Robot,Point> robots = controller.generateRobotsInRandomPositions(20);
        controller.addRobots(robots);
        assertTrue(controller.associateProgramWithRobots());
        assertFalse(controller.isSimulationReady());
    }

    @Test
    public void testStartSimulation() throws Exception {
        File environmentFile = new File(fullPath+"environment.txt");
        controller.loadEnvironment(environmentFile);
        File robotsFile = new File(fullPath+"robot.txt");
        controller.loadRobotsFromFile(robotsFile);
        assertFalse(controller.getRobots().isEmpty());
        File programFile = new File(fullPath+"programC.txt");
        assertDoesNotThrow(() -> controller.loadProgram(programFile));
        assertTrue(controller.associateProgramWithRobots());
        controller.setSimulationTime(5);
        controller.setCommandExecutionTime(1);
        assertDoesNotThrow(controller::startSimulation);
        assertEquals(6, controller.getPassedTime());
    }

    @Test
    public void testNextStepInTheSimulation() {
        try {
            File environmentFile = new File(fullPath+"environment.txt");
            controller.loadEnvironment(environmentFile);
            File robotsFile = new File(fullPath+"robot.txt");
            controller.loadRobotsFromFile(robotsFile);
            assertFalse(controller.getRobots().isEmpty());
            File programFile = new File(fullPath+"programC.txt");
            assertDoesNotThrow(() -> controller.loadProgram(programFile));
            assertTrue(controller.associateProgramWithRobots());
            controller.nextStepInTheSimulation();
            assertTrue(controller.isSimulationReady());
            assertDoesNotThrow(controller::nextStepInTheSimulation);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testIsSimulationReady() throws Exception {
        assertFalse(controller.isSimulationReady());
        File environmentFile = new File(fullPath+"environment.txt");
        controller.loadEnvironment(environmentFile);
        File robotsFile = new File(fullPath+"robot.txt");
        controller.loadRobotsFromFile(robotsFile);
        File programFile = new File(fullPath+"programA.txt");
        assertDoesNotThrow(() -> controller.loadProgram(programFile));
        assertTrue(controller.isSimulationReady());
        }
}
