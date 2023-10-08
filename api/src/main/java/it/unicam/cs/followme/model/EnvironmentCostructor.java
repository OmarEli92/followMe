package it.unicam.cs.followme.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** The environment interface represents objects that are immutable therefore it is necessary to
 * use a separate constructor that id able to build the environment
**/
 public class EnvironmentCostructor {

    private Set<Shape> environment;
    public EnvironmentCostructor(){
        this.environment = new HashSet<>();
    }

    /** Add the shape in the environmnt**/
    public void  addShape(final Shape shape){
        environment.add(shape);
    }

    /** Add the shape in the environmnt**/
    public void  addShape(final double[] args, final String label){
        Shape shape;
        if(args.length == 4)
            shape = new Rectangle(args[2],args[3],new Point(args[0], args[1]),label);
        else
            shape = new Circle(new Point(args[0], args[1]),args[2], label);
        environment.add(shape);
    }

    public void addShapes(final Set<Shape> shapes){
        environment = shapes;
    }

    /**Returns the environment**/
    public Set<Shape> getEnvironment(){
        return environment;
    }

}
