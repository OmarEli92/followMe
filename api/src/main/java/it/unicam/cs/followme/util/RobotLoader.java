package it.unicam.cs.followme.util;

import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Robot;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class responsible for loading robots , they can be read from a txt file,
 * the format must be like this:
 *  POSITIONX POSITIONY DIRECTIONX DIRECTIONY SPEED**/
public class RobotLoader {

    /** Returns a map that contains the robots and their initial position
     * @param file path of the text file
     * @return Map of entities
     * @throws IOException**/
    public synchronized Map<Robot, Point> read(File file) throws IOException{
        return read(file.toPath());
    }

    /** Returns a map that contains the robots and their initial position
     * @param data data
     * @return Map of entities
     * @throws IOException**/
    public synchronized Map<Robot, Point> read(String data){
        return read(List.of(data.split("\n")));
    }

    /** Returns a map that contains the robots and their initial position
     * @param path path of the text file
     * @return Map of entities
     * @throws IOException**/
    public synchronized Map<Robot, Point> read(Path path) throws IOException {
        return read(Files.readAllLines(path));
    }

    /** Returns a map that contains the robots and their intial position
     * @param lines of the text file
     * @return Map of entities
     * @throws IOException **/
    public synchronized Map<Robot, Point> read(List<String> lines){
        Map<Robot,Point> robots = new HashMap<>();
        for(String line : lines) {
            String[] values = line.trim().split(" ");
            if (values.length == 5) {
                double[] args = parseValuesFromTxtFile(values);
                //args[] = POSITION X, POSITION Y, DIRECTION X, DIRECTION Y , SPEED
                Point position = new Point(args[0], args[1]);
                Robot robot = new Robot(new Point(args[2],args[3]), args[4]);
                robots.put(robot,position);
                //System.out.println("Position parsed from the file "+"X: " +args[0]+" Y:" + args[1]);
                Arrays.stream(args).forEach(value -> System.out.print(value+" "));
            }
        }
        return robots;
    }

    /** Parse the values from a string format into a double
     * @param values the values to parse
     * @throws NumberFormatException**/
    private double[] parseValuesFromTxtFile(String[] values) {
        double[] args = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            args[i] = Double.parseDouble(values[i]);
        }
        return args;
    }

}
